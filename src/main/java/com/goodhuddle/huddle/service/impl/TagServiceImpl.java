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

package com.goodhuddle.huddle.service.impl;

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.domain.Tag;
import com.goodhuddle.huddle.domain.Taggable;
import com.goodhuddle.huddle.repository.MemberRepository;
import com.goodhuddle.huddle.repository.TagRepository;
import com.goodhuddle.huddle.repository.TagSpecification;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.TagService;
import com.goodhuddle.huddle.service.exception.TagExistsException;
import com.goodhuddle.huddle.service.request.tag.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private static final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    private final HuddleService huddleService;
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public TagServiceImpl(HuddleService huddleService,
                          TagRepository tagRepository,
                          MemberRepository memberRepository) {
        this.huddleService = huddleService;
        this.tagRepository = tagRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Tag createTag(CreateTagRequest request) throws TagExistsException {
        Huddle huddle = huddleService.getHuddle();
        Tag existing = tagRepository.findByHuddleAndName(huddle, request.getName());
        if (existing != null) {
            throw new TagExistsException("A tag already exists for '" + request.getName() + "'");
        }

        Tag tag = tagRepository.save(new Tag(huddle, request.getName(), request.getDescription()));
        log.info("New member tag created for '{}' with ID {}", tag.getName(), tag.getId());
        return tag;

    }

    @Override
    public Page<? extends Tag> searchTags(SearchTagsRequest request) {
        Page<Tag> tags = tagRepository.findAll(
                TagSpecification.search(huddleService.getHuddle(), request),
                new PageRequest(request.getPage(), request.getSize()));
        return tags;
    }


    @Override
    public Taggable tag(TagRequest request) {
        Taggable taggable = getTaggable(request.getTargetId(), request.getType());
        Tag tag = tagRepository.findByHuddleAndId(huddleService.getHuddle(), request.getTagId());

        List<Tag> tags = taggable.getTags();
        if (tags != null) {
            for (Tag memberTag : tags) {
                if (memberTag.getId().equals(tag.getId())) {
                    log.debug("Ignoring request to add tag '{}' to member {}, tag already exists",
                            tag.getName(), request.getTargetId());
                    return taggable;
                }
            }
        }

        List<Tag> assignedTags = taggable.getTags();
        if (assignedTags == null) {
            assignedTags = new ArrayList<>();
            taggable.setTags(assignedTags);
        }
        assignedTags.add(tag);

        saveTaggable(taggable, request.getType());
        log.info("{} with ID {} tagged with '{}'", request.getType(), request.getTargetId(), tag.getName());
        return taggable;
    }

    @Override
    @Transactional(readOnly = false)
    public Taggable untag(UntagRequest request) {

        Taggable taggable = getTaggable(request.getTargetId(), request.getType());
        List<Tag> tags = taggable.getTags();
        if (tags != null) {
            for (Tag tag : tags) {
                if (tag.getId().equals(request.getTagId())) {
                    log.info("{} with ID {} untagged from '{}'",
                            request.getType(), request.getTargetId(), tag.getName());
                    tags.remove(tag);
                    return saveTaggable(taggable, request.getType());
                }
            }
        }

        log.debug("Ignoring request to remove tag '{}' from member {}, tag not present", request.getTagId(), request.getTagId());
        return taggable;
    }

    private Taggable getTaggable(Long id, TagTargetType type) {
        switch (type) {
            case member:
                return memberRepository.findByHuddleAndId(huddleService.getHuddle(), id);
            default:
                throw new IllegalArgumentException("Unsupported taggable type: " + type);
        }
    }

    private Taggable saveTaggable(Taggable taggable, TagTargetType type) {
        switch (type) {
            case member:
                return memberRepository.save((Member) taggable);
            default:
                throw new IllegalArgumentException("Unsupported taggable type: " + type);
        }
    }
}
