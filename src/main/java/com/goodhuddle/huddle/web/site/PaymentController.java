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

package com.goodhuddle.huddle.web.site;

import com.goodhuddle.huddle.domain.Payment;
import com.goodhuddle.huddle.domain.Subscription;
import com.goodhuddle.huddle.service.PaymentService;
import com.goodhuddle.huddle.service.exception.PaymentFailedException;
import com.goodhuddle.huddle.service.exception.PaymentsNotSetupException;
import com.goodhuddle.huddle.service.request.payment.CreateSubscriptionRequest;
import com.goodhuddle.huddle.service.request.payment.ProcessPaymentRequest;
import com.goodhuddle.huddle.web.builder.payment.PaymentDetailBuilder;
import com.goodhuddle.huddle.web.builder.subscription.SubscriptionDetailBuilder;
import com.goodhuddle.huddle.web.dto.payment.PaymentDetail;
import com.goodhuddle.huddle.web.dto.subscription.SubscriptionDetail;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final PaymentDetailBuilder paymentDetailBuilder;
    private final SubscriptionDetailBuilder subscriptionDetailBuilder;

    @Autowired
    public PaymentController(PaymentService paymentService,
                             PaymentDetailBuilder paymentDetailBuilder,
                             SubscriptionDetailBuilder subscriptionDetailBuilder) {

        this.paymentService = paymentService;
        this.paymentDetailBuilder = paymentDetailBuilder;
        this.subscriptionDetailBuilder = subscriptionDetailBuilder;
    }

    @RequestMapping(value = "subscribe.do", method = RequestMethod.POST)
    @ResponseBody
    public SubscriptionDetail createSubscriptionApi(@Valid @RequestBody CreateSubscriptionRequest request)
            throws PaymentFailedException, PaymentsNotSetupException {

        log.info("Processing donation for ${} from '{}'", request.getAmountInCents() / 100, request.getEmail());

        if (StringUtils.isBlank(request.getCurrency())) {
            request.setCurrency("aud");
        }

        Subscription subscription = paymentService.createSubscription(request);
        return subscriptionDetailBuilder.build(subscription);
    }

    @RequestMapping(value = "pay.do", method = RequestMethod.POST)
    @ResponseBody
    public PaymentDetail makePaymentApi(@Valid @RequestBody ProcessPaymentRequest request)
            throws PaymentFailedException, PaymentsNotSetupException {

        log.info("Processing donation for ${} from '{}'", request.getAmountInCents() / 100, request.getEmail());

        if (StringUtils.isBlank(request.getCurrency())) {
            request.setCurrency("aud");
        }

        Payment payment = paymentService.processPayment(request);
        return paymentDetailBuilder.build(payment);
    }
}
