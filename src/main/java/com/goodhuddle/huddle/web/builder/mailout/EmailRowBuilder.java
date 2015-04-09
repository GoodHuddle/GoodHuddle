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
import com.goodhuddle.huddle.web.dto.mailout.EmailRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailRowBuilder extends AbstractDTOBuilder<EmailRow, Email> {

    private final MemberRefBuilder memberRefBuilder;

    @Autowired
    public EmailRowBuilder(MemberRefBuilder memberRefBuilder) {
        this.memberRefBuilder = memberRefBuilder;
    }

    @Override
    protected EmailRow buildNullSafe(Email entity) {
        return new EmailRow(
                entity.getId(),
                memberRefBuilder.build(entity.getRecipient()),
                entity.getStatus(),
                entity.getSentOn(),
                entity.getSubject());
    }
}
