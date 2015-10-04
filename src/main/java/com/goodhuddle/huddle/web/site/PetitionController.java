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

package com.goodhuddle.huddle.web.site;

import com.goodhuddle.huddle.domain.PetitionSignature;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.service.PetitionService;
import com.goodhuddle.huddle.service.exception.EmailExistsException;
import com.goodhuddle.huddle.service.exception.EmailsNotSetupException;
import com.goodhuddle.huddle.service.exception.NoSuchPetitionException;
import com.goodhuddle.huddle.service.exception.UsernameExistsException;
import com.goodhuddle.huddle.service.request.petition.SignPetitionRequest;
import com.goodhuddle.huddle.web.builder.member.MemberDetailBuilder;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping(value = "/petition")
public class PetitionController {

    private static final Logger log = LoggerFactory.getLogger(PetitionController.class);

    private final PetitionService petitionService;
    private final MenuService menuService;
    private final MemberDetailBuilder memberDetailBuilder;


    @Autowired
    public PetitionController(PetitionService petitionService, MenuService menuService, MemberDetailBuilder memberDetailBuilder) {
        this.petitionService = petitionService;
        this.menuService = menuService;
        this.memberDetailBuilder = memberDetailBuilder;
    }

    @RequestMapping(value = "sign.do", method = RequestMethod.POST)
    @ResponseBody
    public SignPetitionResponse signPetition(@Valid @RequestBody SignPetitionRequest request)
            throws NoSuchPetitionException, EmailExistsException, UsernameExistsException, EmailsNotSetupException, IOException, MandrillApiError {

        log.debug("Signing petition {} for member: {}", request.getPetitionId(), request.getEmail());
        PetitionSignature result = petitionService.signPetition(request);
        return new SignPetitionResponse();
    }

    //-------------------------------------------------------------------------

    public static final class SignPetitionResponse {

        private String status;

        public SignPetitionResponse() {
            this.status = "success";
        }

        public String getStatus() {
            return status;
        }
    }
}
