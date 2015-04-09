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

import com.goodhuddle.huddle.domain.BlogPost;
import com.goodhuddle.huddle.service.BlogService;
import com.goodhuddle.huddle.service.exception.BlogPostExistsException;
import com.goodhuddle.huddle.service.request.blog.CreateBlogPostRequest;
import com.goodhuddle.huddle.service.request.blog.SearchBlogPostRequest;
import com.goodhuddle.huddle.service.request.blog.UpdateBlogPostRequest;
import com.goodhuddle.huddle.web.api.util.UpdateIdChecker;
import com.goodhuddle.huddle.web.builder.blog.BlogPostCommentDetailBuilder;
import com.goodhuddle.huddle.web.builder.blog.BlogPostDetailBuilder;
import com.goodhuddle.huddle.web.builder.blog.BlogPostRefBuilder;
import com.goodhuddle.huddle.web.dto.blog.BlogPostDetail;
import com.goodhuddle.huddle.web.dto.blog.BlogPostRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/blog/post")
public class BlogPostApiController {

    private static final Logger log = LoggerFactory.getLogger(BlogPostApiController.class);

    private final BlogService blogService;
    private final BlogPostRefBuilder blogPostRefBuilder;
    private final BlogPostDetailBuilder blogPostDetailBuilder;
    private final BlogPostCommentDetailBuilder blogPostCommentDetailBuilder;

    @Autowired
    public BlogPostApiController(BlogService blogService,
                                 BlogPostRefBuilder blogPostRefBuilder,
                                 BlogPostDetailBuilder blogPostDetailBuilder,
                                 BlogPostCommentDetailBuilder blogPostCommentDetailBuilder) {
        this.blogService = blogService;
        this.blogPostRefBuilder = blogPostRefBuilder;
        this.blogPostDetailBuilder = blogPostDetailBuilder;
        this.blogPostCommentDetailBuilder = blogPostCommentDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<BlogPostRef> searchBlogPosts(SearchBlogPostRequest request) {
        log.debug("Retrieving blog posts: " + request);
        Page<BlogPost> result = blogService.searchBlogPosts(request);
        return blogPostRefBuilder.build(result);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public BlogPostDetail getBlogPost(@PathVariable long id) {
        log.debug("Retrieving blog post for ID: " + id);
        BlogPost post = blogService.getBlogPost(id);
        return blogPostDetailBuilder.build(post);
    }

    @RequestMapping(method = RequestMethod.POST)
    public BlogPostDetail createBlogPost(@Valid @RequestBody CreateBlogPostRequest request)
            throws BlogPostExistsException, IOException {

        log.info("Creating blog post at '{}'", request.getSlug());
        BlogPost post = blogService.createBlogPost(request);
        return blogPostDetailBuilder.build(post);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public BlogPostDetail updateBlogPost(@PathVariable long id, @Valid @RequestBody UpdateBlogPostRequest request)
            throws BlogPostExistsException, IOException {
        log.info("Updating blog post with ID '{}'", id);
        UpdateIdChecker.checkId(id, request);
        BlogPost post = blogService.updateBlogPost(request);
        return blogPostDetailBuilder.build(post);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBlogPost(@PathVariable long id) {
        log.info("Deleting blog post with ID '{}'", id);
        blogService.deleteBlogPost(id);
    }


    @RequestMapping(value = "{id}/attachment", method = RequestMethod.POST)
    public String uploadPageAttachment(@PathVariable long id, @ModelAttribute MultipartFile file) throws IOException {
        blogService.saveAttachment(id, file.getOriginalFilename(), file.getBytes());
        String url = "/blog/post/" + id + "/attachment/" + file.getOriginalFilename();
        log.debug("Uploaded attachment '{}' to '{}'", file.getOriginalFilename(), url);
        return url;
    }
}
