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

package com.goodhuddle.huddle.web.builder.blog;

import com.goodhuddle.huddle.domain.Blog;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.dto.blog.BlogRef;
import org.springframework.stereotype.Service;

@Service
public class BlogRefBuilder extends AbstractDTOBuilder<BlogRef, Blog> {

    @Override
    protected BlogRef buildNullSafe(Blog entity) {
        return new BlogRef(
                entity.getId(),
                entity.getTitle(),
                entity.getSlug(),
                entity.getUrl()
        );
    }
}
