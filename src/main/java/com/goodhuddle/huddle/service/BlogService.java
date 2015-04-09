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

import com.goodhuddle.huddle.domain.Blog;
import com.goodhuddle.huddle.domain.BlogPost;
import com.goodhuddle.huddle.domain.BlogPostComment;
import com.goodhuddle.huddle.service.exception.*;
import com.goodhuddle.huddle.service.request.blog.*;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BlogService {

    Blog getBlog(long blogId);

    Blog getBlogBySlug(String name);

    List<Blog> getBlogs();

    Blog createBlog(CreateBlogRequest request) throws BlogExistsException;

    Blog updateBlog(UpdateBlogRequest request) throws BlogExistsException;

    void deleteBlog(long blogId);


    BlogPost getBlogPost(long id);

    BlogPost getBlogPostBySlug(String blogSlug, String blogPostSlug);

    Page<BlogPost> searchBlogPosts(SearchBlogPostRequest request);

    BlogPost createBlogPost(CreateBlogPostRequest request) throws BlogPostExistsException, IOException;

    BlogPost updateBlogPost(UpdateBlogPostRequest request) throws BlogPostExistsException, IOException;

    File getBlogPostFeatureImage(String blogSlug, String postSlug);

    File saveAttachment(long blogPostId, String fileName, byte[] data) throws IOException;

    File getAttachment(long blogPostId, String fileName) throws IOException;

    void deleteBlogPost(long blogPostId);


    BlogPostComment postComment(PostCommentRequest request)
            throws CommentsNotAllowedException, CommentsClosedException, CommentsByMembersOnlyException;

    Page<? extends BlogPostComment> searchBlogPostComments(SearchBlogPostCommentRequest request);
}
