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

package com.goodhuddle.huddle.service;

import com.goodhuddle.huddle.domain.Menu;
import com.goodhuddle.huddle.domain.MenuItem;
import com.goodhuddle.huddle.service.exception.MenuExistsException;
import com.goodhuddle.huddle.service.request.menu.UpdateMenuItemDetailsRequest;
import com.goodhuddle.huddle.service.request.menu.UpdateMenuItemLocationRequest;

public interface MenuService {

    public static final String MAIN = "main";
    public static final String UNLINKED = "unlinked";


    Menu createMenu(String name) throws MenuExistsException;

    Menu getMainMenu();

    Menu getUnlinkedMenu();

    Menu getMenu(String name);



    MenuItem getMenuItem(long id);

    MenuItem createMenuItem(String label, long menuId, Long parentMenuItemId, int position,
                            MenuItem.Type targetType, Long targetId, String url);

    MenuItem updateMenuItemDetails(UpdateMenuItemDetailsRequest request);

    MenuItem updateMenuItemLocation(UpdateMenuItemLocationRequest location);

    void deleteMenuItem(long menuItemId, boolean deepDeleteChildItems);

}
