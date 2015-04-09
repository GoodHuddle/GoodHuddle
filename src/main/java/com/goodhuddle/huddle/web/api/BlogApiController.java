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

import com.goodhuddle.huddle.service.BlogService;
import com.goodhuddle.huddle.service.exception.BlogExistsException;
import com.goodhuddle.huddle.service.request.blog.CreateBlogRequest;
import com.goodhuddle.huddle.service.request.blog.UpdateBlogRequest;
import com.goodhuddle.huddle.web.api.util.UpdateIdChecker;
import com.goodhuddle.huddle.web.builder.blog.BlogDetailBuilder;
import com.goodhuddle.huddle.web.builder.blog.BlogRefBuilder;
import com.goodhuddle.huddle.web.dto.blog.BlogDetail;
import com.goodhuddle.huddle.web.dto.blog.BlogRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogApiController {

    private static final Logger log = LoggerFactory.getLogger(BlogApiController.class);

    private final BlogService blogService;
    private final BlogDetailBuilder blogDetailBuilder;
    private final BlogRefBuilder blogRefBuilder;

    @Autowired
    public BlogApiController(BlogService blogService,
                             BlogDetailBuilder blogDetailBuilder,
                             BlogRefBuilder blogRefBuilder) {
        this.blogService = blogService;
        this.blogDetailBuilder = blogDetailBuilder;
        this.blogRefBuilder = blogRefBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BlogRef> listBlogs() {
        log.debug("Retrieving all blogs");
        return blogRefBuilder.build(blogService.getBlogs());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BlogDetail getBlog(@PathVariable long id) {
        log.debug("Retrieving blog with ID '{}'", id);
        return blogDetailBuilder.build(blogService.getBlog(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public BlogDetail createBlog(@Valid @RequestBody CreateBlogRequest request)
            throws BlogExistsException {

        log.info("Creating blog at '{}'", request.getSlug());
        return blogDetailBuilder.build(blogService.createBlog(request));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateBlog(@PathVariable long id, @Valid @RequestBody UpdateBlogRequest request)
            throws BlogExistsException {
        log.info("Updating blog with ID '{}'", id);
        UpdateIdChecker.checkId(id, request);
        blogService.updateBlog(request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBlog(@PathVariable long id) {
        log.info("Deleting blog with ID '{}'", id);
        blogService.deleteBlog(id);
    }
}
