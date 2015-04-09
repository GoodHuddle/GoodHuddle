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

import com.goodhuddle.huddle.domain.BlogPost;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.builder.member.MemberRefBuilder;
import com.goodhuddle.huddle.web.builder.member.tag.TagRefBuilder;
import com.goodhuddle.huddle.web.dto.blog.BlogPostDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogPostDetailBuilder extends AbstractDTOBuilder<BlogPostDetail, BlogPost> {

    private final MemberRefBuilder memberRefBuilder;
    private final TagRefBuilder tagRefBuilder;

    @Autowired
    public BlogPostDetailBuilder(MemberRefBuilder memberRefBuilder, TagRefBuilder tagRefBuilder) {
        this.memberRefBuilder = memberRefBuilder;
        this.tagRefBuilder = tagRefBuilder;
    }

    @Override
    protected BlogPostDetail buildNullSafe(BlogPost entity) {
        return new BlogPostDetail(
                entity.getId(),
                entity.getBlog().getId(),
                entity.getTitle(),
                entity.getSlug(),
                entity.getUrl(),
                entity.getBlurb(),
                entity.getFeatureImageUrl(),
                memberRefBuilder.build(entity.getAuthor()),
                entity.isPublished(),
                entity.getPublishedOn(),
                entity.getLayout(),
                tagRefBuilder.build(entity.getTags()),
                entity.isCommentsOpen(),
                entity.getContent()
        );
    }
}
