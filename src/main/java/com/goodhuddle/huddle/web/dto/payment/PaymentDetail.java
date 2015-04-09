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

package com.goodhuddle.huddle.web.dto.payment;

import com.goodhuddle.huddle.domain.PaymentType;
import com.goodhuddle.huddle.web.dto.member.MemberRef;
import com.goodhuddle.huddle.web.dto.subscription.SubscriptionRef;
import org.joda.time.DateTime;

public class PaymentDetail extends PaymentRef {

    private String stripePaymentId;
    private SubscriptionRef subscription;
    private String description;
    private MemberRef paidBy;
    private DateTime paidOn;

    public PaymentDetail(long id, PaymentType type, String stripePaymentId, SubscriptionRef subscription,
                         int amountInCents, String description, MemberRef paidBy, DateTime paidOn) {
        super(id, type, amountInCents);
        this.stripePaymentId = stripePaymentId;
        this.subscription = subscription;
        this.description = description;
        this.paidBy = paidBy;
        this.paidOn = paidOn;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public SubscriptionRef getSubscription() {
        return subscription;
    }

    public String getDescription() {
        return description;
    }

    public MemberRef getPaidBy() {
        return paidBy;
    }

    public DateTime getPaidOn() {
        return paidOn;
    }
}
