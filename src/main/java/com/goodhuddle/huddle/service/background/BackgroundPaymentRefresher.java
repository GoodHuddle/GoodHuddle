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

package com.goodhuddle.huddle.service.background;

import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.PaymentService;
import com.goodhuddle.huddle.web.HuddleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackgroundPaymentRefresher {

    private static final Logger log = LoggerFactory.getLogger(BackgroundPaymentRefresher.class);

    private final HuddleService huddleService;
    private final HuddleContext huddleContext;
    private final PaymentService paymentService;

    @Autowired
    public BackgroundPaymentRefresher(HuddleService huddleService,
                                      HuddleContext huddleContext,
                                      PaymentService paymentService) {

        this.huddleService = huddleService;
        this.huddleContext = huddleContext;
        this.paymentService = paymentService;
    }

////    @Scheduled(fixedDelay = 5000)
//    public void refreshPayments() {
//
//        List<Huddle> huddles = huddleService.getHuddles();
//
//        for (Huddle huddle : huddles) {
//
//            log.debug("Checking for new Payments for Huddle '{}'", huddle.getSlug());
//            huddleContext.setHuddle(huddle);
//            try {
//                paymentService.refreshPaymentsList();
//            } catch (Exception e) {
//                log.error("Error checking for payments for huddle '" + huddle.getSlug() + "'", e);
//            }
//        }
//
//    }
}
