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
import com.goodhuddle.huddle.service.HuddleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityHelper {

    private final HuddleService huddleService;
    private final MemberRepository memberRepository;

    @Autowired
    public SecurityHelper(HuddleService huddleService, MemberRepository memberRepository) {
        this.huddleService = huddleService;
        this.memberRepository = memberRepository;
    }

    public Member getCurrentMember() {
        AuthenticatedUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return memberRepository.findByHuddleAndId(huddleService.getHuddle(), currentUser.getMemberId());
        } else {
            return null;
        }
    }

    public Long getCurrentMemberId() {
        AuthenticatedUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.getMemberId();
        } else {
            return null;
        }
    }

    public AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication != null ? authentication.getPrincipal() : null;
        return principal instanceof AuthenticatedUser ? (AuthenticatedUser) principal : null;
    }
}
