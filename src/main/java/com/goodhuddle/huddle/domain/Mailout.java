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
@Table(name = "mailout")
@SequenceGenerator(name = "sequence_generator", sequenceName = "mailout_id_seq")
public class Mailout extends AbstractHuddleObject<Long> {

    public enum Status { created, generated, sending, complete }

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name="created_by")
    private Member createdBy;

    @Column(name="created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @ManyToOne
    @JoinColumn(name="generated_by")
    private Member generatedBy;

    @Column(name="generated_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime generatedOn;

    @ManyToOne
    @JoinColumn(name="sent_by")
    private Member sentBy;

    @Column(name="sent_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime sentOn;

    public Mailout() {
    }

    public Mailout(Huddle huddle, String name, String description, String subject, String content, Member createdBy) {
        super(huddle);
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.content = content;
        this.createdBy = createdBy;

        this.status = Status.created;
        this.createdOn = new DateTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Member getGeneratedBy() {
        return generatedBy;
    }

    public void setGenerated(Member generatedBy) {
        this.generatedBy = generatedBy;
        this.generatedOn = new DateTime();
        this.status = Status.generated;
    }

    public void setGeneratedBy(Member generatedBy) {
        this.generatedBy = generatedBy;
    }

    public DateTime getGeneratedOn() {
        return generatedOn;
    }

    public void setGeneratedOn(DateTime generatedOn) {
        this.generatedOn = generatedOn;
    }

    public void setSending(Member sentBy) {
        this.sentBy = sentBy;
        this.sentOn = new DateTime();
        this.status = Status.sending;
    }

    public Member getSentBy() {
        return sentBy;
    }

    public void setSentBy(Member sentBy) {
        this.sentBy = sentBy;
    }

    public DateTime getSentOn() {
        return sentOn;
    }

    public void setSentOn(DateTime sentOn) {
        this.sentOn = sentOn;
    }
}
