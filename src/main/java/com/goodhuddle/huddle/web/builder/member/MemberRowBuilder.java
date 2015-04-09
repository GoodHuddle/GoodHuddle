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
import com.goodhuddle.huddle.web.builder.huddle.HuddleRefBuilder;
import com.goodhuddle.huddle.web.dto.member.MemberRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberRowBuilder extends AbstractDTOBuilder<MemberRow, Member> {

    private final HuddleRefBuilder huddleRefBuilder;
    private final SecurityGroupRefBuilder securityGroupRefBuilder;

    @Autowired
    public MemberRowBuilder(HuddleRefBuilder huddleRefBuilder,
                            SecurityGroupRefBuilder securityGroupRefBuilder) {
        this.huddleRefBuilder = huddleRefBuilder;
        this.securityGroupRefBuilder = securityGroupRefBuilder;
    }

    @Override
    protected MemberRow buildNullSafe(Member entity) {
        return new MemberRow(
                entity.getId(),
                entity.getUsername(),
                entity.getDisplayName(),
                entity.getEmail(),
                huddleRefBuilder.build(entity.getHuddle()),
                entity.getLastLogin(),
                securityGroupRefBuilder.build(entity.getSecurityGroup()));
    }
}
