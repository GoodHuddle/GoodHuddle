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

package com.goodhuddle.huddle.service.impl.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class HuddleAccessDecisionVoter implements AccessDecisionVoter {

    private static final Logger log = LoggerFactory.getLogger(HuddleAccessDecisionVoter.class);

    @Override
    public boolean supports(ConfigAttribute attribute) {
        log.info("*** Attribute: {}", attribute);
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        log.info("*** Supports: {}", clazz);
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        log.info("*** Vote: {}, {}, {}", authentication, object, collection);
        return 0;
    }
}
