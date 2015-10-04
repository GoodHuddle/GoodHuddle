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

package com.goodhuddle.huddle.repository;

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.PetitionSignature;
import com.goodhuddle.huddle.service.request.petition.SearchPetitionSignatureRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class PetitionSignatureSpecification {

    public static Specification<PetitionSignature> search(final Huddle huddle, final SearchPetitionSignatureRequest request) {
        return new Specification<PetitionSignature>() {
            @Override
            public Predicate toPredicate(Root<PetitionSignature> petitionSignature, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add(builder.equal(petitionSignature.get("huddle"), huddle));

                if (request.getPetitionId() != null) {
                    Join<Object, Object> petition = petitionSignature.join("petition");
                    conjunction.getExpressions().add(
                            builder.equal(petition.get("id"), request.getPetitionId()));
                }

                return conjunction;
            }
        };
    }
}
