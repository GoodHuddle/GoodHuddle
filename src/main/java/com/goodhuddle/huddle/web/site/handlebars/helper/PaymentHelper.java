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

package com.goodhuddle.huddle.web.site.handlebars.helper;

import com.github.jknack.handlebars.Options;
import com.goodhuddle.huddle.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PaymentHelper {

    private final PaymentService paymentService;

    @Autowired
    public PaymentHelper(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public CharSequence paymentPublishableKey(Options options) throws IOException {
        return paymentService.getHuddlePublishableKey();
    }
}
