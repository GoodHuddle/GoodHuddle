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

import com.goodhuddle.huddle.service.SearchService;
import com.goodhuddle.huddle.service.request.search.SearchWebsiteRequest;
import com.goodhuddle.huddle.web.builder.website.WebsiteEntityDetailBuilder;
import com.goodhuddle.huddle.web.dto.website.WebsiteEntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/search")
public class SearchApiController {

    private static final Logger log = LoggerFactory.getLogger(SearchApiController.class);

    private final SearchService searchService;
    private final WebsiteEntityDetailBuilder websiteEntityDetailBuilder;

    @Autowired
    public SearchApiController(SearchService searchService,
                               WebsiteEntityDetailBuilder websiteEntityDetailBuilder) {
        this.searchService = searchService;
        this.websiteEntityDetailBuilder = websiteEntityDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Page<WebsiteEntityDetail> searchMembers(@Valid @RequestBody SearchWebsiteRequest request) {
        log.debug("Searching website: " + request);
        return websiteEntityDetailBuilder.build(searchService.searchWebsite(request));
    }
}
