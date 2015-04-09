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
import com.goodhuddle.huddle.service.exception.SubscriptionsExistException;
import com.goodhuddle.huddle.service.request.payment.settings.UpdatePaymentSettingsRequest;
import com.goodhuddle.huddle.web.builder.payment.PaymentSettingsDetailBuilder;
import com.goodhuddle.huddle.web.dto.payment.PaymentSettingsDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment/settings")
public class PaymentSettingsApiController {

    private static final Logger log = LoggerFactory.getLogger(PaymentSettingsApiController.class);

    private final PaymentService paymentService;
    private final PaymentSettingsDetailBuilder paymentSettingsDetailBuilder;

    @Autowired
    public PaymentSettingsApiController(PaymentService paymentService,
                                        PaymentSettingsDetailBuilder paymentSettingsDetailBuilder) {
        this.paymentService = paymentService;
        this.paymentSettingsDetailBuilder = paymentSettingsDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public PaymentSettingsDetail getPaymentSettings() {
        return paymentSettingsDetailBuilder.build(paymentService.getPaymentSettings());
    }

    @RequestMapping(value = "allowed", method = RequestMethod.GET)
    public void checkChangeSettingsAllowed() throws SubscriptionsExistException {
        paymentService.checkChangeSettingsAllowed();
    }

    @RequestMapping(method = RequestMethod.POST)
    public PaymentSettingsDetail updatePaymentSettings(@RequestBody UpdatePaymentSettingsRequest request) throws SubscriptionsExistException {
        return paymentSettingsDetailBuilder.build(paymentService.updatePaymentSettings(request));
    }
}
