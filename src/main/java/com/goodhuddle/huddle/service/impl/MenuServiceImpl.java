/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.goodhuddle.huddle.service.impl;

import com.goodhuddle.huddle.domain.Menu;
import com.goodhuddle.huddle.domain.MenuItem;
import com.goodhuddle.huddle.repository.MenuItemRepository;
import com.goodhuddle.huddle.repository.MenuRepository;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.service.exception.MenuExistsException;
import com.goodhuddle.huddle.service.request.menu.UpdateMenuItemDetailsRequest;
import com.goodhuddle.huddle.service.request.menu.UpdateMenuItemLocationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final HuddleService huddleService;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository, MenuItemRepository menuItemRepository, HuddleService huddleService) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemRepository;
        this.huddleService = huddleService;
    }


    @Override
    public Menu getMenu(String name) {
        return this.menuRepository.findByHuddleAndName(huddleService.getHuddle(), name);
    }

    @Override
    public Menu createMenu(String name) throws MenuExistsException {
        Menu menu = this.menuRepository.save(new Menu(huddleService.getHuddle(), name));
        log.info("Menu '{}' created with ID {}", menu.getName(), menu.getId());
        return menu;
    }

    @Override
    public Menu getMainMenu() {
        return getMenu(MAIN);
    }

    @Override
    public Menu getUnlinkedMenu() {
        return getMenu(UNLINKED);
    }

    @Override
    public MenuItem getMenuItem(long id) {
        return menuItemRepository.findByHuddleAndId(huddleService.getHuddle(), id);
    }

    @Override
    public MenuItem createMenuItem(String label, long menuId, Long parentItemId, int position,
                                   MenuItem.Type targetType, Long targetId, String url) {

        Menu menu = menuRepository.findByHuddleAndId(huddleService.getHuddle(), menuId);
        MenuItem parent = null;
        if (parentItemId != null) {
            parent = menuItemRepository.findByHuddleAndId(huddleService.getHuddle(), parentItemId);
        }

        MenuItem item = new MenuItem(huddleService.getHuddle(), label, menu, parent, position, targetType, targetId, url);
        List<MenuItem> affectedItems = addToParent(menu, parent, item, position);
        menuItemRepository.save(affectedItems);

        log.info("New menu item '{}' created with ID {}", item.getLabel(), item.getId());
        return item;
    }

    @Override
    public MenuItem updateMenuItemDetails(UpdateMenuItemDetailsRequest request) {
        MenuItem item = menuItemRepository.findByHuddleAndId(huddleService.getHuddle(), request.getId());
        item.setLabel(request.getLabel());
        item.setUrl(request.getUrl());
        item = menuItemRepository.save(item);
        log.info("MenuItem {} was updated to have label '{}' and target URL '{}'",
                item.getId(), item.getLabel(), item.getUrl());
        return item;
    }

    @Override
    public MenuItem updateMenuItemLocation(UpdateMenuItemLocationRequest request) {

        Menu menu = menuRepository.findByHuddleAndId(huddleService.getHuddle(), request.getMenuId());

        MenuItem parent = null;
        if (request.getParentItemId() != null) {
            parent = menuItemRepository.findByHuddleAndId(huddleService.getHuddle(), request.getParentItemId());
            if (!menu.getId().equals(parent.getMenu().getId())) {
                throw new IllegalArgumentException("Target menu (id=" + menu.getId()
                        + ") does not match menu of target parent (id=" + parent.getMenu().getId() + ")");
            }
        }

        MenuItem item = menuItemRepository.findByHuddleAndId(huddleService.getHuddle(), request.getId());
        List<MenuItem> affectedItems = moveToParent(menu, parent, item, request.getPosition());
        menuItemRepository.save(affectedItems);

        if (!item.getMenu().getId().equals(menu.getId())) {
            log.debug("MenuItem was moved to a new menu, adjusting references");
            affectedItems = changeMenu(item, menu, new ArrayList<MenuItem>());
            menuItemRepository.save(affectedItems);
        }

        log.info("MenuItem {} was moved to position {}, under parent {}, in menu {}", item.getId(),
                item.getPosition(), (parent != null ? parent.getId() : null), menu.getName());

        return item;
    }

    private List<MenuItem> changeMenu(MenuItem item, Menu menu, List<MenuItem> affectedItems) {
        item.setMenu(menu);
        affectedItems.add(item);
        if (item.getItems() != null) {
            for (MenuItem child : item.getItems()) {
                changeMenu(child, menu, affectedItems);
            }
        }
        return affectedItems;
    }

    @Override
    public void deleteMenuItem(long menuItemId, boolean deepDeleteChildItems) {
        MenuItem item = menuItemRepository.findByHuddleAndId(huddleService.getHuddle(), menuItemId);
        if (item != null) {

            if (deepDeleteChildItems && item.getItems() != null) {
                for (MenuItem child : item.getItems()) {
                    deepDeleteMenuItem(child);
                }
            }

            List<MenuItem> siblings = getSiblings(item);
            siblings.remove(item);
            menuItemRepository.delete(item);
            reindex(siblings);
            menuItemRepository.save(siblings);

        } else {
            log.debug("Ignoring delete request of menu item {}, as it does not exist", menuItemId);
        }
    }

    private void deepDeleteMenuItem(MenuItem item) {
        log.info("Deep deleting menu item {}, including target {}.{}",
                item.getId(), item.getTargetType(), item.getTargetId());
        switch (item.getTargetType()) {
            case page:
                // todo delete attached page
                throw new RuntimeException("Deleting sub-pages is not implemented yet");
            default:
                throw new RuntimeException("Not implemented yet");
        }
        //menuItemRepository.delete(item);
    }


    private List<MenuItem> moveToParent(Menu menu, MenuItem parent, MenuItem item, int position) {
        log.debug("Move menu item {} to parent {} at position {} in menu {}",
                item.getId(), (parent != null ? parent.getId() : null), position, menu.getName());

        // remove from old parent
        MenuItem oldParent = item.getParent();
        if (oldParent != null) {
            log.debug("Removing item from old parent: {}", oldParent.getId());
            oldParent.getItems().remove(item);
        } else {
            log.debug("Removing item from root of old menu: {}", item.getMenu().getName());
            item.getMenu().getItems().remove(item);
        }

        // adjust target position if moving above old position within same parent
        if (item.getMenu().getId().equals(menu.getId())
                && (oldParent != null && parent != null && oldParent.getId().equals(parent.getId())
                    || (oldParent == null && parent == null)) ) {
            if (position >= item.getPosition()) {
                position--;
                log.debug("Moving above old position within same parent, so target position has been adjusted to {}", position);
            }
        }

        return addToParent(menu, parent, item, position);
    }

    private List<MenuItem> addToParent(Menu menu, MenuItem parent, MenuItem item, int position) {
        List<MenuItem> siblings = getSiblings(menu, parent);
        position = Math.max(0, Math.min(siblings.size(), position));

        log.debug("Moving menu item {} to parent {} at position {} in menu {}",
                item.getId(), (parent != null ? parent.getId() : null), position, menu.getName());

        item.setParent(parent);
        siblings.add(position, item);
        reindex(siblings);
        return siblings;
    }

    private List<MenuItem> getSiblings(MenuItem item) {
        return getSiblings(item.getMenu(), item.getParent());
    }

    private List<MenuItem> getSiblings(Menu menu, MenuItem parent) {
        List<MenuItem> siblings;
        if (parent == null) {
            siblings = menu.getItems();
        } else {
            siblings = parent.getItems();
            if (siblings == null)  {
                siblings = new ArrayList<>();
                parent.setItems(siblings);
            }
        }
        return siblings;
    }

    private void reindex(List<MenuItem> items) {
        // adjust all indexes to be in order
        for (int i = 0; i < items.size(); i++) {
            log.debug("Repositioning {} to position {}", items.get(i).getLabel(), i);
            items.get(i).setPosition(i);
        }
    }

}
