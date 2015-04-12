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

@Entity
@Table(name = "page_revision")
@SequenceGenerator(name = "sequence_generator", sequenceName = "page_revision_id_seq")
public class PageRevision extends AbstractHuddleObject<Long> {

    @ManyToOne
    @JoinColumn(name="page_id")
    private Page page;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "layout")
    private String layout;

    @Column(name = "content")
    @Type(type="com.goodhuddle.huddle.repository.PageContentUserType")
    private PageContent content;

    @Column(name="created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @ManyToOne
    @JoinColumn(name="created_by")
    private Member createdBy;

    public PageRevision() {
    }

    public PageRevision(Page page, Member createdBy) {
        super(page.getHuddle());
        this.page = page;
        this.title = page.getTitle();
        this.slug = page.getSlug();
        this.layout = page.getLayout();
        this.content = page.getContent();
        this.createdBy = createdBy;
        this.createdOn = new DateTime();
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
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

    public PageContent getContent() {
        return content;
    }

    public void setContent(PageContent content) {
        this.content = content;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Member getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Member createdBy) {
        this.createdBy = createdBy;
    }
}
