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
import com.goodhuddle.huddle.repository.*;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MemberService;
import com.goodhuddle.huddle.service.PetitionService;
import com.goodhuddle.huddle.service.exception.EmailExistsException;
import com.goodhuddle.huddle.service.exception.EmailsNotSetupException;
import com.goodhuddle.huddle.service.exception.UsernameExistsException;
import com.goodhuddle.huddle.service.impl.mail.EmailSender;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import com.goodhuddle.huddle.service.request.member.CreateMemberRequest;
import com.goodhuddle.huddle.service.request.petition.*;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PetitionServiceImpl implements PetitionService {

    private static final Logger log = LoggerFactory.getLogger(PetitionServiceImpl.class);

    private final EmailSender emailSender;
    private final PetitionRepository petitionRepository;
    private final PetitionTargetRepository petitionTargetRepository;
    private final PetitionSignatureRepository petitionSignatureRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final SecurityHelper securityHelper;
    private final HuddleService huddleService;

    @Autowired
    public PetitionServiceImpl(MemberRepository memberRepository,
                               SecurityHelper securityHelper,
                               PetitionRepository petitionRepository,
                               PetitionSignatureRepository petitionSignatureRepository,
                               MemberService memberService,
                               EmailSender emailSender,
                               PetitionTargetRepository petitionTargetRepository,
                               HuddleService huddleService) {
        this.emailSender = emailSender;
        this.petitionRepository = petitionRepository;
        this.petitionSignatureRepository = petitionSignatureRepository;
        this.memberRepository = memberRepository;
        this.securityHelper = securityHelper;
        this.memberService = memberService;
        this.petitionTargetRepository = petitionTargetRepository;
        this.huddleService = huddleService;
    }

    @Override
    public Petition getPetition(long id) {
        return petitionRepository.findByHuddleAndId(huddleService.getHuddle(), id);
    }

    @Override
    public Page<? extends Petition> searchPetitions(SearchPetitionsRequest request) {
        // todo proper search
        return petitionRepository.findByHuddle(huddleService.getHuddle(),
                new PageRequest(request.getPage(), request.getSize(), Sort.Direction.DESC, "createdOn"));
    }

    @Override
    @Transactional(readOnly = false)
    public Petition createPetition(CreatePetitionRequest request) {

        Member currentMember = securityHelper.getCurrentMember();

        Petition petition = petitionRepository.save(new Petition(
                huddleService.getHuddle(),
                request.getName(),
                request.getDescription(),
                request.getSubject(),
                request.getContent(),
                request.getPetitionEmailTemplate(),
                request.getThankyouEmailTemplate(),
                request.getAdminEmailAddresses(),
                request.getAdminEmailTemplate(),
                currentMember
        ));

        if (CollectionUtils.isNotEmpty(request.getTargets())) {
            for (AbstractPetitionRequest.Target target : request.getTargets()) {
                petitionTargetRepository.save(new PetitionTarget(petition, target.getName(), target.getEmail()));
            }
        }

        log.info("New petition '{}' created with ID", request.getName(), petition.getId());

        return petition;
    }


    @Override
    @Transactional(readOnly = false)
    public Petition updatePetition(UpdatePetitionRequest request) {
        Petition petition = petitionRepository.findByHuddleAndId(huddleService.getHuddle(), request.getId());

        petition.update(
                request.getName(),
                request.getDescription(),
                request.getSubject(),
                request.getContent(),
                request.getPetitionEmailTemplate(),
                request.getThankyouEmailTemplate(),
                request.getAdminEmailAddresses(),
                request.getAdminEmailTemplate());

        petitionRepository.save(petition);

        petitionTargetRepository.deleteByPetition(petition);
        if (CollectionUtils.isNotEmpty(request.getTargets())) {
            for (AbstractPetitionRequest.Target target : request.getTargets()) {
                petitionTargetRepository.save(new PetitionTarget(petition, target.getName(), target.getEmail()));
            }
        }

        log.info("Petition with ID {} updated", request.getId());
        return petition;
    }

    @Override
    public Page<? extends PetitionSignature> searchPetitionSignatures(SearchPetitionSignatureRequest request) {
        return petitionSignatureRepository.findAll(
                PetitionSignatureSpecification.search(huddleService.getHuddle(), request),
                new PageRequest(request.getPage(), request.getSize()));
    }


    @Override
    @Transactional(readOnly = false)
    public PetitionSignature signPetition(SignPetitionRequest request)
            throws EmailExistsException, UsernameExistsException, EmailsNotSetupException,
                   IOException, MandrillApiError {

        Huddle huddle = huddleService.getHuddle();

        Petition petition = petitionRepository.findByHuddleAndId(huddle, request.getPetitionId());

        Member member = memberRepository.findByHuddleAndEmailIgnoreCase(huddle, request.getEmail());
        if (member == null) {
            log.info("New member is signing petition {}", request.getEmail());
            member = memberService.createMember(new CreateMemberRequest(
                    null, request.getFirstName(), request.getLastName(), request.getEmail(),
                    request.getPostCode(), request.getPostCode(), null, null
            ));
        } else {
            log.info("Existing member is signing petition {}", request.getEmail());
        }

        PetitionSignature existing = petitionSignatureRepository.findByPetitionAndMemberId(petition, member.getId());
        if (existing != null) {
            log.info("Ignoring duplicate sign petition request from {}", request.getEmail());
            return existing;
        }

        PetitionSignature signature = petitionSignatureRepository.save(new PetitionSignature(
                petition, member, request.getSubject(), request.getContent(),
                securityHelper.getCurrentMember()
        ));

        Map<String, String> params = new HashMap<>();

        params.put("petition_id", String.valueOf(petition.getId()));
        params.put("petition_name", petition.getName());
        params.put("petition_subject", signature.getSubject());
        params.put("petition_content", signature.getMessage());

        params.put("member_first_name", member.getFirstName());
        params.put("member_last_name", member.getLastName());
        params.put("member_display_name", member.getDisplayName());
        params.put("member_email", member.getEmail());


        List<EmailSender.MailRecipient> recipients = new ArrayList<>();
        if (petition.getTargets() != null && petition.getTargets().size() > 0) {
            for (PetitionTarget target : petition.getTargets()) {
                recipients.add(new EmailSender.MailRecipient(
                        target.getName(), target.getEmail()
                ));
            }
            emailSender.sendEmailWithTemplate(
                    petition.getPetitionEmailTemplate(),
                    params,
                    recipients,
                    petition.getSubject()
            );
        } else {
            log.warn("No target recipients for petition: {}", petition.getSubject());
        }

        if (StringUtils.isNotBlank(petition.getThankyouEmailTemplate())) {
            emailSender.sendEmailWithTemplate(
                    petition.getThankyouEmailTemplate(),
                    params,
                    member.getDisplayName(),
                    member.getEmail(),
                    petition.getSubject()
            );
        }

        String[] adminEmails = petition.getAdminEmailAddressesArray();
        if (adminEmails != null && adminEmails.length > 0) {
            emailSender.sendEmailWithTemplate(
                    petition.getAdminEmailTemplate(),
                    params,
                    adminEmails,
                    "New signature for " + petition.getName()
            );
        }

        return signature;
    }
}
