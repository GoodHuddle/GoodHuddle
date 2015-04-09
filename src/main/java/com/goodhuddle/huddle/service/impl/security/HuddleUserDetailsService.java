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
import com.goodhuddle.huddle.domain.SecurityGroup;
import com.goodhuddle.huddle.repository.MemberRepository;
import com.goodhuddle.huddle.web.HuddleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HuddleUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(HuddleUserDetailsService.class);

    private final HuddleContext huddleContext;
    private final MemberRepository memberRepository;

    @Autowired
    public HuddleUserDetailsService(HuddleContext huddleContext, MemberRepository memberRepository) {
        this.huddleContext = huddleContext;
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Loading user details for '{}' in huddle '{}'", username, huddleContext.getHuddle().getSlug());

        Member member = memberRepository.findByHuddleAndUsername(huddleContext.getHuddle(), username);
        if (member == null) {
            member = memberRepository.findByHuddleAndEmailIgnoreCase(huddleContext.getHuddle(), username);
            if (member == null) {
                throw new UsernameNotFoundException("Invalid username/password.");
            }
        }
        log.debug("Found member with ID {} for username '{}'", member.getId(), username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        SecurityGroup securityGroup = member.getSecurityGroup();
        if (securityGroup != null) {
            log.debug("Member '{}' has security group '{}'", username, member.getSecurityGroup().getName());
            for (String permission : securityGroup.getPermissions()) {
                log.debug("Adding permission: {}", permission);
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }

        return new AuthenticatedUser(member.getUsername(), member.getEncodedPassword(), authorities,
                member.getDisplayName(), member.getId());
    }
}
