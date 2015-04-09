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

import com.goodhuddle.huddle.domain.*;
import com.goodhuddle.huddle.repository.*;
import com.goodhuddle.huddle.service.BlogService;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.service.exception.*;
import com.goodhuddle.huddle.service.impl.file.FileStore;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import com.goodhuddle.huddle.service.request.blog.*;
import com.goodhuddle.huddle.service.request.menu.UpdateMenuItemDetailsRequest;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    private static final Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);

    private final HuddleService huddleService;
    private final BlogRepository blogRepository;
    private final BlogPostRepository blogPostRepository;
    private final BlogPostCommentRepository blogPostCommentRepository;
    private final TagRepository tagRepository;
    private final MenuService menuService;
    private final SecurityHelper securityHelper;
    private final FileStore fileStore;

    @Autowired
    public BlogServiceImpl(HuddleService huddleService,
                           BlogRepository blogRepository,
                           BlogPostRepository blogPostRepository,
                           BlogPostCommentRepository blogPostCommentRepository,
                           TagRepository tagRepository,
                           MenuService menuService,
                           SecurityHelper securityHelper,
                           FileStore fileStore) {

        this.huddleService = huddleService;
        this.blogRepository = blogRepository;
        this.blogPostRepository = blogPostRepository;
        this.blogPostCommentRepository = blogPostCommentRepository;
        this.tagRepository = tagRepository;
        this.menuService = menuService;
        this.securityHelper = securityHelper;
        this.fileStore = fileStore;
    }

    @Override
    public Blog getBlog(long blogId) {
        return blogRepository.findByHuddleAndId(huddleService.getHuddle(), blogId);
    }

    @Override
    public Blog getBlogBySlug(String slug) {
        return blogRepository.findByHuddleAndSlug(huddleService.getHuddle(), slug);
    }

    @Override
    public List<Blog> getBlogs() {
        return blogRepository.findByHuddle(huddleService.getHuddle());
    }

    @Override
    public Blog createBlog(CreateBlogRequest request) throws BlogExistsException {

        Blog existing = blogRepository.findByHuddleAndSlug(huddleService.getHuddle(), request.getSlug());
        if (existing != null) {
            throw new BlogExistsException("Blog already exists with slug: " + request.getSlug());
        }

        Blog blog = blogRepository.save(new Blog(huddleService.getHuddle(),
                request.getTitle(), request.getSlug(), request.getLayout(), request.getDefaultPostLayout(),
                AllowCommentsType.members, RequireCommentApprovalType.none));
        log.info("Blog '{}' created with ID {}", blog.getTitle(), blog.getId());
        MenuItem menuItem = menuService.createMenuItem(
                request.getTitle(), request.getMenuId(), request.getParentItemId(), request.getPosition(),
                MenuItem.Type.blog, blog.getId(), blog.getUrl());

        blog.setMenuItem(menuItem);
        blogRepository.save(blog);

        log.info("Blog '{}' created with ID {}", blog.getTitle(), blog.getId());
        return blog;
    }

    @Override
    public Blog updateBlog(UpdateBlogRequest request) throws BlogExistsException {

        Blog slugOwner = blogRepository.findByHuddleAndSlug(huddleService.getHuddle(), request.getSlug());
        if (slugOwner != null && !(slugOwner.getId().equals(request.getId()))) {
            throw new BlogExistsException("The blog slug '" + request.getSlug() + "' is already in use");
        }

        Blog blog = blogRepository.findByHuddleAndId(huddleService.getHuddle(), request.getId());
        blog.update(request.getTitle(), request.getSlug(), request.getLayout(), request.getDefaultPostLayout(),
                request.getAllowComments(), request.getRequireCommentApproval());
        blog = blogRepository.save(blog);
        menuService.updateMenuItemDetails(new UpdateMenuItemDetailsRequest(
                blog.getMenuItem().getId(), blog.getTitle(), blog.getUrl()
        ));
        log.info("Updated blog with ID {}", blog.getId());
        return blog;
    }

    @Override
    public void deleteBlog(long blogId) {
        Blog blog = blogRepository.findByHuddleAndId(huddleService.getHuddle(), blogId);
        if (blog != null) {
            blogRepository.delete(blogId);
            menuService.deleteMenuItem(blog.getMenuItem().getId(), true);
            log.info("Deleted blog with ID {}", blogId);
        } else {
            log.debug("Ignoring delete request of blog {} as it does not exist", blogId);
        }
    }

    @Override
    public BlogPost getBlogPost(long id) {
        return blogPostRepository.findByHuddleAndId(huddleService.getHuddle(), id);
    }

    @Override
    public BlogPost getBlogPostBySlug(String blogSlug, String blogPostSlug) {
        return blogPostRepository.findByHuddleAndBlogSlugAndSlug(huddleService.getHuddle(), blogSlug, blogPostSlug);
    }

    @Override
    public Page<BlogPost> searchBlogPosts(SearchBlogPostRequest request) {
        Page<BlogPost> posts = blogPostRepository.findAll(BlogPostSpecification.search(huddleService.getHuddle(), request),
                new PageRequest(request.getPage(), request.getSize(), Sort.Direction.DESC, "publishedOn", "createdOn"));
        log.debug("Found {} blog posts matching search criteria", posts.getNumberOfElements());
        return posts;
    }

    @Override
    public BlogPost createBlogPost(CreateBlogPostRequest request) throws BlogPostExistsException, IOException {

        Blog blog = blogRepository.findByHuddleAndId(huddleService.getHuddle(), request.getBlogId());

        BlogPost existing = blogPostRepository.findByHuddleAndBlogIdAndSlug(huddleService.getHuddle(), blog.getId(), request.getSlug());
        if (existing != null) {
            throw new BlogPostExistsException("BlogPost already exists with slug: " + request.getSlug());
        }

        Member currentMember = securityHelper.getCurrentMember();
        BlogPost blogPost = new BlogPost(blog, request.getTitle(),
                request.getSlug(), request.getLayout(), currentMember,
                request.getBlurb(), StringUtils.isNotBlank(request.getFeatureImageId()), request.getContent());

        if (request.isPublish() && !blogPost.isPublished()) {
            blogPost.setPublishedOn(new DateTime());
            if (blogPost.getAuthor() == null) {
                blogPost.setAuthor(currentMember);
            }
        }

        blogPost = blogPostRepository.save(blogPost);

        // find the image
        if (request.getFeatureImageId() != null && !StringUtils.equals(request.getFeatureImageId(), "_remove")) {
            fileStore.moveTempFile(request.getFeatureImageId(), getFeatureImagePath(blogPost.getId()));
        }

        log.info("Blog post '{}' created with ID {} ", blogPost.getTitle(), blogPost.getId());
        return blogPost;
    }

    @Override
    public BlogPost updateBlogPost(UpdateBlogPostRequest request) throws BlogPostExistsException, IOException {

        Huddle huddle = huddleService.getHuddle();

        BlogPost blogPost = blogPostRepository.findByHuddleAndId(huddle, request.getId());

        BlogPost existing = blogPostRepository.findByHuddleAndBlogIdAndSlug(
                huddle, blogPost.getBlog().getId(), request.getSlug());
        if (existing != null && !existing.getId().equals(request.getId())) {
            throw new BlogPostExistsException("BlogPost already exists with slug: " + request.getSlug());
        }

        // find the image
        if (request.getFeatureImageId() != null) {
            if (StringUtils.equals(request.getFeatureImageId(), "_remove")) {
                log.debug("Removing feature image for post");
                fileStore.delete(getFeatureImagePath(blogPost.getId()));
                blogPost.setFeatureImageAvailable(false);
            } else {
                log.debug("Updating feature image for post to temp image with ID {}", request.getFeatureImageId());
                fileStore.moveTempFile(request.getFeatureImageId(), getFeatureImagePath(blogPost.getId()));
                blogPost.setFeatureImageAvailable(true);
            }
        }

        blogPost.update(request.getTitle(), request.getSlug(), request.getLayout(),
                request.getBlurb(), request.getContent(), request.isCommentsOpen());

        if (request.getTagIds() != null) {
            List<Tag> tags = new ArrayList<>();
            for (long tagId : request.getTagIds()) {
                tags.add(tagRepository.findByHuddleAndId(huddle, tagId));
            }
            blogPost.setTags(tags);
        }

        if (request.isPublish() && !blogPost.isPublished()) {
            blogPost.setPublishedOn(new DateTime());
            if (blogPost.getAuthor() == null) {
                blogPost.setAuthor(securityHelper.getCurrentMember());
            }
        }

        blogPost = blogPostRepository.save(blogPost);
        log.info("BlogPost '{}' with ID {} was updated", blogPost.getTitle(), blogPost.getId());
        return blogPost;
    }

    @Override
    public File getBlogPostFeatureImage(String blogSlug, String postSlug) {
        BlogPost post = blogPostRepository.findByHuddleAndBlogSlugAndSlug(huddleService.getHuddle(), blogSlug, postSlug);
        if (post != null) {
            return fileStore.getFile(getFeatureImagePath(post.getId()));
        } else {
            return null;
        }
    }

    @Override
    public File saveAttachment(long blogPostId, String fileName, byte[] data) throws IOException {
        return fileStore.createFile(getAttachmentPath(blogPostId, fileName), data);
    }

    @Override
    public File getAttachment(long blogPostId, String fileName) throws IOException {
        String path = getAttachmentPath(blogPostId, fileName);
        log.debug("Downloading file attachment: {}", path);
        return fileStore.getFile(path);
    }

    @Override
    public void deleteBlogPost(long blogPostId) {
        blogPostRepository.delete(blogPostId);
        log.info("Deleted blog post with ID {}", blogPostId);

        try {
            fileStore.delete(getBlogPostFolder(blogPostId));
        } catch (IOException e) {
            log.error("Failed to delete blog post directory for: " + blogPostId, e);
        }
    }

    @Override
    public BlogPostComment postComment(PostCommentRequest request)
            throws CommentsNotAllowedException, CommentsClosedException, CommentsByMembersOnlyException {

        Huddle huddle = huddleService.getHuddle();
        BlogPost blogPost = blogPostRepository.findByHuddleAndId(huddle, request.getBlogPostId());

        if (!blogPost.isCommentsOpen()) {
            throw new CommentsClosedException("Comments have been closed for blog '" + blogPost.getSlug() + "'");
        }

        Blog blog = blogPost.getBlog();

        Member member = null;

        switch (blog.getAllowComments()) {
            case members:
                if (member == null) {
                    throw new CommentsByMembersOnlyException("Only members can comment on blog '" + blog.getSlug() + "'");
                }
                break;
            case none:
                throw new CommentsNotAllowedException("Comments are not allowed for blog '" + blog.getSlug() + "'");
        }

        boolean approved;
        switch (blog.getRequireCommentApproval()) {
            case none:
                approved = true;
                break;
            case anonymous:
                approved = (member != null);
                break;
            case all:
                approved = false;
                break;
            default:
                throw new IllegalArgumentException("Unsupported comment approval type: " + blog.getRequireCommentApproval());
        }

        BlogPostComment comment = blogPostCommentRepository.save(
                new BlogPostComment(huddle, blogPost, member, DateTime.now(),
                request.getDisplayName(), request.getComment(), approved));
        log.info("New comment added to blog post '{}' with ID {}", blogPost.getSlug(), comment.getId());
        return comment;
    }

    @Override
    public Page<? extends BlogPostComment> searchBlogPostComments(SearchBlogPostCommentRequest request) {
        Huddle huddle = huddleService.getHuddle();
        return blogPostCommentRepository.findAll(BlogPostCommentSpecification.search(huddle, request),
                new PageRequest(request.getPage(), request.getSize()));
    }

    private String getFeatureImagePath(long blogPostId) {
        return getAttachmentFolder(blogPostId) + "/feature";
    }

    private String getAttachmentPath(long blogPostId, String fileName) {
        return getAttachmentFolder(blogPostId) + "/" + fileName;
    }

    private String getAttachmentFolder(long blogPostId) {
        return getBlogPostFolder(blogPostId) + "/attach";
    }

    private String getBlogPostFolder(long blogPostId) {
        return "/attachments/blog/post/" + blogPostId;
    }



}
