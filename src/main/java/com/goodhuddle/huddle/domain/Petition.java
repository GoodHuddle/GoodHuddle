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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "petition")
@SequenceGenerator(name = "sequence_generator", sequenceName = "petition_id_seq")
public class Petition extends AbstractHuddleObject<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @Column(name = "petition_email_template")
    private String petitionEmailTemplate;

    @Column(name = "thankyou_email_template")
    private String thankyouEmailTemplate;

    @Column(name = "admin_email_template")
    private String adminEmailTemplate;

    @Column(name = "admin_email_addresses")
    private String adminEmailAddresses;

    @ManyToOne
    @JoinColumn(name="created_by")
    private Member createdBy;

    @Column(name="created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @OneToMany(mappedBy = "petition", fetch = FetchType.LAZY)
    private List<PetitionTarget> targets;

    public Petition() {
    }

    public Petition(Huddle huddle,
                    String name,
                    String description,
                    String subject,
                    String content,
                    String petitionEmailTemplate,
                    String thankyouEmailTemplate,
                    String adminEmailAddresses,
                    String adminEmailTemplate,
                    Member createdBy) {
        super(huddle);
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.content = content;
        this.petitionEmailTemplate = petitionEmailTemplate;
        this.thankyouEmailTemplate = thankyouEmailTemplate;
        this.adminEmailAddresses = adminEmailAddresses;
        this.adminEmailTemplate = adminEmailTemplate;
        this.createdBy = createdBy;
        this.createdOn = DateTime.now();
    }


    public void update(String name,
                       String description,
                       String subject,
                       String content,
                       String petitionEmailTemplate,
                       String thankyouEmailTemplate,
                       String adminEmailAddresses,
                       String adminEmailTemplate) {
        this.name = name;
        this.description = description;
        this.subject = subject;
        this.content = content;
        this.petitionEmailTemplate = petitionEmailTemplate;
        this.thankyouEmailTemplate = thankyouEmailTemplate;
        this.adminEmailAddresses = adminEmailAddresses;
        this.adminEmailTemplate = adminEmailTemplate;
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

    public String getPetitionEmailTemplate() {
        return petitionEmailTemplate;
    }

    public void setPetitionEmailTemplate(String petitionEmailTemplate) {
        this.petitionEmailTemplate = petitionEmailTemplate;
    }

    public String getThankyouEmailTemplate() {
        return thankyouEmailTemplate;
    }

    public void setThankyouEmailTemplate(String thankyouEmailTemplate) {
        this.thankyouEmailTemplate = thankyouEmailTemplate;
    }

    public String getAdminEmailAddresses() {
        return adminEmailAddresses;
    }

    public String[] getAdminEmailAddressesArray() {
        return StringUtils.isNotBlank(adminEmailAddresses) ? adminEmailAddresses.split(";") : null;
    }

    public void setAdminEmailAddresses(String adminEmailAddresses) {
        this.adminEmailAddresses = adminEmailAddresses;
    }

    public String getAdminEmailTemplate() {
        return adminEmailTemplate;
    }

    public void setAdminEmailTemplate(String adminEmailTemplate) {
        this.adminEmailTemplate = adminEmailTemplate;
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

    public List<PetitionTarget> getTargets() {
        return targets;
    }

    public void setTargets(List<PetitionTarget> targets) {
        this.targets = targets;
    }
}
