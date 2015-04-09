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

package com.goodhuddle.huddle.service.impl.security;

import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.repository.MemberRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final SecurityHelper securityHelper;
    private final MemberRepository memberRepository;

    @Autowired
    public LoginSuccessHandler(SecurityHelper securityHelper, MemberRepository memberRepository) {
        this.securityHelper = securityHelper;
        this.memberRepository = memberRepository;

        setDefaultTargetUrl("/admin/index.html");
        setAlwaysUseDefaultTargetUrl(false);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws ServletException, IOException {

        Member member = securityHelper.getCurrentMember();
        member.setLastLogin(DateTime.now());
        member.setLoginAttempts(0);
        memberRepository.save(member);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
