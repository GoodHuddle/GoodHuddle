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

package com.goodhuddle.huddle.web.builder.mailout;

import com.goodhuddle.huddle.domain.Email;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.builder.member.MemberRefBuilder;
import com.goodhuddle.huddle.web.dto.mailout.EmailDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailDetailBuilder extends AbstractDTOBuilder<EmailDetail, Email> {

    private final MailoutRefBuilder mailoutRefBuilder;
    private final MemberRefBuilder memberRefBuilder;

    @Autowired
    public EmailDetailBuilder(MailoutRefBuilder mailoutRefBuilder,
                              MemberRefBuilder memberRefBuilder) {
        this.mailoutRefBuilder = mailoutRefBuilder;
        this.memberRefBuilder = memberRefBuilder;
    }

    @Override
    protected EmailDetail buildNullSafe(Email entity) {
        return new EmailDetail(
                entity.getId(),
                mailoutRefBuilder.build(entity.getMailout()),
                memberRefBuilder.build(entity.getRecipient()),
                entity.getStatus(),
                entity.getCreatedOn(),
                memberRefBuilder.build(entity.getCreatedBy()),
                entity.getSubject(),
                entity.getContent());
    }
}
