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

import com.goodhuddle.huddle.web.dto.member.MemberRef;

public class BlogPostCommentRef {

    private long id;
    private long blogPostId;
    private MemberRef member;
    private String displayName;
    private String comment;

    public BlogPostCommentRef(long id, long blogPostId, MemberRef member, String displayName, String comment) {
        this.id = id;
        this.blogPostId = blogPostId;
        this.member = member;
        this.displayName = displayName;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public long getBlogPostId() {
        return blogPostId;
    }

    public MemberRef getMember() {
        return member;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getComment() {
        return comment;
    }
}
