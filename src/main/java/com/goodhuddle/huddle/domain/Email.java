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
@Table(name = "email")
@SequenceGenerator(name = "sequence_generator", sequenceName = "email_id_seq")
public class Email extends AbstractHuddleObject<Long> {

    public enum Status { ready, sent, queued, rejected, invalid, delivered, bounced, error }

    @ManyToOne
    @JoinColumn(name="mailout_id")
    private Mailout mailout;

    @ManyToOne
    @JoinColumn(name="recipient_id")
    private Member recipient;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "error")
    private String error;

    @ManyToOne
    @JoinColumn(name="created_by")
    private Member createdBy;

    @Column(name="created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @Column(name="sent_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime sentOn;

    public Email() {
    }

    public Email(Huddle huddle, Member recipient, String subject, String content, Member createdBy) {
        this(huddle, null, recipient, subject, content, createdBy);
    }

    public Email(Mailout mailout, Member recipient, String subject, String content, Member createdBy) {
        this(mailout.getHuddle(), mailout, recipient, subject, content, createdBy);
    }

    protected Email(Huddle huddle, Mailout mailout, Member recipient, String subject, String content, Member createdBy) {
        super(huddle);
        this.mailout = mailout;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        this.createdBy = createdBy;
        this.status = Status.ready;
        this.createdOn = new DateTime();
    }

    public Mailout getMailout() {
        return mailout;
    }

    public void setMailout(Mailout mailout) {
        this.mailout = mailout;
    }

    public Member getRecipient() {
        return recipient;
    }

    public void setRecipient(Member recipient) {
        this.recipient = recipient;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public DateTime getSentOn() {
        return sentOn;
    }

    public void setSentOn(DateTime sentOn) {
        this.sentOn = sentOn;
    }
}
