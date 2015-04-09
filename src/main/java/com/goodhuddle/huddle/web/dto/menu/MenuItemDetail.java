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

package com.goodhuddle.huddle.web.dto.menu;

import com.goodhuddle.huddle.domain.MenuItem;

import java.util.List;

public class MenuItemDetail {

    private long id;
    private long menuId;
    private String label;
    private MenuItem.Type targetType;
    private Long targetId;
    private String url;
    private List<MenuItemDetail> items;

    // selected is used only when displaying a menu for a logged in user
    private boolean selected;

    public MenuItemDetail(long id, long menuId, String label, MenuItem.Type targetType,
                          Long targetId, String url,
                          List<MenuItemDetail> items) {
        this.id = id;
        this.menuId = menuId;
        this.label = label;
        this.targetType = targetType;
        this.targetId = targetId;
        this.url = url;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public long getMenuId() {
        return menuId;
    }

    public String getLabel() {
        return label;
    }

    public MenuItem.Type getTargetType() {
        return targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public String getUrl() {
        return url;
    }

    public List<MenuItemDetail> getItems() {
        return items;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
