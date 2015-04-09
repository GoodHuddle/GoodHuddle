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

import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import com.goodhuddle.huddle.web.builder.member.MemberDetailBuilder;
import com.goodhuddle.huddle.web.dto.member.MemberDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationApiController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationApiController.class);

    private final SecurityHelper securityHelper;
    private final MemberDetailBuilder memberDetailBuilder;

    @Autowired
    public AuthenticationApiController(SecurityHelper securityHelper, MemberDetailBuilder memberDetailBuilder) {
        this.securityHelper = securityHelper;
        this.memberDetailBuilder = memberDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public MemberDetail getCurrentUser() throws IOException {
        Member currentMember = securityHelper.getCurrentMember();
        return memberDetailBuilder.build(currentMember);
    }

}
