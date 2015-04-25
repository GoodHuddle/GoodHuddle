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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@SequenceGenerator(name = "sequence_generator", sequenceName = "member_id_seq")
public class Member extends AbstractHuddleObject<Long> implements Taggable {

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "post_code")
    private String postCode;

    @ManyToOne
    @JoinColumn(name="security_group_id")
    private SecurityGroup securityGroup;

    @JsonIgnore
    @Column(name = "password_enc")
    private String encodedPassword;

    @JsonIgnore
    @Column(name = "password_reset_code")
    private String passwordResetCode;

    @Column(name = "password_reset_expiry")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime passwordResetExpiry;

    @Column(name = "huddle_owner")
    private boolean huddleOwner;

    @Column(name = "last_login")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime lastLogin;

    @Column(name = "login_attempts")
    private int loginAttempts;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "member_tag",
            joinColumns = {@JoinColumn(name = "member_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private List<Tag> tags;

    public Member() {
    }

    public Member(Huddle huddle, String username, String email, String postCode,
                  SecurityGroup securityGroup, String encodedPassword) {
        super(huddle);
        this.username = username;
        this.email = email;
        this.postCode = postCode;
        this.securityGroup = securityGroup;
        this.encodedPassword = encodedPassword;
    }

    public Member(Huddle huddle, String username, String firstName, String lastName, String email, String postCode,
                  SecurityGroup securityGroup, String encodedPassword) {
        super(huddle);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.postCode = postCode;
        this.securityGroup = securityGroup;
        this.encodedPassword = encodedPassword;
    }

    public void update(String firstName, String lastName, String email, String postCode, SecurityGroup securityGroup) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.postCode = postCode;
        this.securityGroup = securityGroup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public SecurityGroup getSecurityGroup() {
        return securityGroup;
    }

    public void setSecurityGroup(SecurityGroup securityGroup) {
        this.securityGroup = securityGroup;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public DateTime getPasswordResetExpiry() {
        return passwordResetExpiry;
    }

    public void setPasswordResetExpiry(DateTime passwordResetExpiry) {
        this.passwordResetExpiry = passwordResetExpiry;
    }

    public boolean isHuddleOwner() {
        return huddleOwner;
    }

    public void setHuddleOwner(boolean huddleOwner) {
        this.huddleOwner = huddleOwner;
    }

    public DateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(DateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }

        for (Tag nextTag : tags) {
            if (nextTag.getId().equals(tag.getId())) {
                // tag already exists
                return;
            }
        }

        this.tags.add(tag);
    }

    public String getDisplayName() {

        if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
            return firstName + " " + lastName;
        } else if (StringUtils.isNotBlank(firstName)) {
            return firstName;
        } else if (StringUtils.isNotBlank(lastName)) {
            return lastName;
        } else if (StringUtils.isNotBlank(username)) {
            return username;
        } else {
            return email;
        }
    }
}
