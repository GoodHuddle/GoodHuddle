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

package com.goodhuddle.huddle.service.impl.mail;

import com.goodhuddle.huddle.domain.Email;
import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.Mailout;
import com.goodhuddle.huddle.repository.EmailRepository;
import com.goodhuddle.huddle.repository.MailoutRepository;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.request.mailout.QueuedMailoutRequest;
import com.goodhuddle.huddle.web.HuddleContext;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailQueueProcessor {

    public static final String QUEUE_NAME = EmailQueueProcessor.class.getName();

    private static final Logger log = LoggerFactory.getLogger(EmailQueueProcessor.class);

    @Value("${mandrill.api.key:}")
    private String defaultMandrillApiKey;

    private final EmailRepository emailRepository;
    private final MailoutRepository mailoutRepository;
    private final HuddleContext huddleContext;
    private final HuddleService huddleService;
    private final EmailSender emailSender;

    @Autowired
    public EmailQueueProcessor(EmailRepository emailRepository,
                               MailoutRepository mailoutRepository,
                               HuddleContext huddleContext,
                               HuddleService huddleService,
                               EmailSender emailSender) {
        this.emailRepository = emailRepository;
        this.mailoutRepository = mailoutRepository;
        this.huddleContext = huddleContext;
        this.huddleService = huddleService;
        this.emailSender = emailSender;
    }

    public void sendMailout(QueuedMailoutRequest request) {

        log.info("Processing mailout from queue, huddle = {}, mailout = {}",
                request.getHuddleSlug(), request.getMailoutId());

        Huddle huddle = huddleService.getHuddle(request.getHuddleSlug());
        huddleContext.setHuddle(huddle);

        Mailout mailout = mailoutRepository.findByHuddleAndId(huddle, request.getMailoutId());

        List<Email> pendingEmails = emailRepository.findByHuddleAndMailoutIdAndStatus(
                huddle, request.getMailoutId(), Email.Status.ready);
        for (Email email : pendingEmails) {
            emailSender.sendEmail(email);
            email.setSentOn(new DateTime());
            emailRepository.save(email);
        }

        mailout.setStatus(Mailout.Status.complete);
        mailoutRepository.save(mailout);
    }
}
