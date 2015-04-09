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

package com.goodhuddle.huddle.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "security_group")
@SequenceGenerator(name = "sequence_generator", sequenceName = "security_group_id_seq")
public class SecurityGroup extends AbstractHuddleObject<Long> {

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "display_name")
    @NotBlank
    private String displayName;

    @Column(name = "description")
    private String description;

    @Column(name = "admin_access")
    private boolean accessAdmin;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="security_group_permission", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "permission")
    private List<String> permissions;

    public SecurityGroup() {
    }

    public SecurityGroup(Huddle huddle, String name, String displayName, String description, boolean accessAdmin, List<String> permissions) {
        super(huddle);
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.accessAdmin = accessAdmin;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAccessAdmin() {
        return accessAdmin;
    }

    public void setAccessAdmin(boolean accessAdmin) {
        this.accessAdmin = accessAdmin;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
