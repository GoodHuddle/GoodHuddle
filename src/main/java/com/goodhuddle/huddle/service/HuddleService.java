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

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.service.exception.*;
import com.goodhuddle.huddle.service.request.huddle.CreateHuddleRequest;
import com.goodhuddle.huddle.service.request.huddle.SetupDefaultPagesRequest;
import com.goodhuddle.huddle.service.request.huddle.UpdateHuddleRequest;

import java.io.IOException;
import java.util.List;

public interface HuddleService {

    Huddle getHuddle();

    Huddle getHuddle(String slug);

    List<Huddle> getHuddles();


    Huddle createHuddle(CreateHuddleRequest request)
            throws HuddleExistsException, InvalidHuddleInvitationCodeException, InvalidHuddleSlugException;

    Huddle updateHuddle(UpdateHuddleRequest request);

    void setupDefaultPages(SetupDefaultPagesRequest request)
            throws PageSlugExistsException, HuddleExistsException,
            BlogExistsException, BlogPostExistsException, IOException;

    Huddle setupWizardComplete(boolean complete);

}
