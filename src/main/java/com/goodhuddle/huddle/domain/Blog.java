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

import javax.persistence.*;

@Entity
@Table(name = "blog")
@SequenceGenerator(name = "sequence_generator", sequenceName = "blog_id_seq")
public class Blog extends AbstractHuddleObject<Long> implements WebsiteEntity {

    @ManyToOne
    @JoinColumn(name="menu_item_id")
    private MenuItem menuItem;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "layout")
    private String layout;

    @Column(name = "default_post_layout")
    private String defaultPostLayout;

    @Column(name = "allow_comments")
    @Enumerated(EnumType.STRING)
    private AllowCommentsType allowComments;

    @Column(name = "require_comment_approval")
    @Enumerated(EnumType.STRING)
    private RequireCommentApprovalType requireCommentApproval;

    public Blog() {
    }

    public Blog(Huddle huddle, String title, String slug, String layout, String defaultPostLayout,
                AllowCommentsType allowComments, RequireCommentApprovalType requireCommentApproval) {
        super(huddle);
        this.title = title;
        this.slug = slug;
        this.layout = layout;
        this.defaultPostLayout = defaultPostLayout;
        this.allowComments = allowComments;
        this.requireCommentApproval = requireCommentApproval;
    }

    public void update(String title, String slug, String layout, String defaultPostLayout,
                       AllowCommentsType allowComments, RequireCommentApprovalType requireCommentApproval) {
        this.title = title;
        this.slug = slug;
        this.layout = layout;
        this.defaultPostLayout = defaultPostLayout;
        this.allowComments = allowComments;
        this.requireCommentApproval = requireCommentApproval;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
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

    public String getDefaultPostLayout() {
        return defaultPostLayout;
    }

    public void setDefaultPostLayout(String defaultPostLayout) {
        this.defaultPostLayout = defaultPostLayout;
    }

    public AllowCommentsType getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(AllowCommentsType allowComments) {
        this.allowComments = allowComments;
    }

    public RequireCommentApprovalType getRequireCommentApproval() {
        return requireCommentApproval;
    }

    public void setRequireCommentApproval(RequireCommentApprovalType requireCommentApproval) {
        this.requireCommentApproval = requireCommentApproval;
    }

    public String getUrl() {
        return "/blog/" + slug;
    }
}
