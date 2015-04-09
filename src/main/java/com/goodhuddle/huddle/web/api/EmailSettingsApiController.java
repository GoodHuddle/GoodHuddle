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

package com.goodhuddle.huddle.web.api;

import com.goodhuddle.huddle.service.MailoutService;
import com.goodhuddle.huddle.service.request.mailout.settings.UpdateEmailSettingsRequest;
import com.goodhuddle.huddle.web.builder.mailout.EmailSettingsDetailBuilder;
import com.goodhuddle.huddle.web.dto.mailout.EmailSettingsDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails/settings")
public class EmailSettingsApiController {

    private static final Logger log = LoggerFactory.getLogger(EmailSettingsApiController.class);

    private final MailoutService mailoutService;
    private final EmailSettingsDetailBuilder emailSettingsDetailBuilder;

    @Autowired
    public EmailSettingsApiController(MailoutService mailoutService,
                                      EmailSettingsDetailBuilder emailSettingsDetailBuilder) {
        this.mailoutService = mailoutService;
        this.emailSettingsDetailBuilder = emailSettingsDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public EmailSettingsDetail getEmailSettings() {
        return emailSettingsDetailBuilder.build(mailoutService.getEmailSettings());
    }

    @RequestMapping(method = RequestMethod.POST)
    public EmailSettingsDetail updateEmailSettings(@RequestBody UpdateEmailSettingsRequest request) {
        return emailSettingsDetailBuilder.build(mailoutService.updateEmailSettings(request));
    }
}
