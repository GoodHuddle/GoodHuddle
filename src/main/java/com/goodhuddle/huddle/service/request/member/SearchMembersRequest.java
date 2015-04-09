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

package com.goodhuddle.huddle.service.request.member;

import java.util.List;

public class SearchMembersRequest {

    private String keywords;
    private List<Long> securityGroupIds;
    private boolean includeNoAccess;
    private MemberTagFilter tags;

    private int page;
    private int size;

    public SearchMembersRequest() {
        page = 0;
        size = 20;
    }

    public SearchMembersRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<Long> getSecurityGroupIds() {
        return securityGroupIds;
    }

    public void setSecurityGroupIds(List<Long> securityGroupIds) {
        this.securityGroupIds = securityGroupIds;
    }

    public boolean isIncludeNoAccess() {
        return includeNoAccess;
    }

    public void setIncludeNoAccess(boolean includeNoAccess) {
        this.includeNoAccess = includeNoAccess;
    }

    public MemberTagFilter getTags() {
        return tags;
    }

    public void setTags(MemberTagFilter tags) {
        this.tags = tags;
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
