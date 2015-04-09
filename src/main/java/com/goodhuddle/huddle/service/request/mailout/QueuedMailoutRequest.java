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

import java.io.Serializable;

public class QueuedMailoutRequest implements Serializable {

    private String huddleSlug;
    private long mailoutId;

    public QueuedMailoutRequest() {
    }

    public QueuedMailoutRequest(String huddleSlug, long mailoutId) {
        this.huddleSlug = huddleSlug;
        this.mailoutId = mailoutId;
    }

    public String getHuddleSlug() {
        return huddleSlug;
    }

    public void setHuddleSlug(String huddleSlug) {
        this.huddleSlug = huddleSlug;
    }

    public long getMailoutId() {
        return mailoutId;
    }

    public void setMailoutId(long mailoutId) {
        this.mailoutId = mailoutId;
    }
}
