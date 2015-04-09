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

package com.goodhuddle.huddle.web.dto.blog;

import com.goodhuddle.huddle.web.dto.member.MemberRef;
import org.joda.time.DateTime;

public class BlogPostRef {

    private long id;
    private long blogId;
    private String title;
    private String slug;
    private String url;
    private String blurb;
    private String featureImageUrl;
    private MemberRef author;
    private boolean published;
    private DateTime publishedOn;

    public BlogPostRef(long id, long blogId, String title, String slug, String url,
                       String blurb, String featureImageUrl,
                       MemberRef author, boolean published, DateTime publishedOn) {
        this.id = id;
        this.blogId = blogId;
        this.title = title;
        this.slug = slug;
        this.url = url;
        this.blurb = blurb;
        this.featureImageUrl = featureImageUrl;
        this.author = author;
        this.published = published;
        this.publishedOn = publishedOn;
    }

    public long getId() {
        return id;
    }

    public long getBlogId() {
        return blogId;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getUrl() {
        return url;
    }

    public String getBlurb() {
        return blurb;
    }

    public String getFeatureImageUrl() {
        return featureImageUrl;
    }

    public MemberRef getAuthor() {
        return author;
    }

    public void setAuthor(MemberRef author) {
        this.author = author;
    }

    public boolean isPublished() {
        return published;
    }

    public DateTime getPublishedOn() {
        return publishedOn;
    }
}
