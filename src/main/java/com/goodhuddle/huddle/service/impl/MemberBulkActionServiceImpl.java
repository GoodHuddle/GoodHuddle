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

package com.goodhuddle.huddle.service.impl;

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.domain.MemberBulkSet;
import com.goodhuddle.huddle.domain.Tag;
import com.goodhuddle.huddle.repository.MemberRepository;
import com.goodhuddle.huddle.repository.MemberSpecification;
import com.goodhuddle.huddle.repository.SecurityGroupRepository;
import com.goodhuddle.huddle.repository.TagRepository;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MemberBulkActionService;
import com.goodhuddle.huddle.service.impl.mail.EmailSender;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import com.goodhuddle.huddle.service.request.member.BulkTagMembersRequest;
import com.goodhuddle.huddle.service.request.member.SearchMembersRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Scope("session")
public class MemberBulkActionServiceImpl implements MemberBulkActionService {

    private static final Logger log = LoggerFactory.getLogger(MemberBulkActionServiceImpl.class);

    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final SecurityGroupRepository securityGroupRepository;
    private final SecurityHelper securityHelper;
    private final EmailSender emailSender;
    private final HuddleService huddleService;

    private List<MemberBulkSet> memberBulkSets;

    @Autowired
    public MemberBulkActionServiceImpl(MemberRepository memberRepository,
                                       TagRepository tagRepository,
                                       SecurityGroupRepository securityGroupRepository,
                                       SecurityHelper securityHelper,
                                       EmailSender emailSender,
                                       HuddleService huddleService) {

        this.memberRepository = memberRepository;
        this.tagRepository = tagRepository;
        this.securityGroupRepository = securityGroupRepository;
        this.securityHelper = securityHelper;
        this.emailSender = emailSender;
        this.huddleService = huddleService;
        this.memberBulkSets = new ArrayList<>();
    }

    @Override
    public MemberBulkSet createMemberBulkSet(SearchMembersRequest request) {

        Huddle huddle = huddleService.getHuddle();

        String id = UUID.randomUUID().toString();
        MemberBulkSet memberBulkSet = new MemberBulkSet(huddle.getId(), id);
        if (memberBulkSets.size() > 5) {
            memberBulkSets.remove(0);
        }
        memberBulkSets.add(memberBulkSet);

        Page<Member> members;
        Pageable pageRequest = new PageRequest(0, 1000);
        do {
            members = memberRepository.findAll(
                    MemberSpecification.search(huddleService.getHuddle(), request), pageRequest);
            log.trace("Found {} members to add to bulk set with ID: {}", members.getTotalElements(), id);
            for (Member member : members) {
                if (member.getHuddle().getId().equals(huddle.getId())) {
                    memberBulkSet.getMemberIds().add(member.getId());
                } else {
                    throw new AccessDeniedException("Attempt to add member to bulk set from different huddle IDs ("
                            + huddle.getId() + " != " + member.getHuddle().getId() + ") , user ID: "
                            + securityHelper.getCurrentMemberId());
                }
            }
            pageRequest = pageRequest.next();
        } while (!members.isLast());

        log.debug("Found {} total members for bulk set with ID: {}", memberBulkSet.getMemberIds().size(), id);
        return memberBulkSet;
    }

    public MemberBulkSet getMemberBulkSet(String id) {
        Huddle huddle = huddleService.getHuddle();
        for (MemberBulkSet memberBulkSet : memberBulkSets) {
            if (memberBulkSet.getId().equals(id)) {
                if (memberBulkSet.getHuddleId() == huddle.getId()) {
                    return memberBulkSet;
                } else {
                    throw new AccessDeniedException("Attempt to access member bulk set from different huddle IDs ("
                            + huddle.getId() + " != " + memberBulkSet.getHuddleId() + ") , user ID: "
                            + securityHelper.getCurrentMemberId());
                }
            }
        }
        return null;
    }

    @Override
    public MemberBulkSet bulkTagMembers(BulkTagMembersRequest request) {
        MemberBulkSet memberBulkSet = getMemberBulkSet(request.getBulkSetId());
        Huddle huddle = huddleService.getHuddle();
        List<Tag> tags = new ArrayList<>();
        for (long tagId : request.getTagIds()) {
            Tag tag = tagRepository.findByHuddleAndId(huddle, tagId);
            log.debug("Bulk tagging members with tag: {}", tag);
            tags.add(tag);
        }

        for (Long memberId : memberBulkSet.getMemberIds()) {
            Member member = memberRepository.findByHuddleAndId(huddle, memberId);
            for (Tag tag : tags) {
                if (!member.getTags().contains(tag)) {
                    member.getTags().add(tag);
                }
            }
            memberRepository.save(member);
        }
        log.info("Tagged {} members with {} tags", memberBulkSet.getMemberIds().size(), tags.size());
        return memberBulkSet;
    }
}
