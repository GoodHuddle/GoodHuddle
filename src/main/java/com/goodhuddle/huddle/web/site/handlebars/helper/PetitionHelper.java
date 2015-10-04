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

package com.goodhuddle.huddle.web.site.handlebars.helper;

import com.github.jknack.handlebars.Options;
import com.goodhuddle.huddle.domain.Petition;
import com.goodhuddle.huddle.service.PetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PetitionHelper {

    private final PetitionService petitionService;

    @Autowired
    public PetitionHelper(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    public CharSequence petition(Options options) throws IOException {
        Object petitionIdObj = options.hash("petitionId");
        if (petitionIdObj != null ) {
            long petitionId = Long.parseLong(String.valueOf(petitionIdObj));
            Petition petition = petitionService.getPetition(petitionId);
            return options.fn(petition);
        } else {
            return "<span class='block-error'>This petition feed is not configured correctly. Please specify a petition to use.</span>";
        }
    }
}
