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

import com.goodhuddle.huddle.web.dto.huddle.HuddleRef;
import org.joda.time.DateTime;

public class MemberRow extends MemberRef {

    private HuddleRef huddle;
    private DateTime lastLogin;
    private SecurityGroupRef securityGroup;

    public MemberRow(long id, String username, String displayName, String email,
                     HuddleRef huddle, DateTime lastLogin, SecurityGroupRef securityGroup) {
        super(id, username, displayName, email);
        this.huddle = huddle;
        this.lastLogin = lastLogin;
        this.securityGroup = securityGroup;
    }

    public HuddleRef getHuddle() {
        return huddle;
    }

    public DateTime getLastLogin() {
        return lastLogin;
    }

    public SecurityGroupRef getSecurityGroup() {
        return securityGroup;
    }
}
