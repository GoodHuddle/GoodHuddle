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

package com.goodhuddle.huddle.service.request.petition;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public abstract class AbstractPetitionRequest {

    @NotBlank
    private String name;

    private String description;
    private String subject;
    private String content;
    private String petitionEmailTemplate;
    private String thankyouEmailTemplate;
    private String adminEmailAddresses;
    private String adminEmailTemplate;
    private List<Target> targets;

    protected AbstractPetitionRequest() {
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

    public void setAdminEmailAddresses(String adminEmailAddresses) {
        this.adminEmailAddresses = adminEmailAddresses;
    }

    public String getAdminEmailTemplate() {
        return adminEmailTemplate;
    }

    public void setAdminEmailTemplate(String adminEmailTemplate) {
        this.adminEmailTemplate = adminEmailTemplate;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

    //-------------------------------------------------------------------------

    public static final class Target {

        private String name;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
