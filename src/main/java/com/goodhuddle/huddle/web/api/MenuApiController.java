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

import com.goodhuddle.huddle.domain.Menu;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.web.builder.menu.MenuDetailBuilder;
import com.goodhuddle.huddle.web.dto.menu.MenuDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuApiController {

    private static final Logger log = LoggerFactory.getLogger(MenuApiController.class);

    private final MenuService menuService;
    private final MenuDetailBuilder menuDetailBuilder;

    @Autowired
    public MenuApiController(MenuService menuService,
                             MenuDetailBuilder menuDetailBuilder) {

        this.menuService = menuService;
        this.menuDetailBuilder = menuDetailBuilder;
    }

    @RequestMapping(value = "{name}", method = RequestMethod.GET)
    public MenuDetail getMenu(@PathVariable String name) {
        log.debug("Retrieving menu with name '{}'", name);
        Menu menu = menuService.getMenu(name);
        return menuDetailBuilder.build(menu);
    }
}
