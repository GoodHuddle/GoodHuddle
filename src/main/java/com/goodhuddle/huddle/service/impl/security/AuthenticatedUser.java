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

package com.goodhuddle.huddle.service.impl.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthenticatedUser extends User {

    private String displayName;
    private long memberId;

    public AuthenticatedUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             String displayName, long memberId) {
        super(username, password, authorities);
        this.displayName = displayName;
        this.memberId = memberId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getMemberId() {
        return memberId;
    }
}
