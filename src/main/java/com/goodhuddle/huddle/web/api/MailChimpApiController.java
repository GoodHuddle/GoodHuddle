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

import com.goodhuddle.huddle.domain.MailChimpList;
import com.goodhuddle.huddle.service.MailoutService;
import com.goodhuddle.huddle.service.exception.MailChimpErrorException;
import com.goodhuddle.huddle.service.request.member.MailChimpSyncMemberRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mailchimp")
public class MailChimpApiController {

    private static final Logger log = LoggerFactory.getLogger(MailChimpApiController.class);

    private final MailoutService mailoutService;

    @Autowired
    public MailChimpApiController(MailoutService mailoutService) {
        this.mailoutService = mailoutService;
    }

    @RequestMapping(value = "lists", method = RequestMethod.GET)
    public List<MailChimpList> getMailChimpLists() throws MailChimpErrorException {
        log.debug("Finding MailChimp lists");
        return mailoutService.getMailChimpLists();
    }

    @RequestMapping(value = "sync", method = RequestMethod.POST)
    public void syncMembers(@RequestBody MailChimpSyncMemberRequest request) throws MailChimpErrorException {
        log.debug("Synchronizing member list with MailChimp: " + request);
        mailoutService.mailChimpSyncMembers(request);
    }

}
