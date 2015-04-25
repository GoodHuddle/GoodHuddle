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

package com.goodhuddle.huddle.web.builder.member;

import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.builder.member.tag.TagRefBuilder;
import com.goodhuddle.huddle.web.dto.member.MemberDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailBuilder extends AbstractDTOBuilder<MemberDetail, Member> {

    private final SecurityGroupRefBuilder securityGroupRefBuilder;
    private final TagRefBuilder tagRefBuilder;

    @Autowired
    public MemberDetailBuilder(SecurityGroupRefBuilder securityGroupRefBuilder,
                               TagRefBuilder tagRefBuilder) {
        this.securityGroupRefBuilder = securityGroupRefBuilder;
        this.tagRefBuilder = tagRefBuilder;
    }

    @Override
    protected MemberDetail buildNullSafe(Member entity) {
        return new MemberDetail(
                entity.getId(),
                entity.getUsername(),
                entity.getDisplayName(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPostCode(),
                entity.getPhone(),
                entity.getLastLogin(),
                securityGroupRefBuilder.build(entity.getSecurityGroup()),
                tagRefBuilder.build(entity.getTags()));
    }
}
