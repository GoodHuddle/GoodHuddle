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

package com.goodhuddle.huddle.web.api;

import com.goodhuddle.huddle.domain.MenuItem;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.service.request.menu.UpdateMenuItemLocationRequest;
import com.goodhuddle.huddle.web.api.util.UpdateIdChecker;
import com.goodhuddle.huddle.web.builder.menu.MenuDetailBuilder;
import com.goodhuddle.huddle.web.builder.menu.MenuItemDetailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu/item")
public class MenuItemApiController {

    private static final Logger log = LoggerFactory.getLogger(MenuItemApiController.class);

    private final MenuService menuService;
    private final MenuDetailBuilder menuDetailBuilder;
    private final MenuItemDetailBuilder menuItemDetailBuilder;

    @Autowired
    public MenuItemApiController(MenuService menuService,
                                 MenuDetailBuilder menuDetailBuilder,
                                 MenuItemDetailBuilder menuItemDetailBuilder) {

        this.menuService = menuService;
        this.menuDetailBuilder = menuDetailBuilder;
        this.menuItemDetailBuilder = menuItemDetailBuilder;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MenuItem getMenuItem(@PathVariable long id) {
        log.debug("Retrieving menu item with ID '{}'", id);
        return menuService.getMenuItem(id);
    }

    @RequestMapping(value = "/{id}/location", method = RequestMethod.PUT)
    public void updateMenuItemLocation(@PathVariable long id, @RequestBody UpdateMenuItemLocationRequest location) {
        UpdateIdChecker.checkId(id, location);
        log.debug("Moving menu item with ID '{}' under {}, position {}",
                id, location.getParentItemId(), location.getPosition());
        menuService.updateMenuItemLocation(location);
    }
}
