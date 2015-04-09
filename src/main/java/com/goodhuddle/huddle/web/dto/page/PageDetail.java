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

package com.goodhuddle.huddle.web.dto.page;

import com.goodhuddle.huddle.domain.PageContent;

public class PageDetail {

    private long id;
    private String title;
    private String slug;
    private String url;
    private String layout;
    private Long menuItemId;
    private PageContent content;

    public PageDetail(long id, String title, String slug, String url, String layout, Long menuItemId, PageContent content) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.url = url;
        this.layout = layout;
        this.menuItemId = menuItemId;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getUrl() {
        return url;
    }

    public String getLayout() {
        return layout;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public PageContent getContent() {
        return content;
    }
}
