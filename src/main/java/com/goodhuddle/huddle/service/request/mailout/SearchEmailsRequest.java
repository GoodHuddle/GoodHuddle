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

package com.goodhuddle.huddle.service.request.mailout;

public class SearchEmailsRequest {

    private Long mailoutId;
    private Long recipientId;
    private int page;
    private int size;

    public SearchEmailsRequest() {
        page = 0;
        size = 20;
    }

    public SearchEmailsRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public Long getMailoutId() {
        return mailoutId;
    }

    public void setMailoutId(Long mailoutId) {
        this.mailoutId = mailoutId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
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

    @Override
    public String toString() {
        return "SearchEmailsRequest[mailoutId=" + mailoutId + ",page=" + page + ",size=" + size + "]";
    }
}
