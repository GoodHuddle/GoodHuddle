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
import com.goodhuddle.huddle.service.request.payment.SearchPaymentsRequest;
import com.goodhuddle.huddle.web.builder.payment.PaymentDetailBuilder;
import com.goodhuddle.huddle.web.builder.payment.PaymentRowBuilder;
import com.goodhuddle.huddle.web.dto.payment.PaymentDetail;
import com.goodhuddle.huddle.web.dto.payment.PaymentRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentApiController {

    private static final Logger log = LoggerFactory.getLogger(PaymentApiController.class);

    private final PaymentService paymentService;
    private final PaymentRowBuilder paymentRowBuilder;
    private final PaymentDetailBuilder paymentDetailBuilder;

    @Autowired
    public PaymentApiController(PaymentService paymentService,
                                PaymentRowBuilder paymentRowBuilder,
                                PaymentDetailBuilder paymentDetailBuilder) {
        this.paymentService = paymentService;
        this.paymentRowBuilder = paymentRowBuilder;
        this.paymentDetailBuilder = paymentDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PaymentRow> searchPayments(SearchPaymentsRequest request) {
        log.debug("Searching payments: " + request);
        return paymentRowBuilder.build(paymentService.searchPayments(request));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public PaymentDetail getPayment(@PathVariable long id) {
        return paymentDetailBuilder.build(paymentService.getPayment(id));
    }
}
