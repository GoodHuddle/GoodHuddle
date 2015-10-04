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

package com.goodhuddle.huddle.web.dto.petition;

import com.goodhuddle.huddle.web.dto.member.MemberRef;
import org.joda.time.DateTime;

public class PetitionDetail extends PetitionRef {

    private String description;
    private String subject;
    private String content;
    private String thankyouEmailTemplate;
    private String adminEmailTemplate;
    private String adminEmailAddresses;
    private DateTime createdOn;
    private MemberRef createdBy;

    public PetitionDetail(long id, String name, String description,
                          String subject, String content,
                          String thankyouEmailTemplate,
                          String adminEmailTemplate,
                          String adminEmailAddresses,
                          DateTime createdOn, MemberRef createdBy) {
        super(id, name);
        this.description = description;
        this.subject = subject;
        this.content = content;
        this.thankyouEmailTemplate = thankyouEmailTemplate;
        this.adminEmailTemplate = adminEmailTemplate;
        this.adminEmailAddresses = adminEmailAddresses;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public String getSubject() {
        return subject;
    }

    public String getThankyouEmailTemplate() {
        return thankyouEmailTemplate;
    }

    public String getAdminEmailTemplate() {
        return adminEmailTemplate;
    }

    public String getAdminEmailAddresses() {
        return adminEmailAddresses;
    }

    public String getContent() {
        return content;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public MemberRef getCreatedBy() {
        return createdBy;
    }

}
