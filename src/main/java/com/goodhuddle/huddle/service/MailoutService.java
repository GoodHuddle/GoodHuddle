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

package com.goodhuddle.huddle.service;

import com.goodhuddle.huddle.domain.Email;
import com.goodhuddle.huddle.domain.EmailSettings;
import com.goodhuddle.huddle.domain.Mailout;
import com.goodhuddle.huddle.service.exception.EmailsAlreadySentException;
import com.goodhuddle.huddle.service.exception.EmailsNotGeneratedException;
import com.goodhuddle.huddle.service.request.mailout.CreateMailoutRequest;
import com.goodhuddle.huddle.service.request.mailout.SearchEmailsRequest;
import com.goodhuddle.huddle.service.request.mailout.SearchMailoutsRequest;
import com.goodhuddle.huddle.service.request.mailout.settings.UpdateEmailSettingsRequest;
import org.springframework.data.domain.Page;

public interface MailoutService {

    EmailSettings getEmailSettings();

    EmailSettings updateEmailSettings(UpdateEmailSettingsRequest request);


    Mailout getMailout(long id);

    Page<? extends Mailout> searchMailouts(SearchMailoutsRequest request);

    Mailout createMailout(CreateMailoutRequest request);

    Mailout generateEmails(long mailoutId);

    Mailout sendEmails(long mailoutId) throws EmailsNotGeneratedException, EmailsAlreadySentException;


    Email getEmail(long emailId);

    Page<? extends Email> searchEmails(SearchEmailsRequest request);

}
