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

import com.goodhuddle.huddle.domain.WebsiteEntity;
import com.goodhuddle.huddle.repository.BlogPostRepository;
import com.goodhuddle.huddle.repository.BlogRepository;
import com.goodhuddle.huddle.repository.PageRepository;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.SearchService;
import com.goodhuddle.huddle.service.request.search.SearchWebsiteRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final HuddleService huddleService;
    private final PageRepository pageRepository;
    private final BlogRepository blogRepository;
    private final BlogPostRepository blogPostRepository;

    @Autowired
    public SearchServiceImpl(HuddleService huddleService,
                             PageRepository pageRepository,
                             BlogRepository blogRepository,
                             BlogPostRepository blogPostRepository) {
        this.huddleService = huddleService;

        this.pageRepository = pageRepository;
        this.blogRepository = blogRepository;
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public org.springframework.data.domain.Page<WebsiteEntity> searchWebsite(SearchWebsiteRequest request) {

        List<WebsiteEntity> matches = new ArrayList<>();

        // poor man's search until implemented properly
        String phrase = request.getSearchPhrase();
        String[] terms = phrase != null ? StringUtils.split(phrase.toLowerCase()) : null;

        List<WebsiteEntity> entities = new ArrayList<>();
        entities.addAll(pageRepository.findByHuddle(huddleService.getHuddle()));
        entities.addAll(blogRepository.findByHuddle(huddleService.getHuddle()));
        entities.addAll(blogPostRepository.findAll());

        for (WebsiteEntity entity : entities) {
            if (matches(terms, entity.getTitle()) || matches(terms, entity.getUrl())) {
                matches.add(entity);
            }
        }

        int start = request.getPage() * request.getSize();
        int end = Math.min(start + request.getSize(), matches.size());
        return new PageImpl<>(matches.subList(start, end),
                new PageRequest(request.getPage(), request.getSize()), matches.size());
    }

    private boolean matches(String[] terms, String target) {
        if (terms == null || terms.length == 0) {
            return true;
        }

        if (target == null) {
            return false;
        }

        target = target.toLowerCase();
        for (String term : terms) {
            if (!target.contains(term)) {
                return false;
            }
        }
        return true;
    }
}
