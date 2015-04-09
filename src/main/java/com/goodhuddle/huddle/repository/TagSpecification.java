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
import com.goodhuddle.huddle.domain.Tag;
import com.goodhuddle.huddle.service.request.tag.SearchTagsRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TagSpecification {

    public static Specification<Tag> search(final Huddle huddle, final SearchTagsRequest request) {
        return new Specification<Tag>() {
            @Override
            public Predicate toPredicate(Root<Tag> tag, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add(builder.equal(tag.get("huddle"), huddle));

                if (StringUtils.isNotBlank(request.getName())) {
                    String nameTerm = "%" + request.getName().toLowerCase() + "%";
                    conjunction.getExpressions().add(
                            builder.like(builder.lower(tag.<String>get("name")), nameTerm));
                }

                return conjunction;
            }
        };
    }
}
