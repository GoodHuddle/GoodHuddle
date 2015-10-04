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
    private boolean updateMailChimpApiKey;
    private String mailChimpApiKey;
    private boolean updateMandrillApiKey;
    private String mandrillApiKey;

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

    public boolean isUpdateMailChimpApiKey() {
        return updateMailChimpApiKey;
    }

    public void setUpdateMailChimpApiKey(boolean updateMailChimpApiKey) {
        this.updateMailChimpApiKey = updateMailChimpApiKey;
    }

    public boolean isUpdateMandrillApiKey() {
        return updateMandrillApiKey;
    }

    public void setUpdateMandrillApiKey(boolean updateMandrillApiKey) {
        this.updateMandrillApiKey = updateMandrillApiKey;
    }

    public String getMailChimpApiKey() {
        return mailChimpApiKey;
    }

    public void setMailChimpApiKey(String mailChimpApiKey) {
        this.mailChimpApiKey = mailChimpApiKey;
    }

    public String getMandrillApiKey() {
        return mandrillApiKey;
    }

    public void setMandrillApiKey(String mandrillApiKey) {
        this.mandrillApiKey = mandrillApiKey;
    }
}

