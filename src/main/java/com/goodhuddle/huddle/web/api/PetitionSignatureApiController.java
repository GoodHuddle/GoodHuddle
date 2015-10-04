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
import com.goodhuddle.huddle.service.request.petition.SearchPetitionSignatureRequest;
import com.goodhuddle.huddle.web.builder.petition.PetitionSignatureRowBuilder;
import com.goodhuddle.huddle.web.dto.petition.PetitionSignatureRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/petition/signature")
public class PetitionSignatureApiController {

    private static final Logger log = LoggerFactory.getLogger(PetitionSignatureApiController.class);

    private final PetitionService petitionService;
    private final PetitionSignatureRowBuilder petitionSignatureRowBuilder;

    @Autowired
    public PetitionSignatureApiController(PetitionService petitionService,
                                          PetitionSignatureRowBuilder petitionSignatureRowBuilder) {
        this.petitionService = petitionService;
        this.petitionSignatureRowBuilder = petitionSignatureRowBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PetitionSignatureRow> searchPetitionSignatures(SearchPetitionSignatureRequest request) {
        log.debug("Searching petition signatures: " + request);
        return petitionSignatureRowBuilder.build(petitionService.searchPetitionSignatures(request));
    }
}
