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

import com.goodhuddle.huddle.domain.Mailout;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.builder.member.MemberRefBuilder;
import com.goodhuddle.huddle.web.dto.mailout.MailoutDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailoutDetailBuilder extends AbstractDTOBuilder<MailoutDetail, Mailout> {

    private final MemberRefBuilder memberRefBuilder;

    @Autowired
    public MailoutDetailBuilder(MemberRefBuilder memberRefBuilder) {
        this.memberRefBuilder = memberRefBuilder;
    }

    @Override
    protected MailoutDetail buildNullSafe(Mailout entity) {
        return new MailoutDetail(
                entity.getId(),
                entity.getName(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getSubject(),
                entity.getContent(),
                entity.getCreatedOn(),
                memberRefBuilder.build(entity.getCreatedBy()),
                entity.getGeneratedOn(),
                memberRefBuilder.build(entity.getGeneratedBy()),
                entity.getSentOn(),
                memberRefBuilder.build(entity.getSentBy()));
    }
}
