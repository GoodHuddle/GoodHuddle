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

import com.goodhuddle.huddle.domain.Email;
import com.goodhuddle.huddle.domain.EmailSettings;
import com.goodhuddle.huddle.domain.Mailout;
import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.repository.*;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MailoutService;
import com.goodhuddle.huddle.service.exception.EmailsAlreadySentException;
import com.goodhuddle.huddle.service.exception.EmailsNotGeneratedException;
import com.goodhuddle.huddle.service.impl.mail.EmailQueueProcessor;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import com.goodhuddle.huddle.service.request.mailout.CreateMailoutRequest;
import com.goodhuddle.huddle.service.request.mailout.QueuedMailoutRequest;
import com.goodhuddle.huddle.service.request.mailout.SearchEmailsRequest;
import com.goodhuddle.huddle.service.request.mailout.SearchMailoutsRequest;
import com.goodhuddle.huddle.service.request.mailout.settings.UpdateEmailSettingsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;

@Service
public class MailoutServiceImpl implements MailoutService {

    private static final Logger log = LoggerFactory.getLogger(MailoutServiceImpl.class);

    private final EmailSettingsRepository emailSettingsRepository;
    private final MailoutRepository mailoutRepository;
    private final EmailRepository emailRepository;
    private final MemberRepository memberRepository;
    private final SecurityHelper securityHelper;
    private final JmsTemplate jmsTemplate;
    private final HuddleService huddleService;

    @Autowired
    public MailoutServiceImpl(EmailSettingsRepository emailSettingsRepository,
                              MailoutRepository mailoutRepository,
                              EmailRepository emailRepository,
                              MemberRepository memberRepository,
                              SecurityHelper securityHelper,
                              JmsTemplate jmsTemplate,
                              HuddleService huddleService) {

        this.emailSettingsRepository = emailSettingsRepository;
        this.mailoutRepository = mailoutRepository;
        this.emailRepository = emailRepository;
        this.memberRepository = memberRepository;
        this.securityHelper = securityHelper;
        this.jmsTemplate = jmsTemplate;
        this.huddleService = huddleService;
    }

    @Override
    public EmailSettings getEmailSettings() {
        List<EmailSettings> settings = emailSettingsRepository.findByHuddle(huddleService.getHuddle());
        return settings.size() > 0 ? settings.get(0) : new EmailSettings(huddleService.getHuddle());
    }

    @Override
    @Transactional(readOnly = false)
    public EmailSettings updateEmailSettings(UpdateEmailSettingsRequest request) {
        EmailSettings settings = getEmailSettings();
        settings.setSendFromAddress(request.getSendFromAddress());
        settings.setSendFromName(request.getSendFromName());
        settings.setMandrillApiKey(request.getMandrillApiKey());
        emailSettingsRepository.save(settings);
        return settings;
    }

    @Override
    public Mailout getMailout(long id) {
        return mailoutRepository.findByHuddleAndId(huddleService.getHuddle(), id);
    }

    @Override
    public Page<? extends Mailout> searchMailouts(SearchMailoutsRequest request) {
        // todo proper search
        return mailoutRepository.findByHuddle(huddleService.getHuddle(),
                new PageRequest(request.getPage(), request.getSize(), Sort.Direction.DESC, "createdOn"));
    }

    @Override
    @Transactional(readOnly = false)
    public Mailout createMailout(CreateMailoutRequest request) {

        Member currentMember = securityHelper.getCurrentMember();

        Mailout mailout = mailoutRepository.save(new Mailout(
                huddleService.getHuddle(),
                request.getName(),
                request.getDescription(),
                request.getSubject(),
                request.getContent(),
                currentMember
        ));
        log.info("New mailout '{}' created with ID", request.getName(), mailout.getId());

        if (request.isGenerateEmails()) {
            generateEmails(mailout.getId());
        }

        return mailout;
    }

    @Override
    @Transactional(readOnly = false)
    public Mailout generateEmails(long mailoutId) {

        Mailout mailout = mailoutRepository.findByHuddleAndId(huddleService.getHuddle(), mailoutId);
        // todo check current geneated status

        log.debug("Generating emails for Mailout with ID {}", mailoutId);
        Member currentMember = securityHelper.getCurrentMember();

        // todo match members to criteria
        List<Member> members = memberRepository.findByHuddle(huddleService.getHuddle());
        for (Member member : members) {
            // todo replace tokens in subject and content
            String subject = mailout.getSubject();
            String content = mailout.getContent();
            emailRepository.save(new Email(mailout, member, subject, content, currentMember));
        }

        log.info("{} emails generated for Mailout with ID {}", members.size(), mailoutId);
        mailout.setGenerated(currentMember);
        mailoutRepository.save(mailout);
        return mailout;
    }

    @Override
    @Transactional(readOnly = false)
    public Mailout sendEmails(final long mailoutId) throws EmailsNotGeneratedException, EmailsAlreadySentException {

        log.debug("Sending emails for Mailout with ID {}", mailoutId);

        Mailout mailout = mailoutRepository.findByHuddleAndId(huddleService.getHuddle(), mailoutId);
        Member currentMember = securityHelper.getCurrentMember();

        if (mailout.getStatus().ordinal() < Mailout.Status.generated.ordinal()) {
            throw new EmailsNotGeneratedException(
                    "Emails have not been generated for mailout with ID '" + mailoutId + "'");
        }

        if (mailout.getStatus().ordinal() > Mailout.Status.generated.ordinal()) {
            throw new EmailsAlreadySentException(
                    "Emails have have already been sent for mailout with ID '" + mailoutId + "'");
        }


        final String huddleSlug = huddleService.getHuddle().getSlug();
        jmsTemplate.send(EmailQueueProcessor.QUEUE_NAME, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(new QueuedMailoutRequest(
                        huddleSlug, mailoutId
                ));
            }
        });

        log.info("Emails queue for sending for Mailout with ID {}", mailoutId);
        mailout.setSending(currentMember);
        mailoutRepository.save(mailout);
        return mailout;
    }

    @Override
    public Email getEmail(long emailId) {
        return emailRepository.findByHuddleAndId(huddleService.getHuddle(), emailId);
    }

    @Override
    public Page<? extends Email> searchEmails(SearchEmailsRequest request) {
        return emailRepository.findAll(
                EmailSpecification.search(huddleService.getHuddle(), request),
                new PageRequest(request.getPage(), request.getSize()));
    }
}
