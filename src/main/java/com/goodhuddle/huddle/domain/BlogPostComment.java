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
@Table(name = "blog_post_comment")
@SequenceGenerator(name = "sequence_generator", sequenceName = "blog_post_comment_id_seq")
public class BlogPostComment extends AbstractHuddleObject<Long> {

    @ManyToOne
    @JoinColumn(name="blog_post_id")
    private BlogPost blogPost;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(name = "created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "comment")
    private String comment;

    @Column(name = "approved")
    private boolean approved;

    public BlogPostComment() {
    }

    public BlogPostComment(Huddle huddle, BlogPost blogPost, Member member,
                           DateTime createdOn, String displayName, String comment, boolean approved) {
        super(huddle);
        this.blogPost = blogPost;
        this.member = member;
        this.createdOn = createdOn;
        this.displayName = displayName;
        this.comment = comment;
        this.approved = approved;
    }

    public BlogPost getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
