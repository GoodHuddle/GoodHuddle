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
import com.goodhuddle.huddle.domain.Subscription;
import com.goodhuddle.huddle.service.request.payment.SearchSubscriptionsRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class SubscriptionSpecification {

    public static Specification<Subscription> search(final Huddle huddle, final SearchSubscriptionsRequest request) {
        return new Specification<Subscription>() {
            @Override
            public Predicate toPredicate(Root<Subscription> payment, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add(builder.equal(payment.get("huddle"), huddle));

                if (request.getMemberId() != null) {
                    Join<Object, Object> member = payment.join("member");
                    conjunction.getExpressions().add(
                            builder.equal(member.get("id"), request.getMemberId()));
                }

                return conjunction;
            }
        };
    }
}
