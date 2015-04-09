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

import com.goodhuddle.huddle.service.TagService;
import com.goodhuddle.huddle.service.exception.TagExistsException;
import com.goodhuddle.huddle.service.request.tag.CreateTagRequest;
import com.goodhuddle.huddle.service.request.tag.SearchTagsRequest;
import com.goodhuddle.huddle.service.request.tag.TagRequest;
import com.goodhuddle.huddle.service.request.tag.UntagRequest;
import com.goodhuddle.huddle.web.builder.member.tag.TagDetailBuilder;
import com.goodhuddle.huddle.web.builder.member.tag.TagRefBuilder;
import com.goodhuddle.huddle.web.dto.member.tag.TagDetail;
import com.goodhuddle.huddle.web.dto.member.tag.TagRef;
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
@RequestMapping("/api/tag")
public class TagApiController {

    private static final Logger log = LoggerFactory.getLogger(TagApiController.class);

    private final TagService tagService;
    private final TagRefBuilder tagRefBuilder;
    private final TagDetailBuilder tagDetailBuilder;

    @Autowired
    public TagApiController(TagService tagService, TagRefBuilder tagRefBuilder, TagDetailBuilder tagDetailBuilder) {
        this.tagService = tagService;
        this.tagRefBuilder = tagRefBuilder;
        this.tagDetailBuilder = tagDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<TagRef> searchTags(SearchTagsRequest request) {
        log.debug("Retrieving tag list: " + request);
        return tagRefBuilder.build(tagService.searchTags(request));
    }

    @RequestMapping(method = RequestMethod.POST)
    public TagDetail createTag(@Valid @RequestBody CreateTagRequest request) throws TagExistsException {
        log.info("Creating tag '{}'", request.getName());
        return tagDetailBuilder.build(tagService.createTag(request));
    }


    @RequestMapping(value = "/{id}/tagged", method = RequestMethod.POST)
    public void tag(@Valid @RequestBody TagRequest request) {
        log.info("Tagging {} with ID {} with tag ID '{}'", request.getType(), request.getTargetId(), request.getTagId());
        tagService.tag(request);
    }

    @RequestMapping(value = "/{id}/untagged", method = RequestMethod.POST)
    public void untag(@Valid @RequestBody UntagRequest request) {
        log.info("Untagging {} with ID {} from tag ID '{}'", request.getType(), request.getTargetId(), request.getTagId());
        tagService.untag(request);
    }
}
