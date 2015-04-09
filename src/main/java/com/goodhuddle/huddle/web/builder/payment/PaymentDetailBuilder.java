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

package com.goodhuddle.huddle.web.builder.payment;

import com.goodhuddle.huddle.domain.Payment;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.builder.member.MemberRefBuilder;
import com.goodhuddle.huddle.web.builder.subscription.SubscriptionRefBuilder;
import com.goodhuddle.huddle.web.dto.payment.PaymentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailBuilder extends AbstractDTOBuilder<PaymentDetail, Payment> {

    private final SubscriptionRefBuilder subscriptionRefBuilder;
    private final MemberRefBuilder memberRefBuilder;

    @Autowired
    public PaymentDetailBuilder(SubscriptionRefBuilder subscriptionRefBuilder,
                                MemberRefBuilder memberRefBuilder) {
        this.subscriptionRefBuilder = subscriptionRefBuilder;
        this.memberRefBuilder = memberRefBuilder;
    }

    @Override
    protected PaymentDetail buildNullSafe(Payment entity) {
        return new PaymentDetail(
                entity.getId(),
                entity.getType(),
                entity.getStripePaymentId(),
                subscriptionRefBuilder.build(entity.getSubscription()),
                entity.getAmountInCents(),
                entity.getDescription(),
                memberRefBuilder.build(entity.getPaidBy()),
                entity.getPaidOn());
    }
}
