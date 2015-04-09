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
import com.goodhuddle.huddle.service.exception.EmailExistsException;
import com.goodhuddle.huddle.service.exception.UsernameExistsException;
import com.goodhuddle.huddle.service.request.member.CreateMemberRequest;
import com.goodhuddle.huddle.service.request.member.SearchMembersRequest;
import com.goodhuddle.huddle.service.request.member.UpdateMemberRequest;
import com.goodhuddle.huddle.web.api.util.UpdateIdChecker;
import com.goodhuddle.huddle.web.builder.member.MemberDetailBuilder;
import com.goodhuddle.huddle.web.builder.member.MemberRefBuilder;
import com.goodhuddle.huddle.web.builder.member.MemberRowBuilder;
import com.goodhuddle.huddle.web.dto.member.MemberDetail;
import com.goodhuddle.huddle.web.dto.member.MemberRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/member")
public class MemberApiController {

    private static final Logger log = LoggerFactory.getLogger(MemberApiController.class);

    private final MemberService memberService;
    private final MemberRefBuilder memberRefBuilder;
    private final MemberRowBuilder memberRowBuilder;
    private final MemberDetailBuilder memberDetailBuilder;

    @Autowired
    public MemberApiController(MemberService memberService,
                               MemberRefBuilder memberRefBuilder,
                               MemberRowBuilder memberRowBuilder,
                               MemberDetailBuilder memberDetailBuilder) {

        this.memberService = memberService;
        this.memberRefBuilder = memberRefBuilder;
        this.memberRowBuilder = memberRowBuilder;
        this.memberDetailBuilder = memberDetailBuilder;
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public Page<MemberRow> searchMembers(@RequestBody SearchMembersRequest request) {
        log.debug("Retrieving member list: " + request);
        return memberRowBuilder.build(memberService.searchMembers(request));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public MemberDetail getMember(@PathVariable long id) {
        return memberDetailBuilder.build(memberService.getMember(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public MemberDetail createMember(@Valid @RequestBody CreateMemberRequest request)
            throws UsernameExistsException, EmailExistsException {

        log.info("Creating member '{}'", request.getUsername());
        return memberDetailBuilder.build(memberService.createMember(request));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void updateMember(@PathVariable long id,
                             @Valid @RequestBody UpdateMemberRequest request)
            throws UsernameExistsException, EmailExistsException {

        log.info("Updating member with ID '{}'", id);
        UpdateIdChecker.checkId(id, request);
        memberService.updateMember(request);
    }

    @RequestMapping(value = "{id}/password", method = RequestMethod.PUT)
    public void updatePassword(@PathVariable long id, @RequestBody String password)
            throws UsernameExistsException {

        log.info("Updating member password for member with ID '{}'", id);
        memberService.updatePassword(id, password);
    }

}
