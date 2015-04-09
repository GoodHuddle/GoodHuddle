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

package com.goodhuddle.huddle.web.dto.huddle.plan;

public class HuddlePlanDetail extends HuddlePlanRef {

    private String description;
    private boolean defaultPlan;
    private boolean customerSelectable;

    public HuddlePlanDetail(long id, String code, String name, boolean defaultPlan, String description,
                            int costInCents, boolean customerSelectable) {
        super(id, code, name, costInCents);
        this.defaultPlan = defaultPlan;
        this.description = description;
        this.customerSelectable = customerSelectable;
    }

    public boolean isDefaultPlan() {
        return defaultPlan;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCustomerSelectable() {
        return customerSelectable;
    }
}

