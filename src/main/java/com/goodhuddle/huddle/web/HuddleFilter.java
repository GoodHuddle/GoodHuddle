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

package com.goodhuddle.huddle.web;

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.service.HuddleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@Order(0)
public class HuddleFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HuddleFilter.class);

    private final HuddleContext huddleContext;
    private final HuddleService huddleService;

    @Autowired
    public HuddleFilter(HuddleContext huddleContext, HuddleService huddleService) {
        this.huddleContext = huddleContext;
        this.huddleService = huddleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String huddleSlug = StringUtils.trimToNull(request.getHeader("huddle"));

        if (log.isTraceEnabled()) {
            log.trace("Processing request '{}' for Huddle with slug: '{}'", request.getRequestURL(), huddleSlug);
        }

        if (!request.getRequestURI().startsWith("/_huddles")) {
            Huddle huddle;
            if (huddleSlug != null) {
                if (huddleSlug.endsWith(".goodhuddle.com")) {
                    huddleSlug = huddleSlug.substring(0, huddleSlug.length() - ".goodhuddle.com".length());
                }
                if (huddleSlug.startsWith("www.")) {
                    huddleSlug = huddleSlug.substring("www.".length());
                }
                huddle = huddleService.getHuddle(huddleSlug);
            } else {
                List<Huddle> huddles = huddleService.getHuddles();
                if (huddles.size() == 1) {
                    huddle = huddles.get(0);
                } else {
                    huddle = null;
                }
            }

            huddleContext.setHuddle(huddle);
        }

        chain.doFilter(request, response);

    }

}
