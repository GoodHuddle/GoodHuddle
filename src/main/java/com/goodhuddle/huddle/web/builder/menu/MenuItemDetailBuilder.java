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

package com.goodhuddle.huddle.web.builder.menu;

import com.goodhuddle.huddle.domain.MenuItem;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.dto.menu.MenuItemDetail;
import org.springframework.stereotype.Service;

@Service
public class MenuItemDetailBuilder extends AbstractDTOBuilder<MenuItemDetail, MenuItem> {

    @Override
    protected MenuItemDetail buildNullSafe(MenuItem entity) {
        return new MenuItemDetail(
                entity.getId(),
                entity.getMenu().getId(),
                entity.getLabel(),
                entity.getTargetType(),
                entity.getTargetId(),
                entity.getUrl(),
                build(entity.getItems())
        );
    }
}
