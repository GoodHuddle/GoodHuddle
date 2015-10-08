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

package com.goodhuddle.huddle.web.builder.petition;

import com.goodhuddle.huddle.domain.Petition;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.builder.member.MemberRefBuilder;
import com.goodhuddle.huddle.web.dto.petition.PetitionDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetitionDetailBuilder extends AbstractDTOBuilder<PetitionDetail, Petition> {

    private final MemberRefBuilder memberRefBuilder;
    private final PetitionTargetRefBuilder petitionTargetRefBuilder;

    @Autowired
    public PetitionDetailBuilder(MemberRefBuilder memberRefBuilder, PetitionTargetRefBuilder petitionTargetRefBuilder) {
        this.memberRefBuilder = memberRefBuilder;
        this.petitionTargetRefBuilder = petitionTargetRefBuilder;
    }

    @Override
    protected PetitionDetail buildNullSafe(Petition entity) {
        return new PetitionDetail(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getSubject(),
                entity.getContent(),
                entity.getPetitionEmailTemplate(),
                entity.getThankyouEmailTemplate(),
                entity.getAdminEmailTemplate(),
                entity.getAdminEmailAddresses(),
                entity.getCreatedOn(),
                memberRefBuilder.build(entity.getCreatedBy()),
                petitionTargetRefBuilder.build(entity.getTargets()));
    }
}
