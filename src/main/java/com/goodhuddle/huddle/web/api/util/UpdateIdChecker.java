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

package com.goodhuddle.huddle.web.api.util;

import com.goodhuddle.huddle.domain.HasId;

import java.io.Serializable;

public class UpdateIdChecker {

    public static <IdType extends Serializable> void checkId(IdType id, HasId<IdType> target) {
        if (target.getId() != null && !target.getId().equals(id)) {
            throw new IllegalArgumentException("ID in body content ('" + target.getId()
                    + "') does not match ID in parameters ('" + id + "')");
        }
        target.setId(id);
    }
}
