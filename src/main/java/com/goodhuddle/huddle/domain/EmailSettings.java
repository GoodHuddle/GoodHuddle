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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "email_settings")
@SequenceGenerator(name = "sequence_generator", sequenceName = "email_settings_id_seq")
public class EmailSettings extends AbstractHuddleObject<Long> {

    @Column(name = "send_from_address")
    private String sendFromAddress;

    @Column(name = "send_from_name")
    private String sendFromName;

    @Column(name = "mandrill_api_key")
    private String mandrillApiKey;

    public EmailSettings() {
    }

    public EmailSettings(Huddle huddle) {
        super(huddle);
    }

    public String getSendFromAddress() {
        return sendFromAddress;
    }

    public void setSendFromAddress(String sendFromAddress) {
        this.sendFromAddress = sendFromAddress;
    }

    public String getSendFromName() {
        return sendFromName;
    }

    public void setSendFromName(String sendFromName) {
        this.sendFromName = sendFromName;
    }

    public String getMandrillApiKey() {
        return mandrillApiKey;
    }

    public void setMandrillApiKey(String mandrillApiKey) {
        this.mandrillApiKey = mandrillApiKey;
    }
}
