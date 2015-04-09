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

package com.goodhuddle.huddle.web.api.webhooks;

import com.goodhuddle.huddle.service.PaymentService;
import com.goodhuddle.huddle.service.exception.WebHookException;
import com.stripe.model.Event;
import com.stripe.net.APIResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StripeWebHook {

    private static final Logger log = LoggerFactory.getLogger(StripeWebHook.class);

    private final PaymentService paymentService;

    @Autowired
    public StripeWebHook(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RequestMapping(value = "/webhooks/stripe/event", method = RequestMethod.POST)
    @ResponseBody
    public String stripeEventOccurred(@RequestBody String stripeJsonEvent) throws WebHookException {
        Event event = APIResource.GSON.fromJson(stripeJsonEvent, Event.class);
        paymentService.handleStripeEvent(event);
        return "ok";
    }
}
