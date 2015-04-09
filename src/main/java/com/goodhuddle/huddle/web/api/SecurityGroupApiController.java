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

import com.goodhuddle.huddle.service.MemberService;
import com.goodhuddle.huddle.web.builder.member.SecurityGroupDetailBuilder;
import com.goodhuddle.huddle.web.builder.member.SecurityGroupRefBuilder;
import com.goodhuddle.huddle.web.dto.member.SecurityGroupRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security/group")
public class SecurityGroupApiController {

    private static final Logger log = LoggerFactory.getLogger(SecurityGroupApiController.class);

    private final MemberService memberService;
    private final SecurityGroupRefBuilder securityGroupRefBuilder;
    private final SecurityGroupDetailBuilder securityGroupDetailBuilder;

    @Autowired
    public SecurityGroupApiController(MemberService memberService,
                                      SecurityGroupRefBuilder securityGroupRefBuilder,
                                      SecurityGroupDetailBuilder securityGroupDetailBuilder) {
        this.memberService = memberService;
        this.securityGroupRefBuilder = securityGroupRefBuilder;
        this.securityGroupDetailBuilder = securityGroupDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<SecurityGroupRef> getSecurityGroups() {
        return securityGroupRefBuilder.build(memberService.getSecurityGroups());
    }
}
