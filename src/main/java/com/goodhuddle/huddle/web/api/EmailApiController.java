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
import com.goodhuddle.huddle.service.request.mailout.SearchEmailsRequest;
import com.goodhuddle.huddle.web.builder.mailout.EmailDetailBuilder;
import com.goodhuddle.huddle.web.builder.mailout.EmailRowBuilder;
import com.goodhuddle.huddle.web.dto.mailout.EmailDetail;
import com.goodhuddle.huddle.web.dto.mailout.EmailRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailApiController {

    private static final Logger log = LoggerFactory.getLogger(EmailApiController.class);

    private final MailoutService mailoutService;
    private final EmailDetailBuilder emailDetailBuilder;
    private final EmailRowBuilder emailRowBuilder;

    @Autowired
    public EmailApiController(MailoutService mailoutService,
                              EmailDetailBuilder emailDetailBuilder,
                              EmailRowBuilder emailRowBuilder) {

        this.mailoutService = mailoutService;
        this.emailDetailBuilder = emailDetailBuilder;
        this.emailRowBuilder = emailRowBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<EmailRow> searchEmails(SearchEmailsRequest request) {
        log.debug("Searching emails: " + request);
        return emailRowBuilder.build(mailoutService.searchEmails(request));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public EmailDetail getEmail(@PathVariable long id) {
        return emailDetailBuilder.build(mailoutService.getEmail(id));
    }
}
