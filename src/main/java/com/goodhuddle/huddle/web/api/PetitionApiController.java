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

package com.goodhuddle.huddle.web.api;

import com.goodhuddle.huddle.service.PetitionService;
import com.goodhuddle.huddle.service.exception.EmailExistsException;
import com.goodhuddle.huddle.service.exception.UsernameExistsException;
import com.goodhuddle.huddle.service.request.petition.CreatePetitionRequest;
import com.goodhuddle.huddle.service.request.petition.SearchPetitionsRequest;
import com.goodhuddle.huddle.service.request.petition.UpdatePetitionRequest;
import com.goodhuddle.huddle.web.api.util.UpdateIdChecker;
import com.goodhuddle.huddle.web.builder.petition.PetitionDetailBuilder;
import com.goodhuddle.huddle.web.builder.petition.PetitionRowBuilder;
import com.goodhuddle.huddle.web.dto.petition.PetitionDetail;
import com.goodhuddle.huddle.web.dto.petition.PetitionRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/petition")
public class PetitionApiController {

    private static final Logger log = LoggerFactory.getLogger(PetitionApiController.class);

    private final PetitionService petitionService;
    private final PetitionRowBuilder petitionRowBuilder;
    private final PetitionDetailBuilder petitionDetailBuilder;

    @Autowired
    public PetitionApiController(PetitionService petitionService,
                                 PetitionRowBuilder petitionRowBuilder,
                                 PetitionDetailBuilder petitionDetailBuilder) {
        this.petitionService = petitionService;
        this.petitionRowBuilder = petitionRowBuilder;
        this.petitionDetailBuilder = petitionDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PetitionRow> searchPetitions(SearchPetitionsRequest request) {
        log.debug("Searching petitions: " + request);
        return petitionRowBuilder.build(petitionService.searchPetitions(request));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public PetitionDetail getPetition(@PathVariable long id) {
        return petitionDetailBuilder.build(petitionService.getPetition(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public PetitionDetail createPetition(@Valid @RequestBody CreatePetitionRequest request)
            throws UsernameExistsException, EmailExistsException {

        log.info("Creating petition '{}'", request.getName());
        return petitionDetailBuilder.build(petitionService.createPetition(request));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void updateMember(@PathVariable long id,
                             @Valid @RequestBody UpdatePetitionRequest request)
            throws UsernameExistsException, EmailExistsException {

        log.info("Updating member with ID '{}'", id);
        UpdateIdChecker.checkId(id, request);
        petitionService.updatePetition(request);
    }
}
