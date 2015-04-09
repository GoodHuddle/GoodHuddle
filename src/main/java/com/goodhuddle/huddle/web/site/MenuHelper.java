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

package com.goodhuddle.huddle.web.site;

import com.goodhuddle.huddle.domain.MenuItem;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.web.builder.menu.MenuDetailBuilder;
import com.goodhuddle.huddle.web.dto.menu.MenuDetail;
import com.goodhuddle.huddle.web.dto.menu.MenuItemDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MenuHelper {

    private final MenuService menuService;
    private final MenuDetailBuilder menuDetailBuilder;

    @Autowired
    public MenuHelper(MenuService menuService, MenuDetailBuilder menuDetailBuilder) {
        this.menuService = menuService;
        this.menuDetailBuilder = menuDetailBuilder;
    }

    public void addMenuAndPath(Model model, MenuItem menuItem) {

        MenuDetail menu = menuDetailBuilder.build(menuService.getMainMenu());
        List<MenuItemDetail> path;
        if (menuItem != null) {
            path = selectMenuItem(menu.getItems(), menuItem.getId());
        } else {
            path = Collections.EMPTY_LIST;
        }
        model.addAttribute("menu", menu);
        model.addAttribute("path", path);
    }

    private List<MenuItemDetail> selectMenuItem(List<MenuItemDetail> items, Long menuItemId) {
        if (items != null && menuItemId != null) {
            for (MenuItemDetail item : items) {
                if (item.getId() == menuItemId) {
                    item.setSelected(true);
                    ArrayList<MenuItemDetail> path = new ArrayList<>();
                    path.add(item);
                    return path;
                }
                List<MenuItemDetail> path = selectMenuItem(item.getItems(), menuItemId);
                if (path != null) {
                    item.setSelected(true);
                    path.add(0, item);
                    return path;
                }
            }
        }
        return null;
    }
}
