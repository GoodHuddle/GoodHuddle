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

package com.goodhuddle.huddle.web.dto.mailout;

import com.goodhuddle.huddle.domain.Mailout;

public class MailoutRow extends MailoutRef {

    private String description;

    public MailoutRow(long id, String name, Mailout.Status status, String description) {
        super(id, name, status);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
