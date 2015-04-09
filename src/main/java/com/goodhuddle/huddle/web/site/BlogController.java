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

package com.goodhuddle.huddle.web.site;

import com.goodhuddle.huddle.domain.Blog;
import com.goodhuddle.huddle.domain.BlogPost;
import com.goodhuddle.huddle.domain.BlogPostComment;
import com.goodhuddle.huddle.service.BlogService;
import com.goodhuddle.huddle.service.exception.CommentsByMembersOnlyException;
import com.goodhuddle.huddle.service.exception.CommentsClosedException;
import com.goodhuddle.huddle.service.exception.CommentsNotAllowedException;
import com.goodhuddle.huddle.service.request.blog.PostCommentRequest;
import com.goodhuddle.huddle.service.request.blog.SearchBlogPostRequest;
import com.goodhuddle.huddle.web.builder.blog.BlogPostCommentDetailBuilder;
import com.goodhuddle.huddle.web.dto.blog.BlogPostCommentDetail;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    private static final Logger log = LoggerFactory.getLogger(BlogController.class);

    private final BlogService blogService;
    private final ThemeHelper themeHelper;
    private final BlogPostCommentDetailBuilder blogPostCommentDetailBuilder;


    @Autowired
    public BlogController(BlogService blogService, ThemeHelper themeHelper,
                          BlogPostCommentDetailBuilder blogPostCommentDetailBuilder) {
        this.blogService = blogService;
        this.themeHelper = themeHelper;
        this.blogPostCommentDetailBuilder = blogPostCommentDetailBuilder;
    }

    @RequestMapping(value = "{slug}", method = RequestMethod.GET)
    public String showBlog(@PathVariable String slug, @RequestParam(value = "page", defaultValue = "0") int page,  Model model) {

        log.debug("Showing blog '{}'", slug);

        Blog blog = blogService.getBlogBySlug(slug);
        model.addAttribute("blog", blog);

        Page<BlogPost> posts = blogService.searchBlogPosts(new SearchBlogPostRequest(
                blog.getId(), false, page, 10));
        model.addAttribute("blogPosts", posts);

        themeHelper.addThemeAttributes(model, blog.getMenuItem());

        return "blog/" + (blog.getLayout() != null ? blog.getLayout() : "default-blog");
    }

    @RequestMapping(value = "{blogSlug}/{postSlug}", method = RequestMethod.GET)
    public String showBlogPost(@PathVariable String blogSlug, @PathVariable String postSlug, Model model) {

        log.debug("Showing blog post for '{}/{}'", blogSlug, postSlug);
        BlogPost post = blogService.getBlogPostBySlug(blogSlug, postSlug);
        model.addAttribute("blogPost", post);
        themeHelper.addThemeAttributes(model, post.getBlog().getMenuItem());

        String layout = post.getLayout();
        if (layout == null) {
            layout = post.getBlog().getDefaultPostLayout();
        }
        return "blog/post/" + (layout != null ? layout : "default-blog-post");
    }

    @RequestMapping(value = "{blogSlug}/{postSlug}/feature-image", method = RequestMethod.GET)
    public void downloadFeatureImage(@PathVariable String blogSlug,
                                     @PathVariable String postSlug,
                                     HttpServletResponse response)
            throws IOException {

        log.debug("Showing feature image for blog post for '{}/{}'", blogSlug, postSlug);
        File file = blogService.getBlogPostFeatureImage(blogSlug, postSlug);
        if (file != null) {
            Tika tika = new Tika();
            String contentType = tika.detect(file);
            response.setContentType(contentType);
            try (InputStream fileStream = new FileInputStream(file)) {
                IOUtils.copy(fileStream, response.getOutputStream());
            }
        } else {
            // todo show default image
        }
    }


    @RequestMapping(value = "/post/{blogPostId}/attachment/{fileName:.+}", method = RequestMethod.GET)
    public void downloadPageAttachment(@PathVariable long blogPostId,
                                       @PathVariable String fileName,
                                       HttpServletResponse response)
            throws IOException {

        log.debug("Showing attachment for blog post for '{}/{}'", blogPostId, fileName);
        File file = blogService.getAttachment(blogPostId, fileName);
        if (file != null) {
            Tika tika = new Tika();
            String contentType = tika.detect(file);
            response.setContentType(contentType);
            try (InputStream fileStream = new FileInputStream(file)) {
                IOUtils.copy(fileStream, response.getOutputStream());
            }
        } else {
            log.warn("Attempt to download missing blog post attachment: {}/{}", blogPostId, fileName);
        }
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public BlogPostCommentDetail postComment(@RequestBody PostCommentRequest request)
            throws CommentsClosedException, CommentsByMembersOnlyException, CommentsNotAllowedException {
        log.debug("Adding comment to blog post: {}", request.getBlogPostId());
        BlogPostComment comment = blogService.postComment(request);
        return blogPostCommentDetailBuilder.build(comment);
    }
}
