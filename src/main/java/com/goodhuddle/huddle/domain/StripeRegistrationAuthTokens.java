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

package com.goodhuddle.huddle.domain;

public class StripeRegistrationAuthTokens {

    private String appCode;
    private String huddleSlug;
    private String registrationToken;

    public StripeRegistrationAuthTokens(String appCode, String huddleSlug, String registrationToken) {
        this.appCode = appCode;
        this.huddleSlug = huddleSlug;
        this.registrationToken = registrationToken;
    }

    public String getAppCode() {
        return appCode;
    }

    public String getHuddleSlug() {
        return huddleSlug;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public String getStripeStateVariable() {
        return huddleSlug + ":" + registrationToken;
    }
}
