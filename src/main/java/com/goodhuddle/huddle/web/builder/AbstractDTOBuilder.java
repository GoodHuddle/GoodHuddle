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

package com.goodhuddle.huddle.web.builder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractDTOBuilder<DTOType, EntityType> {

    public Page<DTOType> build(Page<? extends EntityType> page) {
        if (page != null) {
            List<DTOType> result = new ArrayList<>();
            for (EntityType entity : page) {
                result.add(build(entity));
            }
            return new PageImpl<>(result,
                    new PageRequest(page.getNumber(), page.getSize(), page.getSort()),
                    page.getTotalElements());
        } else {
            return null;
        }
    }

    public List<DTOType> build(Collection<? extends EntityType> entities) {
        if (entities != null) {
            List<DTOType> result = new ArrayList<>();
            for (EntityType entity : entities) {
                result.add(build(entity));
            }
            return result;
        } else {
            return null;
        }
    }

    public DTOType build(EntityType entity) {
        if (entity != null) {
            return buildNullSafe(entity);
        } else {
            return null;
        }
    }

    protected abstract DTOType buildNullSafe(EntityType entity);
}
