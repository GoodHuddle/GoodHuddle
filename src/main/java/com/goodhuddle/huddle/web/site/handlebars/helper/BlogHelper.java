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

package com.goodhuddle.huddle.web.site.handlebars.helper;

import com.github.jknack.handlebars.Options;
import com.goodhuddle.huddle.domain.Blog;
import com.goodhuddle.huddle.domain.BlogPost;
import com.goodhuddle.huddle.service.BlogService;
import com.goodhuddle.huddle.service.request.blog.SearchBlogPostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class BlogHelper {

    private final BlogService blogService;

    @Autowired
    public BlogHelper(BlogService blogService) {
        this.blogService = blogService;
    }

    public CharSequence blog(Options options) throws IOException {
        Object blogIdObject = options.hash("blogId");
        if (blogIdObject != null ) {
            long blogId = Long.parseLong(String.valueOf(blogIdObject));
            Blog blog = blogService.getBlog(blogId);
            return options.fn(blog);
        } else {
            return "<span class='block-error'>This blog feed is not configured correctly. Please specify a blog to use.</span>";
        }
    }

    public CharSequence blogFeed(Options options) throws IOException {
        Object blogIdObject = options.hash("blogId");
        if (blogIdObject != null ) {
            long blogId = Long.parseLong(String.valueOf(blogIdObject));
            int numPosts = Integer.parseInt(String.valueOf(options.hash("numPosts", 5)));

            StringBuilder builder = new StringBuilder();
            Page<BlogPost> posts = blogService.searchBlogPosts(new SearchBlogPostRequest(
                    Arrays.asList(blogId), false, 0, numPosts));
            for (BlogPost post : posts) {
                builder.append(options.fn(post));
            }
            return builder.toString();
        } else {
            return "<span class='block-error'>This blog feed is not configured correctly. Please specify a blog to use.</span>";
        }
    }
}
