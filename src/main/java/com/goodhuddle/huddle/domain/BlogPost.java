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

package com.goodhuddle.huddle.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "blog_post")
@SequenceGenerator(name = "sequence_generator", sequenceName = "blog_post_id_seq")
public class BlogPost extends AbstractHuddleObject<Long> implements WebsiteEntity {

    @ManyToOne
    @JoinColumn(name="blog_id")
    private Blog blog;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "layout")
    private String layout;

    @ManyToOne
    @JoinColumn(name="created_by")
    private Member createdBy;

    @Column(name="created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @Column(name="published_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime publishedOn;

    @ManyToOne
    @JoinColumn(name="author_id")
    private Member author;

    @Column(name = "blurb")
    private String blurb;

    @Column(name = "feat_img_available")
    private boolean featureImageAvailable;

    @Column(name = "comments_open")
    private boolean commentsOpen;

    @Column(name = "content")
    @Type(type="com.goodhuddle.huddle.repository.PageContentUserType")
    private PageContent content;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "blog_post_tag",
            joinColumns = {@JoinColumn(name = "blog_post_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private List<Tag> tags;

    @OneToMany(mappedBy = "blogPost", fetch = FetchType.LAZY)
    @OrderBy("createdOn DESC")
    private List<BlogPostComment> comments;

    public BlogPost() {
    }

    public BlogPost(Blog blog, String title, String slug, String layout, Member createdBy,
                    String blurb, boolean featureImageAvailable, PageContent content) {
        super(blog.getHuddle());
        this.blog = blog;
        this.title = title;
        this.slug = slug;
        this.layout = layout;
        this.createdBy = createdBy;
        this.blurb = blurb;
        this.content = content;
        this.createdOn = new DateTime();
        this.featureImageAvailable = featureImageAvailable;
    }

    public void update(String title, String slug, String layout,
                       String blurb, PageContent content, boolean commentsOpen) {
        this.title = title;
        this.slug = slug;
        this.layout = layout;
        this.blurb = blurb;
        this.content = content;
        this.commentsOpen = commentsOpen;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Member getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Member createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public DateTime getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(DateTime publishedOn) {
        this.publishedOn = publishedOn;
    }

    public boolean isPublished() {
        return publishedOn != null && publishedOn.isBeforeNow();
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public boolean isFeatureImageAvailable() {
        return featureImageAvailable;
    }

    public void setFeatureImageAvailable(boolean featureImageAvailable) {
        this.featureImageAvailable = featureImageAvailable;
    }

    public PageContent getContent() {
        return content;
    }

    public void setContent(PageContent content) {
        this.content = content;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<BlogPostComment> getComments() {
        return comments;
    }

    public void setComments(List<BlogPostComment> comments) {
        this.comments = comments;
    }

    public boolean isCommentsOpen() {
        return commentsOpen;
    }

    public void setCommentsOpen(boolean commentsOpen) {
        this.commentsOpen = commentsOpen;
    }

    public String getUrl() {
        return blog.getUrl() + "/" + slug;
    }

    public String getFeatureImageUrl() {
        return isFeatureImageAvailable() ? getUrl() + "/feature-image" : null;
    }
}
