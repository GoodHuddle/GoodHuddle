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

package com.goodhuddle.huddle.web.site;

import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.exception.HuddleExistsException;
import com.goodhuddle.huddle.service.exception.InvalidHuddleInvitationCodeException;
import com.goodhuddle.huddle.service.exception.InvalidHuddleSlugException;
import com.goodhuddle.huddle.service.request.huddle.CreateHuddleRequest;
import com.goodhuddle.huddle.web.builder.huddle.HuddleRefBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CreateHuddleController {

    private static final Logger log = LoggerFactory.getLogger(CreateHuddleController.class);

    private final HuddleService huddleService;
    private final HuddleRefBuilder huddleRefBuilder;

    @Autowired
    public CreateHuddleController(HuddleService huddleService, HuddleRefBuilder huddleRefBuilder) {
        this.huddleService = huddleService;
        this.huddleRefBuilder = huddleRefBuilder;
    }

    @RequestMapping(value = "/_huddles/create", method = RequestMethod.GET)
    public String showCreateHuddlePage(Model model) {
        model.addAttribute("form", new CreateHuddleRequest());
        return "huddles/create";
    }

    @RequestMapping(value = "/_huddles/create", method = RequestMethod.POST)
    public String createHuddle(@ModelAttribute CreateHuddleRequest form, BindingResult bindingResult)
            throws HuddleExistsException, InvalidHuddleInvitationCodeException, InvalidHuddleSlugException {
        log.debug("Creating huddle: {}", form.getBaseUrl());
        if (bindingResult.hasErrors()) {
            return "huddles/create";
        }
        return createHuddle(form);
    }

    @RequestMapping(value = "/error/huddle-not-found")
    public String showNoSuchHuddlePage() {
        return "huddles/no-such-huddle";
    }

    @RequestMapping(value = "/not-setup")
    public String showNotSetupPage() {
        return "error/not-setup";
    }

    protected String createHuddle(CreateHuddleRequest request)
            throws HuddleExistsException, InvalidHuddleInvitationCodeException, InvalidHuddleSlugException {
        huddleService.createHuddle(request);
        return "redirect:/admin";
    }
}
