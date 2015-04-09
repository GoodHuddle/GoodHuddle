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

package com.goodhuddle.huddle.web.dto.subscription;

import com.goodhuddle.huddle.domain.PaymentType;
import com.goodhuddle.huddle.domain.SubscriptionFrequency;
import com.goodhuddle.huddle.domain.SubscriptionStatus;
import com.goodhuddle.huddle.web.dto.member.MemberRef;
import org.joda.time.DateTime;

public class SubscriptionRow extends SubscriptionRef {

    private String stripeSubscriptionId;
    private String description;
    private MemberRef member;
    private DateTime createdOn;

    public SubscriptionRow(long id, SubscriptionStatus status, PaymentType paymentType, SubscriptionFrequency frequency,
                           String stripeSubscriptionId, int amountInCents,
                           String description, MemberRef member, DateTime createdOn) {
        super(id, status, paymentType, frequency, amountInCents);
        this.stripeSubscriptionId = stripeSubscriptionId;
        this.description = description;
        this.member = member;
        this.createdOn = createdOn;
    }

    public String getStripePaymentId() {
        return stripeSubscriptionId;
    }

    public String getDescription() {
        return description;
    }

    public MemberRef getMember() {
        return member;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }
}
