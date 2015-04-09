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

import com.goodhuddle.huddle.domain.BlogPost;
import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.service.request.blog.SearchBlogPostRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class BlogPostSpecification {

    public static Specification<BlogPost> search(final Huddle huddle, final SearchBlogPostRequest request) {
        return new Specification<BlogPost>() {
            @Override
            public Predicate toPredicate(Root<BlogPost> blogPost, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add(builder.equal(blogPost.get("huddle"), huddle));

                if (StringUtils.isNotBlank(request.getPhrase())) {
                    String phrase = "%" + request.getPhrase().toLowerCase() + "%";
                    conjunction.getExpressions().add(
                            builder.like(
                                    builder.lower(blogPost.<String>get("title")), phrase));
                }

                if (CollectionUtils.isNotEmpty(request.getBlogIds())) {
                    Join<Object, Object> blog = blogPost.join("blog");
                    conjunction.getExpressions().add(
                            builder.in(blog.get("id")).value(request.getBlogIds()));
                }

                if (!request.isIncludeUnpublished()) {
                    conjunction.getExpressions().add(
                            builder.lessThan((Expression) blogPost.get("publishedOn"), new DateTime()));
                }

                return conjunction;
            }
        };
    }
}
