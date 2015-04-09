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

import java.math.BigDecimal;

public class SubscriptionRef {

    private long id;
    private SubscriptionStatus status;
    private PaymentType paymentType;
    private SubscriptionFrequency frequency;
    private int amountInCents;

    public SubscriptionRef(long id, SubscriptionStatus status, PaymentType paymentType,
                           SubscriptionFrequency frequency, int amountInCents) {
        this.id = id;
        this.status = status;
        this.paymentType = paymentType;
        this.frequency = frequency;
        this.amountInCents = amountInCents;
    }

    public long getId() {
        return id;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public SubscriptionFrequency getFrequency() {
        return frequency;
    }

    public int getAmountInCents() {
        return amountInCents;
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amountInCents).divide(new BigDecimal(100));
    }
}
