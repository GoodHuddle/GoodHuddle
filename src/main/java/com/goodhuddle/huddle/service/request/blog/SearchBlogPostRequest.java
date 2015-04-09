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

import java.util.Arrays;
import java.util.List;

public class SearchBlogPostRequest {

    private List<Long> blogIds;
    private String phrase;
    private boolean includeUnpublished;
    private int page;
    private int size;

    public SearchBlogPostRequest() {
        page = 0;
        size = 20;
    }

    public SearchBlogPostRequest(List<Long> blogIds, boolean includeUnpublished, int page, int size) {
        this.blogIds = blogIds;
        this.includeUnpublished = includeUnpublished;
        this.page = page;
        this.size = size;
    }

    public SearchBlogPostRequest(Long blogId, boolean includeUnpublished, int page, int size) {
        this.blogIds = Arrays.asList(blogId);
        this.includeUnpublished = includeUnpublished;
        this.page = page;
        this.size = size;
    }

    public boolean isIncludeUnpublished() {
        return includeUnpublished;
    }

    public void setIncludeUnpublished(boolean includeUnpublished) {
        this.includeUnpublished = includeUnpublished;
    }

    public List<Long> getBlogIds() {
        return blogIds;
    }

    public void setBlogIds(List<Long> blogIds) {
        this.blogIds = blogIds;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
