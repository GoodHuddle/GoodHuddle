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

package com.goodhuddle.huddle.service.request.blog;

import com.goodhuddle.huddle.domain.HasId;

public class UpdateBlogPostRequest extends AbstractBlogPostDetailRequest implements HasId<Long> {

    private Long id;
    private long[] tagIds;
    private boolean commentsOpen;

    public UpdateBlogPostRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long[] getTagIds() {
        return tagIds;
    }

    public void setTagIds(long[] tagIds) {
        this.tagIds = tagIds;
    }

    public boolean isCommentsOpen() {
        return commentsOpen;
    }

    public void setCommentsOpen(boolean commentsOpen) {
        this.commentsOpen = commentsOpen;
    }
}
