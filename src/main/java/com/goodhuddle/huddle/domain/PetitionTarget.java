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
@Table(name = "petition_target")
@SequenceGenerator(name = "sequence_generator", sequenceName = "petition_target_id_seq")
public class PetitionTarget extends AbstractHuddleObject<Long> {

    @ManyToOne
    @JoinColumn(name="petition_id")
    private Petition petition;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;


    public PetitionTarget() {
    }

    public PetitionTarget(Petition petition, String name, String email) {
        super(petition.getHuddle());
        this.petition = petition;
        this.name = name;
        this.email = email;
    }

    public Petition getPetition() {
        return petition;
    }

    public void setPetition(Petition petition) {
        this.petition = petition;
    }

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
