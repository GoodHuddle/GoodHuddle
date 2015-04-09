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

import com.goodhuddle.huddle.domain.BlogPostComment;
import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.service.request.blog.SearchBlogPostCommentRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class BlogPostCommentSpecification {

    public static Specification<BlogPostComment> search(final Huddle huddle, final SearchBlogPostCommentRequest request) {
        return new Specification<BlogPostComment>() {
            @Override
            public Predicate toPredicate(Root<BlogPostComment> blogPostComment, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate conjunction = builder.conjunction();
                conjunction.getExpressions().add(builder.equal(blogPostComment.get("huddle"), huddle));

                Join<Object, Object> blogPost = blogPostComment.join("blogPost");
                conjunction.getExpressions().add(builder.equal(blogPost.get("id"), request.getBlogPostId()));

                return conjunction;
            }
        };
    }
}
