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

package com.goodhuddle.huddle.web;

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.domain.Permissions;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HuddleGlobalInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(HuddleGlobalInterceptor.class);

    private final HuddleService huddleService;
    private final SecurityHelper securityHelper;

    public HuddleGlobalInterceptor(HuddleService huddleService,
                                   SecurityHelper securityHelper) {
        this.huddleService = huddleService;
        this.securityHelper = securityHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // anything under '/_huddles/' is not specific to a Huddle and is allowed at any time
        if (request.getRequestURI().startsWith("/_huddles")
                || request.getRequestURI().startsWith("/error/huddle-not-found")
                || request.getRequestURI().startsWith("/webhooks")) {
            return true;
        }

        Huddle huddle = huddleService.getHuddle();
        if (huddle == null) {
            if (huddleService.getHuddles().size() == 0) {
                response.sendRedirect("/_huddles/create");
            } else {
                response.sendRedirect("/error/huddle-not-found");
            }
            return false;
        }

        // check if admin setup wizard has been completed
        if (!huddle.isSetupWizardComplete()) {
            if (StringUtils.isNotBlank(request.getRequestURI())
                    && (request.getRequestURI().endsWith("/not-setup")
                    || request.getRequestURI().startsWith("/admin")
                    || request.getRequestURI().startsWith("/api"))) {
                return true;
            } else {
                response.sendRedirect("/not-setup");
                return false;
            }
        }

        // check if in coming soon mode
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = false;
        if (auth != null) {
            for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
                if (Permissions.Admin.access.equals(grantedAuthority.getAuthority())) {
                    isAdmin = true;
                    break;
                }
            }
        }

        if (huddle.isComingSoon() && !isAdmin) {
            if (StringUtils.isNotBlank(request.getRequestURI())
                    && (request.getRequestURI().endsWith("/not-setup")
                    || request.getRequestURI().startsWith("/coming-soon")
                    || request.getRequestURI().startsWith("/admin")
                    || request.getRequestURI().startsWith("/error")
                    || request.getRequestURI().startsWith("/api"))) {
                return true;
            } else {
                response.sendRedirect("/coming-soon");
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("huddle", huddleService.getHuddle());

            Member currentMember = securityHelper.getCurrentMember();
            if (currentMember != null) {
                modelAndView.addObject("loggedInUser", currentMember);
            }
        }
    }

}
