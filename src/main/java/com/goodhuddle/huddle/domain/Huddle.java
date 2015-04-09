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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "huddle")
@SequenceGenerator(name = "sequence_generator", sequenceName = "huddle_id_seq")
public class Huddle extends AbstractDomainObject<Long> {

    @Column(name = "slug")
    private String slug;

    @Column(name = "name")
    private String name;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdOn;

    @Column(name = "setup_wizard_complete")
    private boolean setupWizardComplete;

    public Huddle() {
    }

    public Huddle(String slug, String name, String baseUrl, String description) {
        this.slug = slug;
        this.name = name;
        this.baseUrl = baseUrl;
        this.description = description;
        this.createdOn = new DateTime();
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public boolean isSetupWizardComplete() {
        return setupWizardComplete;
    }

    public void setSetupWizardComplete(boolean setupWizardComplete) {
        this.setupWizardComplete = setupWizardComplete;
    }
}
