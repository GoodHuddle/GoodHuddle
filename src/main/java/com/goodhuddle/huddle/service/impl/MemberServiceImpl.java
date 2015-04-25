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

import com.goodhuddle.huddle.domain.*;
import com.goodhuddle.huddle.repository.MemberRepository;
import com.goodhuddle.huddle.repository.MemberSpecification;
import com.goodhuddle.huddle.repository.SecurityGroupRepository;
import com.goodhuddle.huddle.repository.TagRepository;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MemberService;
import com.goodhuddle.huddle.service.PageService;
import com.goodhuddle.huddle.service.exception.*;
import com.goodhuddle.huddle.service.impl.mail.EmailSender;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import com.goodhuddle.huddle.service.request.member.*;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final SecurityGroupRepository securityGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityHelper securityHelper;
    private final EmailSender emailSender;
    private final HuddleService huddleService;
    private final PageService pageService;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,
                             TagRepository tagRepository,
                             SecurityGroupRepository securityGroupRepository,
                             PasswordEncoder passwordEncoder,
                             SecurityHelper securityHelper,
                             EmailSender emailSender,
                             HuddleService huddleService,
                             PageService pageService) {

        this.memberRepository = memberRepository;
        this.tagRepository = tagRepository;
        this.securityGroupRepository = securityGroupRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityHelper = securityHelper;
        this.emailSender = emailSender;
        this.huddleService = huddleService;
        this.pageService = pageService;
    }

    @Override
    public Member getMember(long memberId) {
        return memberRepository.findByHuddleAndId(huddleService.getHuddle(), memberId);
    }

    @Override
    public Member getHuddleOwner() {
        List<Member> owners = memberRepository.findByHuddleAndHuddleOwnerIsTrue(huddleService.getHuddle());
        if (owners.size() > 0) {
            return owners.get(0);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public Member getOrCreateMemberByEmail(String email) {
        Member member = memberRepository.findByHuddleAndEmailIgnoreCase(huddleService.getHuddle(), email);
        if (member == null) {
            try {
                member = createMember(new CreateMemberRequest(null, null, null, email, null, null, null));
                log.info("New member created with ID {} for email address '{}'", member.getId(), member.getEmail());
            } catch (UsernameExistsException | EmailExistsException e) {
                log.error("An impossible error occurred while creating a new member for email: " + email, e);
            }
        }
        return member;
    }

    @Override
    public Member getLoggedInMember() {
        return securityHelper.getCurrentMember();
    }

    @Override
    public Page<Member> searchMembers(SearchMembersRequest request) {

        // todo check has permission to search sub-members

        Page<Member> pages = memberRepository.findAll(
                MemberSpecification.search(huddleService.getHuddle(), request),
                new PageRequest(request.getPage(), request.getSize()));
        return pages;
    }

    @Override
    @Transactional(readOnly = false)
    public Member createMember(CreateMemberRequest request) throws UsernameExistsException, EmailExistsException {

        if (request.getUsername() != null) {
            if (memberRepository.findByHuddleAndUsernameIgnoreCase(huddleService.getHuddle(), request.getUsername()) != null) {
                throw new UsernameExistsException("Member with username '" + request.getUsername() + "' already exists");
            }
        }

        if (request.getEmail() != null) {
            if (memberRepository.findByHuddleAndEmailIgnoreCase(huddleService.getHuddle(), request.getEmail()) != null) {
                throw new EmailExistsException("Member with email '" + request.getEmail() + "' already exists");
            }
        }

        log.info("Creating member with username: {}", request.getUsername());

        SecurityGroup securityGroup = null;
        if (request.getSecurityGroupId() != null) {
            securityGroup = securityGroupRepository.findByHuddleAndId(huddleService.getHuddle(), request.getSecurityGroupId());
        }

        String encodedPassword = request.getPassword() != null ?
                passwordEncoder.encode(request.getPassword()) : null;
        Member member = new Member(
                huddleService.getHuddle(),
                request.getUsername(), request.getFirstName(),
                request.getLastName(), request.getEmail(), request.getPostCode(),
                securityGroup, encodedPassword
        );
        member = memberRepository.save(member);
        return member;
    }

    @Override
    @Transactional(readOnly = false)
    public Member joinMailingList(JoinMailingListRequest request) throws EmailExistsException {

        Member existingMember = memberRepository.findByHuddleAndEmailIgnoreCase(huddleService.getHuddle(), request.getEmail());
        if (existingMember != null) {
            log.debug("Member already exists for: {}", request.getEmail());
            return existingMember;
        }

        log.info("Adding email {} to mailing list", request.getEmail());
        Member member = new Member(huddleService.getHuddle(), null, request.getEmail(), null, null, null);
        member = memberRepository.save(member);
        return member;
    }

    @Override
    public SignUpResult signUpMember(SignUpMemberRequest request) throws NoSuchBlockException {

        Huddle huddle = huddleService.getHuddle();

        SignUpResult result = new SignUpResult("/signup-success");
        List<Tag> tags = new ArrayList<>();
        if (request.getBlockId() != null) {
            PageContent.Block block = pageService.getBlock(request.getBlockId());
            if (block != null) {
                Map value = block.getValue();
                if (value != null) {
                    Object successUrl = value.get("successUrl");
                    if (successUrl != null) {
                        result.setSuccessUrl(String.valueOf(successUrl));
                    }

                    Object tagIds = value.get("tagIds");
                    if (tagIds instanceof List) {
                        for (Object tagIdObj : (List) tagIds) {
                            long tagId = Long.valueOf(String.valueOf(tagIdObj));
                            Tag tag = tagRepository.findByHuddleAndId(huddle, tagId);
                            if (tag != null) {
                                tags.add(tag);
                            }
                        }
                    }
                }
            } else {
                throw new NoSuchBlockException("No block found for ID: " + request.getBlockId());
            }
        }

        Member member = memberRepository.findByHuddleAndEmailIgnoreCase(huddleService.getHuddle(), request.getEmail());
        if (member != null) {
            log.debug("Member already exists for: {}", request.getEmail());
            result.setMember(member);
        } else {
            log.info("Signing up new member, email = {}", request.getEmail());
            member = new Member(huddleService.getHuddle(), null,
                    request.getFirstName(), request.getLastName(), request.getEmail(), request.getPostCode(),
                    null, null);
            member.setPostCode(request.getPostCode());
        }

        for (Tag tag : tags) {
            member.addTag(tag);
        }

        result.setMember(memberRepository.save(member));

        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public Member updateMember(UpdateMemberRequest request) throws UsernameExistsException, EmailExistsException {

        Member member = memberRepository.findByHuddleAndId(huddleService.getHuddle(), request.getId());

        if (request.getUsername() != null) {
            Member existing = memberRepository.findByHuddleAndUsernameIgnoreCase(huddleService.getHuddle(), request.getUsername());
            if (existing != null && !existing.getId().equals(request.getId())) {
                throw new UsernameExistsException("Member with username '" + request.getUsername() + "' already exists");
            }
        }

        if (request.getEmail() != null) {
            Member existing = memberRepository.findByHuddleAndEmailIgnoreCase(huddleService.getHuddle(), request.getEmail());
            if (existing != null && !existing.getId().equals(request.getId())) {
                throw new EmailExistsException("Member with username '" + request.getEmail() + "' already exists");
            }
        }

        SecurityGroup securityGroup = null;
        if (request.getSecurityGroupId() != null) {
            securityGroup = securityGroupRepository.findByHuddleAndId(huddleService.getHuddle(), request.getSecurityGroupId());
        }

        member.update(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPostCode(),
                securityGroup);

        if (securityGroup != null) {
            member.setUsername(request.getUsername());
            if (request.isChangePassword()) {
                String encodedPassword = passwordEncoder.encode(request.getPassword());
                member.setEncodedPassword(encodedPassword);
            }
        } else {
            member.setUsername(null);
            member.setEncodedPassword(null);
        }

        memberRepository.save(member);

        log.info("Member with ID {} updated", request.getId());
        return member;
    }

    @Override
    @Transactional(readOnly = false)
    public Member updatePassword(long id, String password) {

        Member member = memberRepository.findByHuddleAndId(huddleService.getHuddle(), id);
        String encodedPassword = passwordEncoder.encode(password);
        member.setEncodedPassword(encodedPassword);
        memberRepository.save(member);
        log.info("Password for member with ID {} updated", id);
        return member;
    }

    @Override
    public List<SecurityGroup> getSecurityGroups() {
        return securityGroupRepository.findByHuddle(huddleService.getHuddle());
    }

    @Override
    @Transactional(readOnly = false)
    public SecurityGroup createSecurityGroup(SecurityGroup securityGroup) {
        securityGroup = securityGroupRepository.save(securityGroup);
        log.info("SecurityGroup '{}' created with ID {}", securityGroup.getName(), securityGroup.getId());
        return securityGroup;
    }

    @Override
    public void processContactUsRequest(ContactUsRequest request) {
        List<Member> huddleOwners = memberRepository.findByHuddleAndHuddleOwnerIsTrue(huddleService.getHuddle());

        String safeEmail = StringEscapeUtils.escapeHtml4(request.getEmail());

        String message
                = "Contact us request."
                + "<p>Email: " + safeEmail + "</p>"
                + "<p>Name: " + StringEscapeUtils.escapeHtml4(request.getName()) + "</p>"
                + "<p>Message:</p><p>"
                + StringEscapeUtils.escapeHtml4(request.getMessage()) + "</p>";

        for (Member huddleOwner : huddleOwners) {
            emailSender.sendEmail(huddleOwner, "Contact us message from: " + safeEmail, message);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void sendPasswordResetEmail(ForgotPasswordRequest request)
            throws NoSuchMemberException, ResetPasswordFailedException, EmailsNotSetupException {

        Member member = memberRepository.findByHuddleAndEmailIgnoreCase(huddleService.getHuddle(), request.getEmail());
        if (member != null) {

            String resetCode = UUID.randomUUID().toString();
            member.setPasswordResetCode(resetCode);
            DateTime expiry = DateTime.now().plusDays(1);
            member.setPasswordResetExpiry(expiry);
            memberRepository.save(member);

            Huddle huddle = huddleService.getHuddle();

            String message
                    = "<p>Hi " + member.getDisplayName() + ",</p>"
                    + "<p>We received a request to reset your password for your GoodHuddle account for '" + huddle.getName() + "'."
                    + " If this request was from you, please follow this link to set a new password:</p>"
                    + "<ul><li><a href='" + huddle.getBaseUrl() + "/admin/reset/" + resetCode + "'>"
                    + huddle.getBaseUrl() + "/admin/reset/"  + resetCode + "</a></li></ul>"
                    + "<p>If this request was not from you, please ignore this email.</p>"
                    + "<p>If you have any questions or problems, please contact us at info@goodhuddle.com</p>"
                    + "<p>Kind regards,<br/> The GoodHuddle Team<br/><a href='http://goodhuddle.com'>http://goodhuddle.com</a></p>";

            try {

                emailSender.sendEmail(member.getEmail(), member.getDisplayName(),
                        "Password reset for " + huddle.getName(), message, Arrays.asList(""));

            } catch (IOException | MandrillApiError e) {
                throw new ResetPasswordFailedException("Unable to send reset password email to: " + member.getEmail(), e);
            }

        } else {
            throw new NoSuchMemberException("No member found for email: " + request.getEmail());
        }
    }

    @Override
    public Member getMemberForPasswordResetCode(String resetCode) {
        Member member = memberRepository.findByHuddleAndPasswordResetCode(huddleService.getHuddle(), resetCode);
        if (member != null && member.getPasswordResetExpiry() != null) {
            if (member.getPasswordResetExpiry().isAfterNow()) {
                return member;
            }
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public Member resetPassword(ResetPasswordRequest request) {
        Member member = getMemberForPasswordResetCode(request.getResetCode());
        if (member != null) {
            String encodedPassword = request.getPassword() != null ?
                    passwordEncoder.encode(request.getPassword()) : null;
            member.setEncodedPassword(encodedPassword);
            member.setPasswordResetCode(null);
            member.setPasswordResetExpiry(null);
            memberRepository.save(member);
        }
        return member;
    }
}
