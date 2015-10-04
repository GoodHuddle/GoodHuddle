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
import com.goodhuddle.huddle.domain.EmailSettings;
import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.repository.EmailRepository;
import com.goodhuddle.huddle.repository.EmailSettingsRepository;
import com.goodhuddle.huddle.service.exception.EmailsNotSetupException;
import com.goodhuddle.huddle.web.HuddleContext;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class EmailSender {

    private static final Logger log = LoggerFactory.getLogger(EmailSender.class);

    private final EmailRepository emailRepository;
    private final EmailSettingsRepository emailSettingsRepository;
    private final HuddleContext huddleContext;

    @Autowired
    public EmailSender(EmailRepository emailRepository,
                       EmailSettingsRepository emailSettingsRepository,
                       HuddleContext huddleContext) {

        this.emailRepository = emailRepository;
        this.emailSettingsRepository = emailSettingsRepository;
        this.huddleContext = huddleContext;
    }

    public Email sendEmail(Member member, String subject, String message) {
        Email email = emailRepository.save(new Email(member.getHuddle(), member, subject, message, member));
        sendEmail(email);
        return email;
    }

    public void sendEmail(Email email) {

        Member recipient = email.getRecipient();
        log.info("Sending email to: {}", recipient.getEmail());

        try {

            List<String> tags = new ArrayList<>();
            if (email.getMailout() != null) {
                tags.add(email.getMailout().getName());
            }

            MandrillMessageStatus result = sendEmail(recipient.getEmail(), recipient.getDisplayName(),
                    email.getSubject(), email.getContent(), tags);

            switch (result.getStatus()) {
                case "sent":
                    email.setStatus(Email.Status.sent);
                    break;
                case "queued":
                    email.setStatus(Email.Status.queued);
                    break;
                case "rejected":
                    email.setStatus(Email.Status.rejected);
                    email.setError(result.getRejectReason());
                    break;
                case "invalid":
                    email.setStatus(Email.Status.invalid);
                    email.setError(result.getRejectReason());
                    break;
                default:
                    email.setStatus(Email.Status.error);
                    email.setError("Unexpected response from Mandril server: " + result.getStatus());
                    break;
            }
            emailRepository.save(email);


        } catch (IOException | MandrillApiError e) {
            email.setStatus(Email.Status.error);
            email.setError(ExceptionUtils.getStackTrace(e));
            emailRepository.save(email);
        } catch (EmailsNotSetupException e) {
            e.printStackTrace();
        }
    }

    public MandrillMessageStatus sendEmail(String toEmail, String toName, String subject, String content, List<String> tags)
            throws IOException, MandrillApiError, EmailsNotSetupException {

        EmailSettings settings = getEmailSettings();
        return sendEmail(toEmail, toName, subject, content,
                settings.getSendFromAddress(), settings.getSendFromName(),  tags);
    }

    public MandrillMessageStatus sendEmail(String toEmail, String toName, String subject, String content,
                                           String fromEmail, String fromName, List<String> tags)
            throws IOException, MandrillApiError, EmailsNotSetupException {
        return sendEmail(Arrays.asList(new MailRecipient(toName, toEmail)), subject, content, fromEmail, fromName, tags);
    }

    public MandrillMessageStatus sendEmail(List<MailRecipient> mailRecipients, String subject, String content,
                                           String fromEmail, String fromName, List<String> tags)
            throws IOException, MandrillApiError, EmailsNotSetupException {


        MandrillApi mandrillApi = getMandrillApi();

        // create message
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setHtml(content);
        message.setAutoText(true);
        message.setFromEmail(fromEmail);
        message.setFromName(fromName);

        // add recipient
        List<MandrillMessage.Recipient> recipients = new ArrayList<>();
        for (MailRecipient mailRecipient : mailRecipients) {
            MandrillMessage.Recipient mandrilRecipient = new MandrillMessage.Recipient();
            mandrilRecipient.setEmail(mailRecipient.getEmail());
            mandrilRecipient.setName(mailRecipient.getName());
            recipients.add(mandrilRecipient);
        }

        message.setTo(recipients);
        message.setPreserveRecipients(true);

        message.setTags(tags);

        return mandrillApi.messages().send(message, false)[0];
    }

    private EmailSettings getEmailSettings() throws EmailsNotSetupException {
        List<EmailSettings> allSettings = emailSettingsRepository.findByHuddle(huddleContext.getHuddle());
        if (allSettings.size() == 0) {
            throw new EmailsNotSetupException("No email settings have been provided");
        }
        return allSettings.get(0);
    }


    public MandrillMessageStatus sendEmailWithTemplate(String templateName,
                                                       Map<String, String> parameters,
                                                       String toName,
                                                       String toEmail,
                                                       String subject) throws IOException, MandrillApiError, EmailsNotSetupException {

        return sendEmailWithTemplate(templateName, parameters,
                Arrays.asList(new MailRecipient(toName, toEmail)), subject);
    }

    public MandrillMessageStatus sendEmailWithTemplate(String templateName,
                                                       Map<String, String> parameters,
                                                       String[] emailAddresses,
                                                       String subject) throws IOException, MandrillApiError, EmailsNotSetupException {

        List<MailRecipient> recipients = new ArrayList<>();
        for (String emailAddress : emailAddresses) {
            recipients.add(new MailRecipient(emailAddress, emailAddress));
        }
        return sendEmailWithTemplate(templateName, parameters, recipients, subject);
    }

    public MandrillMessageStatus sendEmailWithTemplate(String templateName,
                                                       Map<String, String> parameters,
                                                       List<MailRecipient> mailRecipients,
                                                       String subject)
            throws IOException, MandrillApiError, EmailsNotSetupException {

        MandrillApi mandrillApi = getMandrillApi();
        EmailSettings settings = getEmailSettings();

        // create message
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setAutoText(true);
        message.setFromEmail(settings.getSendFromAddress());
        message.setFromName(settings.getSendFromName());

        // add recipient
        List<MandrillMessage.Recipient> recipients = new ArrayList<>();
        for (MailRecipient mailRecipient : mailRecipients) {
            log.info("Sending email to {}: {}", mailRecipient.getEmail(), subject);
            MandrillMessage.Recipient mandrillRecipient = new MandrillMessage.Recipient();
            mandrillRecipient.setEmail(mailRecipient.getEmail());
            mandrillRecipient.setName(mailRecipient.getName());
            recipients.add(mandrillRecipient);
        }

        message.setTo(recipients);
        message.setPreserveRecipients(true);

        message.setTrackClicks(false);

        List<MandrillMessage.MergeVar> mergeVars = new ArrayList<>();
        for (String param : parameters.keySet()) {
            mergeVars.add(new MandrillMessage.MergeVar(param, parameters.get(param)));
        }
        message.setGlobalMergeVars(mergeVars);

        return mandrillApi.messages().sendTemplate(templateName, new HashMap<String, String>(), message, true)[0];
    }

    public MandrillApi getMandrillApi() throws EmailsNotSetupException {
        EmailSettings settings = getEmailSettings();
        return new MandrillApi(settings.getMandrillApiKey());
    }


    //-------------------------------------------------------------------------

    public static final class MailRecipient {
        private String name;
        private String email;

        public MailRecipient(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
