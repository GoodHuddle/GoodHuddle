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
import com.goodhuddle.huddle.service.request.blog.SearchBlogPostCommentRequest;
import com.goodhuddle.huddle.web.builder.blog.BlogPostCommentDetailBuilder;
import com.goodhuddle.huddle.web.builder.blog.BlogPostDetailBuilder;
import com.goodhuddle.huddle.web.builder.blog.BlogPostRefBuilder;
import com.goodhuddle.huddle.web.dto.blog.BlogPostCommentDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog/post/comment")
public class BlogPostCommentApiController {

    private static final Logger log = LoggerFactory.getLogger(BlogPostCommentApiController.class);

    private final BlogService blogService;
    private final BlogPostRefBuilder blogPostRefBuilder;
    private final BlogPostDetailBuilder blogPostDetailBuilder;
    private final BlogPostCommentDetailBuilder blogPostCommentDetailBuilder;

    @Autowired
    public BlogPostCommentApiController(BlogService blogService,
                                        BlogPostRefBuilder blogPostRefBuilder,
                                        BlogPostDetailBuilder blogPostDetailBuilder,
                                        BlogPostCommentDetailBuilder blogPostCommentDetailBuilder) {
        this.blogService = blogService;
        this.blogPostRefBuilder = blogPostRefBuilder;
        this.blogPostDetailBuilder = blogPostDetailBuilder;
        this.blogPostCommentDetailBuilder = blogPostCommentDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<BlogPostCommentDetail> searchBlogPosts(SearchBlogPostCommentRequest request) {
        return blogPostCommentDetailBuilder.build(blogService.searchBlogPostComments(request));
    }
}
