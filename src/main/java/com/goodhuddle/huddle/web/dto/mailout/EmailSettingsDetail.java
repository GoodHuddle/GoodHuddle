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

public class EmailSettingsDetail {

    private String sendFromAddress;
    private String sendFromName;

    public EmailSettingsDetail(String sendFromAddress, String sendFromName) {
        this.sendFromAddress = sendFromAddress;
        this.sendFromName = sendFromName;
    }

    public String getSendFromAddress() {
        return sendFromAddress;
    }

    public String getSendFromName() {
        return sendFromName;
    }
}
