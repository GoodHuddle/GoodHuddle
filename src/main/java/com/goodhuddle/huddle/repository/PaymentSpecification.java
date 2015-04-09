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
import com.goodhuddle.huddle.domain.Payment;
import com.goodhuddle.huddle.service.request.payment.SearchPaymentsRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class PaymentSpecification {

    public static Specification<Payment> search(final Huddle huddle, final SearchPaymentsRequest request) {
        return new Specification<Payment>() {
            @Override
            public Predicate toPredicate(Root<Payment> payment, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add(builder.equal(payment.get("huddle"), huddle));

                if (request.getPaidById() != null) {
                    Join<Object, Object> paidBy = payment.join("paidBy");
                    conjunction.getExpressions().add(
                            builder.equal(paidBy.get("id"), request.getPaidById()));
                }

                if (request.getSubscriptionId() != null) {
                    Join<Object, Object> subscription = payment.join("subscription");
                    conjunction.getExpressions().add(
                            builder.equal(subscription.get("id"), request.getSubscriptionId()));
                }

                return conjunction;
            }
        };
    }
}
