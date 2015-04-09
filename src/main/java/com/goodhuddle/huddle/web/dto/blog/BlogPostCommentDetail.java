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
import org.joda.time.DateTime;

public class BlogPostCommentDetail extends BlogPostCommentRef {

    private boolean approved;
    private DateTime createdOn;

    public BlogPostCommentDetail(long id, long blogPostId, MemberRef member, String displayName, String comment,
                                 boolean approved, DateTime createdOn) {
        super(id, blogPostId, member, displayName, comment);
        this.approved = approved;
        this.createdOn = createdOn;
    }

    public boolean isApproved() {
        return approved;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }
}
