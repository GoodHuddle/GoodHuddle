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

import com.goodhuddle.huddle.domain.AllowCommentsType;
import com.goodhuddle.huddle.domain.RequireCommentApprovalType;

public class BlogDetail extends BlogRef {

    private String layout;
    private String defaultPostLayout;
    private Long menuItemId;
    private AllowCommentsType allowComments;
    private RequireCommentApprovalType requireCommentApproval;

    public BlogDetail(long id, String title, String slug, String url,
                      String layout, String defaultPostLayout, Long menuItemId,
                      AllowCommentsType allowComments,
                      RequireCommentApprovalType requireCommentApproval) {
        super(id, title, slug, url);
        this.layout = layout;
        this.defaultPostLayout = defaultPostLayout;
        this.menuItemId = menuItemId;
        this.allowComments = allowComments;
        this.requireCommentApproval = requireCommentApproval;
    }

    public String getLayout() {
        return layout;
    }

    public String getDefaultPostLayout() {
        return defaultPostLayout;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public AllowCommentsType getAllowComments() {
        return allowComments;
    }

    public RequireCommentApprovalType getRequireCommentApproval() {
        return requireCommentApproval;
    }
}
