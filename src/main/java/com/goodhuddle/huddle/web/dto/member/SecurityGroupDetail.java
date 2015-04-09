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

package com.goodhuddle.huddle.web.dto.member;

import java.util.List;

public class SecurityGroupDetail extends SecurityGroupRef {

    private String description;
    private boolean accessAdmin;
    private List<String> permissions;

    public SecurityGroupDetail(long id, String name, String displayName, String description,
                               boolean accessAdmin, List<String> permissions) {
        super(id, name, displayName);
        this.description = description;
        this.accessAdmin = accessAdmin;
        this.permissions = permissions;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAccessAdmin() {
        return accessAdmin;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
