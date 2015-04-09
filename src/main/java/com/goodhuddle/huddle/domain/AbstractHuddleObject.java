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

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class AbstractHuddleObject <IdType extends Serializable> extends AbstractDomainObject<IdType> {

    @ManyToOne
    @JoinColumn(name="huddle_id")
    private Huddle huddle;

    public AbstractHuddleObject() {
    }

    public AbstractHuddleObject(Huddle huddle) {
        this.huddle = huddle;
    }

    public Huddle getHuddle() {
        return huddle;
    }

    public void setHuddle(Huddle huddle) {
        this.huddle = huddle;
    }
}
