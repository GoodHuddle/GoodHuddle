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

package com.goodhuddle.huddle.service.request.blog;

import com.goodhuddle.huddle.domain.PageContent;
import org.hibernate.validator.constraints.NotBlank;

public abstract class AbstractBlogPostDetailRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String slug;

    private String layout;

    private String blurb;

    private String featureImageId;

    private PageContent content;

    private boolean publish;


    public AbstractBlogPostDetailRequest() {
    }

    protected AbstractBlogPostDetailRequest(String title, String slug, String layout, PageContent content) {
        this.title = title;
        this.slug = slug;
        this.layout = layout;
        this.content = content;
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

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getFeatureImageId() {
        return featureImageId;
    }

    public void setFeatureImageId(String featureImageId) {
        this.featureImageId = featureImageId;
    }

    public PageContent getContent() {
        return content;
    }

    public void setContent(PageContent content) {
        this.content = content;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }
}
