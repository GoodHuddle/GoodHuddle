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

package com.goodhuddle.huddle.web.api;

import com.goodhuddle.huddle.service.PaymentService;
import com.goodhuddle.huddle.service.exception.SubscriptionErrorException;
import com.goodhuddle.huddle.service.request.payment.SearchSubscriptionsRequest;
import com.goodhuddle.huddle.web.builder.subscription.SubscriptionDetailBuilder;
import com.goodhuddle.huddle.web.builder.subscription.SubscriptionRowBuilder;
import com.goodhuddle.huddle.web.dto.subscription.SubscriptionDetail;
import com.goodhuddle.huddle.web.dto.subscription.SubscriptionRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionApiController {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionApiController.class);

    private final PaymentService paymentService;
    private final SubscriptionRowBuilder subscriptionRowBuilder;
    private final SubscriptionDetailBuilder subscriptionDetailBuilder;

    @Autowired
    public SubscriptionApiController(PaymentService paymentService,
                                     SubscriptionRowBuilder subscriptionRowBuilder,
                                     SubscriptionDetailBuilder subscriptionDetailBuilder) {
        this.paymentService = paymentService;
        this.subscriptionRowBuilder = subscriptionRowBuilder;
        this.subscriptionDetailBuilder = subscriptionDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<SubscriptionRow> searchPayments(SearchSubscriptionsRequest request) {
        log.debug("Searching subscriptions: " + request);
        return subscriptionRowBuilder.build(paymentService.searchSubscriptions(request));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public SubscriptionDetail getSubscription(@PathVariable long id) {
        return subscriptionDetailBuilder.build(paymentService.getSubscription(id));
    }

    @RequestMapping(value = "{id}/cancel", method = RequestMethod.PUT)
    public SubscriptionDetail cancelSubscription(@PathVariable long id) throws SubscriptionErrorException {
        return subscriptionDetailBuilder.build(paymentService.cancelSubscription(id));
    }
}
