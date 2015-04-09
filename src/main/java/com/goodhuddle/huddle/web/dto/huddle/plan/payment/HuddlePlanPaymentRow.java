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

package com.goodhuddle.huddle.web.dto.huddle.plan.payment;

import org.joda.time.DateTime;

public class HuddlePlanPaymentRow extends HuddlePlanPaymentRef {

    private String stripePaymentId;
    private DateTime paidOn;

    public HuddlePlanPaymentRow(long id, String stripePaymentId, int amountInCents, DateTime paidOn) {
        super(id, amountInCents);
        this.stripePaymentId = stripePaymentId;
        this.paidOn = paidOn;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public DateTime getPaidOn() {
        return paidOn;
    }
}
