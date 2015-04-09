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

import com.goodhuddle.huddle.domain.Page;
import com.goodhuddle.huddle.service.PageService;
import com.goodhuddle.huddle.service.exception.PageSlugExistsException;
import com.goodhuddle.huddle.service.request.page.CreatePageRequest;
import com.goodhuddle.huddle.service.request.page.UpdatePageRequest;
import com.goodhuddle.huddle.web.api.util.UpdateIdChecker;
import com.goodhuddle.huddle.web.builder.page.PageDetailBuilder;
import com.goodhuddle.huddle.web.dto.page.PageDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/page")
public class PageApiController {

    private static final Logger log = LoggerFactory.getLogger(PageApiController.class);

    private final PageService pageService;
    private final PageDetailBuilder pageDetailBuilder;

    @Autowired
    public PageApiController(PageService pageService,
                             PageDetailBuilder pageDetailBuilder) {
        this.pageService = pageService;
        this.pageDetailBuilder = pageDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PageDetail> getPages() {
        log.debug("Retrieving all pages");
        return pageDetailBuilder.build(pageService.getPages());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PageDetail getPage(@PathVariable long id) {
        log.debug("Retrieving page with ID '{}'", id);
        return pageDetailBuilder.build(pageService.getPage(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public PageDetail createPage(@Valid @RequestBody CreatePageRequest request) throws PageSlugExistsException {
        log.info("Creating page at '{}'", request.getSlug());
        Page page = pageService.createPage(request);
        return pageDetailBuilder.build(page);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updatePage(@PathVariable long id, @Valid @RequestBody UpdatePageRequest request) throws PageSlugExistsException {
        log.info("Updating page with ID '{}'", id);
        UpdateIdChecker.checkId(id, request);
        pageService.updatePage(request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePage(@PathVariable long id) {
        log.info("Deleting page with ID '{}'", id);
        pageService.deletePage(id);
    }

    @RequestMapping(value = "{id}/attachment", method = RequestMethod.POST)
    public String uploadPageAttachment(@PathVariable long id, @ModelAttribute MultipartFile file) throws IOException {
        pageService.saveAttachment(id, file.getOriginalFilename(), file.getBytes());
        String url = "/page/" + id + "/attachment/" + file.getOriginalFilename();
        log.debug("Uploaded attachment '{}' to '{}'", file.getOriginalFilename(), url);
        return url;
    }
}
