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

import com.goodhuddle.huddle.domain.Email;
import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.service.request.mailout.SearchEmailsRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class EmailSpecification {

    public static Specification<Email> search(final Huddle huddle, final SearchEmailsRequest request) {
        return new Specification<Email>() {
            @Override
            public Predicate toPredicate(Root<Email> email, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add(builder.equal(email.get("huddle"), huddle));

                if (request.getMailoutId() != null) {
                    Join<Object, Object> mailout = email.join("mailout");
                    conjunction.getExpressions().add(
                            builder.equal(mailout.get("id"), request.getMailoutId()));
                }

                if (request.getRecipientId() != null) {
                    Join<Object, Object> recipient = email.join("mailout");
                    conjunction.getExpressions().add(
                            builder.equal(recipient.get("id"), request.getRecipientId()));
                }

                return conjunction;
            }
        };
    }
}
