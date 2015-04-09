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

import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.PaymentService;
import com.goodhuddle.huddle.service.exception.BlogExistsException;
import com.goodhuddle.huddle.service.exception.BlogPostExistsException;
import com.goodhuddle.huddle.service.exception.HuddleExistsException;
import com.goodhuddle.huddle.service.exception.PageSlugExistsException;
import com.goodhuddle.huddle.service.request.huddle.SetupDefaultPagesRequest;
import com.goodhuddle.huddle.service.request.huddle.UpdateHuddleRequest;
import com.goodhuddle.huddle.web.builder.huddle.HuddleDetailBuilder;
import com.goodhuddle.huddle.web.builder.huddle.HuddleRowBuilder;
import com.goodhuddle.huddle.web.dto.huddle.HuddleDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/huddle")
public class HuddleApiController {

    private static final Logger log = LoggerFactory.getLogger(HuddleApiController.class);

    private final HuddleService huddleService;
    private final HuddleRowBuilder huddleRowBuilder;
    private final HuddleDetailBuilder huddleDetailBuilder;
    private final PaymentService paymentService;

    @Autowired
    public HuddleApiController(HuddleService huddleService,
                               HuddleRowBuilder huddleRowBuilder,
                               HuddleDetailBuilder huddleDetailBuilder,
                               PaymentService paymentService) {
        this.huddleService = huddleService;
        this.huddleRowBuilder = huddleRowBuilder;
        this.huddleDetailBuilder = huddleDetailBuilder;
        this.paymentService = paymentService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public HuddleDetail getHuddle() {
        log.debug("Retrieving huddle details");
        return huddleDetailBuilder.build(huddleService.getHuddle());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public HuddleDetail updateHuddle(@Valid @RequestBody UpdateHuddleRequest request) {
        log.debug("Updating Huddle details: {}", request.getName());
        return huddleDetailBuilder.build(huddleService.updateHuddle(request));
    }

    @RequestMapping(value = "/setup/pages", method = RequestMethod.PUT)
    public void setupDefaultPages(@RequestBody SetupDefaultPagesRequest request)
            throws PageSlugExistsException, HuddleExistsException,
                   IOException, BlogPostExistsException, BlogExistsException {

        log.debug("Setting up default pages for Huddle");
        huddleService.setupDefaultPages(request);
    }

    @RequestMapping(value = "/setup/complete", method = RequestMethod.PUT)
    public HuddleDetail setupWizardComplete() {
        log.debug("Setup wizard complete for Huddle");
        return huddleDetailBuilder.build(huddleService.setupWizardComplete(true));
    }
}
