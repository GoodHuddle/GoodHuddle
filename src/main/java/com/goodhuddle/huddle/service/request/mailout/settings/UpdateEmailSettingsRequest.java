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

package com.goodhuddle.huddle.service.request.mailout.settings;

public class UpdateEmailSettingsRequest {

    private String sendFromAddress;
    private String sendFromName;
    private String mailChimpApiKey;

    public String getSendFromAddress() {
        return sendFromAddress;
    }

    public void setSendFromAddress(String sendFromAddress) {
        this.sendFromAddress = sendFromAddress;
    }

    public String getSendFromName() {
        return sendFromName;
    }

    public void setSendFromName(String sendFromName) {
        this.sendFromName = sendFromName;
    }


    public String getMailChimpApiKey() {
        return mailChimpApiKey;
    }

    public void setMailChimpApiKey(String mailChimpApiKey) {
        this.mailChimpApiKey = mailChimpApiKey;
    }
}

