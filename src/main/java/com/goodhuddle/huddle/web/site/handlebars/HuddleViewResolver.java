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

package com.goodhuddle.huddle.web.site.handlebars;

import com.github.jknack.handlebars.helper.AssignHelper;
import com.github.jknack.handlebars.io.URLTemplateLoader;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.goodhuddle.huddle.service.ThemeService;
import com.goodhuddle.huddle.web.HuddleContext;
import com.goodhuddle.huddle.web.site.handlebars.helper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class HuddleViewResolver extends HandlebarsViewResolver {

    private final HuddleContext huddleContext;
    private final ThemeService themeService;

    @Autowired
    public HuddleViewResolver(ThemeService themeService,
                              LayoutHelper layoutHelper,
                              DateTimeHelper dateTimeHelper,
                              MathHelper mathHelper,
                              MemberHelper memberHelper,
                              BlogHelper blogHelper,
                              FormHelper formHelper,
                              PaymentHelper paymentHelper,
                              SignUpCountHelper signUpCountHelper,
                              HuddleContext huddleContext) {

        this.themeService = themeService;
        this.huddleContext = huddleContext;

        registerHelpers(layoutHelper);
        registerHelpers(dateTimeHelper);
        registerHelpers(mathHelper);
        registerHelpers(memberHelper);
        registerHelpers(blogHelper);
        registerHelpers(formHelper);
        registerHelpers(paymentHelper);
        registerHelpers(signUpCountHelper);

        registerHelper("assign", new AssignHelper());

        // todo use cache with clearCache() after theme is activated
        setCache(false);
    }

    @Override
    protected URLTemplateLoader createTemplateLoader(ApplicationContext context) {
        return new HandlebarsTemplateLoader(context, huddleContext, themeService);
    }
}
