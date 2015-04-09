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

package com.goodhuddle.huddle.service;

import com.goodhuddle.huddle.domain.Payment;
import com.goodhuddle.huddle.domain.PaymentSettings;
import com.goodhuddle.huddle.domain.Subscription;
import com.goodhuddle.huddle.service.exception.*;
import com.goodhuddle.huddle.service.request.payment.CreateSubscriptionRequest;
import com.goodhuddle.huddle.service.request.payment.ProcessPaymentRequest;
import com.goodhuddle.huddle.service.request.payment.SearchPaymentsRequest;
import com.goodhuddle.huddle.service.request.payment.SearchSubscriptionsRequest;
import com.goodhuddle.huddle.service.request.payment.settings.UpdatePaymentSettingsRequest;
import com.stripe.model.Event;
import org.springframework.data.domain.Page;

public interface PaymentService {

    PaymentSettings getPaymentSettings();

    String getHuddlePublishableKey();

    void checkChangeSettingsAllowed()
            throws SubscriptionsExistException;

    PaymentSettings updatePaymentSettings(UpdatePaymentSettingsRequest request) throws SubscriptionsExistException;


    Payment getPayment(long id);

    Page<? extends Payment> searchPayments(SearchPaymentsRequest request);

    Payment processPayment(ProcessPaymentRequest request)
            throws PaymentFailedException, PaymentsNotSetupException;


    Subscription getSubscription(long id);

    Subscription getSubscriptionByStripeCustomerId(String stripeCustomerId);

    Page<? extends Subscription> searchSubscriptions(SearchSubscriptionsRequest request);

    Subscription createSubscription(CreateSubscriptionRequest request)
            throws PaymentFailedException, PaymentsNotSetupException;

    Subscription cancelSubscription(long id) throws SubscriptionErrorException;


    void handleStripeEvent(Event event) throws WebHookException;

}
