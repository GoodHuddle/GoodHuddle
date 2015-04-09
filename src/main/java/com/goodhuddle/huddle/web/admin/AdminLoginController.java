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

package com.goodhuddle.huddle.web.admin;

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MemberService;
import com.goodhuddle.huddle.service.exception.EmailsNotSetupException;
import com.goodhuddle.huddle.service.exception.NoSuchMemberException;
import com.goodhuddle.huddle.service.exception.ResetPasswordFailedException;
import com.goodhuddle.huddle.service.request.member.ForgotPasswordRequest;
import com.goodhuddle.huddle.service.request.member.ResetPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/admin")
public class AdminLoginController {

    private static final Logger log = LoggerFactory.getLogger(AdminLoginController.class);

    private final HuddleService huddleService;
    private final MemberService memberService;

    @Autowired
    public AdminLoginController(HuddleService huddleService, MemberService memberService) {
        this.huddleService = huddleService;
        this.memberService = memberService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showAdminPage(Model model) {
        return "redirect:/admin/index.html";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String showAdminLoginPage(@RequestParam(value = "error", defaultValue = "false") boolean hasError,
                                     @RequestParam(value = "user", required = false) String username,
                                     Model model) {

        Huddle huddle = huddleService.getHuddle();

        if (hasError) {
            model.addAttribute("hasError", true);
        }
        model.addAttribute("username", username);
        return "/admin/login";
    }

    @RequestMapping(value = "not-activated", method = RequestMethod.GET)
    public String showNotActivatedPage(Model model) {
        model.addAttribute("huddle", huddleService.getHuddle());
        model.addAttribute("huddleOwner", memberService.getHuddleOwner());
        return "admin/not-activated";
    }

    @RequestMapping(value = "forgot", method = RequestMethod.GET)
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("request", new ForgotPasswordRequest());
        return "admin/forgot-password";
    }

    @RequestMapping(value = "forgot", method = RequestMethod.POST)
    public String sendPasswordResetEmail(@Valid @ModelAttribute("request") ForgotPasswordRequest request,
                                         BindingResult result, Model model) throws ResetPasswordFailedException, EmailsNotSetupException {

        if (result.hasErrors()) {
            return "admin/forgot-password";
        }

        try {
            memberService.sendPasswordResetEmail(request);
            return "admin/password-reset-sent";
        } catch (NoSuchMemberException e) {

            result.rejectValue("email", "error.noSuchMember", "No account could be found for this email address");
            return "admin/forgot-password";

        }
    }

    @RequestMapping(value = "reset/{resetCode}", method = RequestMethod.GET)
    public String showResetPasswordPage(@PathVariable("resetCode") String resetCode, Model model) {

        Member member = memberService.getMemberForPasswordResetCode(resetCode);
        if (member != null) {
            model.addAttribute("resetCode", resetCode);
            model.addAttribute("member", member);
            return "admin/reset-password";
        } else {
            return "admin/invalid-password-reset-code";
        }
    }

    @RequestMapping(value = "reset", method = RequestMethod.POST)
    public String resetPassword(@Valid @ModelAttribute("request") ResetPasswordRequest request,
                                BindingResult result, Model model) {

        if (result.hasErrors()) {
            return showResetPasswordPage(request.getResetCode(), model);
        }

        Member member = memberService.resetPassword(request);
        if (member != null) {
            model.addAttribute("member", member);
            return "admin/reset-password-success";
        } else {
            return "admin/invalid-reset-code";
        }
    }

}
