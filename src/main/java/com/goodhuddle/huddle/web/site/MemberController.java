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

import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.domain.Menu;
import com.goodhuddle.huddle.domain.SignUpResult;
import com.goodhuddle.huddle.service.MemberService;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.service.exception.EmailExistsException;
import com.goodhuddle.huddle.service.exception.NoSuchBlockException;
import com.goodhuddle.huddle.service.exception.PaymentFailedException;
import com.goodhuddle.huddle.service.exception.PaymentsNotSetupException;
import com.goodhuddle.huddle.service.request.member.ContactUsRequest;
import com.goodhuddle.huddle.service.request.member.JoinMailingListRequest;
import com.goodhuddle.huddle.service.request.member.SignUpMemberRequest;
import com.goodhuddle.huddle.web.builder.member.MemberDetailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    private final MenuService menuService;
    private final MemberDetailBuilder memberDetailBuilder;


    @Autowired
    public MemberController(MemberService memberService, MenuService menuService, MemberDetailBuilder memberDetailBuilder) {
        this.memberService = memberService;
        this.menuService = menuService;
        this.memberDetailBuilder = memberDetailBuilder;
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String showLoginPage(Model model) {
        Menu menu = menuService.getMainMenu();
        model.addAttribute("menu", menu);
        return "site/member/login";
    }

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public String showMembersPage(Model model) {
        Menu menu = menuService.getMainMenu();
        model.addAttribute("menu", menu);
        model.addAttribute("member", memberService.getLoggedInMember());
        return "site/member/home";
    }

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String showMemberProfilePage(Model model) {
        Menu menu = menuService.getMainMenu();
        model.addAttribute("menu", menu);
        model.addAttribute("member", memberService.getLoggedInMember());
        return "site/member/profile";
    }

    @RequestMapping(value = "join", method = RequestMethod.GET)
    public String showJoinMailingListPage(Model model) {
        Menu menu = menuService.getMainMenu();
        model.addAttribute("menu", menu);
        model.addAttribute("request", new JoinMailingListRequest());
        return "site/member/join-mailing-list";
    }

    @RequestMapping(value = "join", method = RequestMethod.POST)
    public String joinMailingList(@Valid @ModelAttribute("request") JoinMailingListRequest request, BindingResult result, Model model) {
        Menu menu = menuService.getMainMenu();
        model.addAttribute("menu", menu);

        try {

            if (result.hasErrors()) {
                return "site/member/join-mailing-list";
            }

            log.info("Signing up new member to mailing list: {}", request.getEmail());
            Member member = memberService.joinMailingList(request);
            model.addAttribute("member", member);
            return "site/member/join-mailing-list-success";

        } catch (EmailExistsException e) {
            result.addError(new FieldError("form", "email", "An account already exists for this email address"));
            return "site/member/join-mailing-list";
        }
    }


    @RequestMapping(value = "contact.do", method = RequestMethod.POST)
    @ResponseBody
    public ContactUsResponse processContactUsRequestApi(@Valid @RequestBody ContactUsRequest request)
            throws PaymentFailedException, PaymentsNotSetupException {

        log.info("Contact us request from '{}'", request.getEmail());
        memberService.processContactUsRequest(request);
        return new ContactUsResponse();
    }


    @RequestMapping(value = "signup.do", method = RequestMethod.POST)
    @ResponseBody
    public SignUpResponse signUpMember(@Valid @RequestBody SignUpMemberRequest request)
            throws NoSuchBlockException {

        log.debug("Signing up new member: {}", request.getEmail());
        SignUpResult result = memberService.signUpMember(request);
        return new SignUpResponse(result.getSuccessUrl());
    }


    //-------------------------------------------------------------------------

    public static final class ContactUsResponse {
        private boolean sent;

        public ContactUsResponse() {
            sent = true;
        }

        public boolean isSent() {
            return sent;
        }

        public void setSent(boolean sent) {
            this.sent = sent;
        }
    }

    //-------------------------------------------------------------------------

    public static final class SignUpResponse {

        private String successUrl;

        public SignUpResponse(String successUrl) {
            this.successUrl = successUrl;
        }

        public String getSuccessUrl() {
            return successUrl;
        }
    }
}
