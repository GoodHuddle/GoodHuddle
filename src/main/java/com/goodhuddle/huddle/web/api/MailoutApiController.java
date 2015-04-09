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
import com.goodhuddle.huddle.service.exception.EmailExistsException;
import com.goodhuddle.huddle.service.exception.EmailsAlreadySentException;
import com.goodhuddle.huddle.service.exception.EmailsNotGeneratedException;
import com.goodhuddle.huddle.service.exception.UsernameExistsException;
import com.goodhuddle.huddle.service.request.mailout.CreateMailoutRequest;
import com.goodhuddle.huddle.service.request.mailout.SearchMailoutsRequest;
import com.goodhuddle.huddle.web.builder.mailout.MailoutDetailBuilder;
import com.goodhuddle.huddle.web.builder.mailout.MailoutRowBuilder;
import com.goodhuddle.huddle.web.dto.mailout.MailoutDetail;
import com.goodhuddle.huddle.web.dto.mailout.MailoutRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/mailout")
public class MailoutApiController {

    private static final Logger log = LoggerFactory.getLogger(MailoutApiController.class);

    private final MailoutService mailoutService;
    private final MailoutRowBuilder mailoutRowBuilder;
    private final MailoutDetailBuilder mailoutDetailBuilder;

    @Autowired
    public MailoutApiController(MailoutService mailoutService,
                                MailoutRowBuilder mailoutRowBuilder,
                                MailoutDetailBuilder mailoutDetailBuilder) {
        this.mailoutService = mailoutService;
        this.mailoutRowBuilder = mailoutRowBuilder;
        this.mailoutDetailBuilder = mailoutDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<MailoutRow> searchMailouts(SearchMailoutsRequest request) {
        log.debug("Searching mailouts: " + request);
        return mailoutRowBuilder.build(mailoutService.searchMailouts(request));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public MailoutDetail getMailout(@PathVariable long id) {
        return mailoutDetailBuilder.build(mailoutService.getMailout(id));
    }

    @RequestMapping(value = "{id}/emails/generate", method = RequestMethod.POST)
    public MailoutDetail generateEmails(@PathVariable long id) {
        return mailoutDetailBuilder.build(mailoutService.generateEmails(id));
    }

    @RequestMapping(value = "{id}/emails/send", method = RequestMethod.POST)
    public MailoutDetail sendEmails(@PathVariable long id)
            throws EmailsNotGeneratedException, EmailsAlreadySentException {
        return mailoutDetailBuilder.build(mailoutService.sendEmails(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public MailoutDetail createMailout(@Valid @RequestBody CreateMailoutRequest request)
            throws UsernameExistsException, EmailExistsException {

        log.info("Creating mailout '{}'", request.getName());
        return mailoutDetailBuilder.build(mailoutService.createMailout(request));
    }
}
