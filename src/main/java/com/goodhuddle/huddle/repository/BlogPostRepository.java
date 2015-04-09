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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long>, JpaSpecificationExecutor {

    BlogPost findByHuddleAndId(Huddle huddle, long id);

    BlogPost findByHuddleAndBlogSlugAndSlug(Huddle huddle, String blogSlug, String blogPostSlug);

    BlogPost findByHuddleAndBlogIdAndSlug(Huddle huddle, Long blogId, String slug);

}
