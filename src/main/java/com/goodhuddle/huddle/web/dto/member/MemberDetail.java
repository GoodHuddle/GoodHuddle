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

import com.goodhuddle.huddle.web.dto.member.tag.TagRef;
import org.joda.time.DateTime;

import java.util.List;

public class MemberDetail extends MemberRef {

    private String firstName;
    private String lastName;
    private String postCode;
    private String phone;
    private DateTime lastLogin;
    private SecurityGroupRef securityGroup;
    private List<TagRef> tags;

    public MemberDetail(long id, String username, String displayName, String email,
                        String firstName, String lastName, String postCode, String phone, DateTime lastLogin,
                        SecurityGroupRef securityGroup, List<TagRef> tags) {
        super(id, username, displayName, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.postCode = postCode;
        this.phone = phone;
        this.lastLogin = lastLogin;
        this.securityGroup = securityGroup;
        this.tags = tags;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getPhone() {
        return phone;
    }

    public DateTime getLastLogin() {
        return lastLogin;
    }

    public SecurityGroupRef getSecurityGroup() {
        return securityGroup;
    }

    public List<TagRef> getTags() {
        return tags;
    }
}
