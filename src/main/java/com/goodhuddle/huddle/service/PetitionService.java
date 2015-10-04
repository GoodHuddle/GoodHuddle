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

import com.goodhuddle.huddle.domain.Petition;
import com.goodhuddle.huddle.domain.PetitionSignature;
import com.goodhuddle.huddle.service.exception.EmailExistsException;
import com.goodhuddle.huddle.service.exception.EmailsNotSetupException;
import com.goodhuddle.huddle.service.exception.UsernameExistsException;
import com.goodhuddle.huddle.service.request.petition.*;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface PetitionService {

    Petition getPetition(long id);

    Page<? extends Petition> searchPetitions(SearchPetitionsRequest request);

    Petition createPetition(CreatePetitionRequest request);

    PetitionSignature signPetition(SignPetitionRequest request) throws EmailExistsException, UsernameExistsException, EmailsNotSetupException, IOException, MandrillApiError;

    Petition updatePetition(UpdatePetitionRequest request);

    Page<? extends PetitionSignature> searchPetitionSignatures(SearchPetitionSignatureRequest request);


//    Email getEmail(long emailId);
//
//    Page<? extends Email> searchEmails(SearchEmailsRequest request);

}
