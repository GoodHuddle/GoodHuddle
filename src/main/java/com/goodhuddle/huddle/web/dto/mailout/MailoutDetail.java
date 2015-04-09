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

package com.goodhuddle.huddle.web.dto.mailout;

import com.goodhuddle.huddle.domain.Mailout;
import com.goodhuddle.huddle.web.dto.member.MemberRef;
import org.joda.time.DateTime;

public class MailoutDetail extends MailoutRef {

    private String description;
    private String subject;
    private String content;
    private DateTime createdOn;
    private MemberRef createdBy;
    private DateTime generatedOn;
    private MemberRef generatedBy;
    private DateTime sentOn;
    private MemberRef sentBy;

    public MailoutDetail(long id, String name, Mailout.Status status, String description,
                         String subject, String content,
                         DateTime createdOn, MemberRef createdBy,
                         DateTime generatedOn, MemberRef generatedBy,
                         DateTime sentOn, MemberRef sentBy) {
        super(id, name, status);
        this.description = description;
        this.subject = subject;
        this.content = content;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.generatedOn = generatedOn;
        this.generatedBy = generatedBy;
        this.sentOn = sentOn;
        this.sentBy = sentBy;
    }

    public String getDescription() {
        return description;
    }

    public String getSubject() {
        return subject;
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

    public DateTime getGeneratedOn() {
        return generatedOn;
    }

    public MemberRef getGeneratedBy() {
        return generatedBy;
    }

    public DateTime getSentOn() {
        return sentOn;
    }

    public MemberRef getSentBy() {
        return sentBy;
    }
}
