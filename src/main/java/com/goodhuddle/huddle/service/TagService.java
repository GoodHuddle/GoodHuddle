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


import com.goodhuddle.huddle.domain.Tag;
import com.goodhuddle.huddle.domain.Taggable;
import com.goodhuddle.huddle.service.exception.TagExistsException;
import com.goodhuddle.huddle.service.request.tag.CreateTagRequest;
import com.goodhuddle.huddle.service.request.tag.SearchTagsRequest;
import com.goodhuddle.huddle.service.request.tag.TagRequest;
import com.goodhuddle.huddle.service.request.tag.UntagRequest;
import org.springframework.data.domain.Page;

public interface TagService {

    Tag createTag(CreateTagRequest request) throws TagExistsException;

    Taggable tag(TagRequest request);

    Taggable untag(UntagRequest request);

    Page<? extends Tag> searchTags(SearchTagsRequest request);
}
