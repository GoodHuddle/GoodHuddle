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
@Table(name = "block_id_mapping")
@SequenceGenerator(name = "sequence_generator", sequenceName = "block_id_mapping_id_seq")
public class BlockIdMapping extends AbstractDomainObject<Long> {

    @ManyToOne
    @JoinColumn(name="page_id")
    private Page page;

    public BlockIdMapping() {
    }

    public BlockIdMapping(Page page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
