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
@Table(name = "petition_signature")
@SequenceGenerator(name = "sequence_generator", sequenceName = "petition_signature_id_seq")
public class PetitionSignature extends AbstractHuddleObject<Long> {

    @ManyToOne
    @JoinColumn(name="petition_id")
    private Petition petition;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name="created_by")
    private Member createdBy;

    @Column(name="created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    public PetitionSignature() {
    }

    public PetitionSignature(Petition petition, Member member, String subject, String content, Member createdBy) {
        this(petition.getHuddle(), petition, member, subject, content, createdBy);
    }

    protected PetitionSignature(Huddle huddle, Petition petition, Member member, String subject, String message, Member createdBy) {
        super(huddle);
        this.petition = petition;
        this.member = member;
        this.subject = subject;
        this.message = message;
        this.createdBy = createdBy;
        this.createdOn = new DateTime();
    }

    public Petition getPetition() {
        return petition;
    }

    public void setPetition(Petition petition) {
        this.petition = petition;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
