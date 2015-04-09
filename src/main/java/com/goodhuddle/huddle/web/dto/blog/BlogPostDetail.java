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

package com.goodhuddle.huddle.web.dto.blog;

import com.goodhuddle.huddle.domain.PageContent;
import com.goodhuddle.huddle.web.dto.member.MemberRef;
import com.goodhuddle.huddle.web.dto.member.tag.TagRef;
import org.joda.time.DateTime;

import java.util.List;

public class BlogPostDetail extends BlogPostRef {

    private String layout;
    private List<TagRef> tags;
    private boolean commentsOpen;
    private PageContent content;

    public BlogPostDetail(long id, long blogId, String title, String slug, String url,
                          String blurb, String featureImageUrl,
                          MemberRef author, boolean published, DateTime publishedOn, String layout,
                          List<TagRef> tags, boolean commentsOpen, PageContent content) {
        super(id, blogId, title, slug, url, blurb, featureImageUrl, author, published, publishedOn);
        this.layout = layout;
        this.tags = tags;
        this.commentsOpen = commentsOpen;
        this.content = content;
    }

    public String getLayout() {
        return layout;
    }

    public List<TagRef> getTags() {
        return tags;
    }

    public boolean isCommentsOpen() {
        return commentsOpen;
    }

    public PageContent getContent() {
        return content;
    }
}
