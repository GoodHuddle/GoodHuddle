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

import com.goodhuddle.huddle.service.MemberBulkActionService;
import com.goodhuddle.huddle.service.request.member.BulkTagMembersRequest;
import com.goodhuddle.huddle.service.request.member.SearchMembersRequest;
import com.goodhuddle.huddle.web.builder.member.bulk.MemberBulkSetRefBuilder;
import com.goodhuddle.huddle.web.dto.member.bulk.MemberBulkSetRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/member/bulk")
@Scope("session")
public class MemberBulkActionApiController {

    private static final Logger log = LoggerFactory.getLogger(MemberBulkActionApiController.class);

    private final MemberBulkActionService memberBulkActionService;
    private final MemberBulkSetRefBuilder memberBulkSetRefBuilder;

    @Autowired
    public MemberBulkActionApiController(MemberBulkActionService memberBulkActionService,
                                         MemberBulkSetRefBuilder memberBulkSetRefBuilder) {
        this.memberBulkActionService = memberBulkActionService;
        this.memberBulkSetRefBuilder = memberBulkSetRefBuilder;
    }

    @RequestMapping(method = RequestMethod.POST)
    public MemberBulkSetRef createMemberBulkSet(@RequestBody SearchMembersRequest request) {
        return memberBulkSetRefBuilder.build(memberBulkActionService.createMemberBulkSet(request));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MemberBulkSetRef getMemberBulkSet(@PathVariable("id") String id) {
        return memberBulkSetRefBuilder.build(memberBulkActionService.getMemberBulkSet(id));
    }

    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public MemberBulkSetRef bulkTag(@Valid @RequestBody BulkTagMembersRequest request) {
        log.debug("Bulk tagging members in set with ID {}", request.getBulkSetId());
        return memberBulkSetRefBuilder.build(memberBulkActionService.bulkTagMembers(request));
    }
}
