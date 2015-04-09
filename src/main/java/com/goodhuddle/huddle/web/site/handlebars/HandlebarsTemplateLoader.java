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

import com.github.jknack.handlebars.io.TemplateSource;
import com.github.jknack.handlebars.io.URLTemplateSource;
import com.github.jknack.handlebars.springmvc.SpringTemplateLoader;
import com.goodhuddle.huddle.service.ThemeService;
import com.goodhuddle.huddle.web.HuddleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;

public class HandlebarsTemplateLoader extends SpringTemplateLoader {

    private static final Logger log = LoggerFactory.getLogger(HandlebarsTemplateLoader.class);

    private final HuddleContext huddleContext;
    private final ThemeService themeService;

    public HandlebarsTemplateLoader(ApplicationContext applicationContext, HuddleContext huddleContext, ThemeService themeService) {
        super(applicationContext);
        this.huddleContext = huddleContext;
        this.themeService = themeService;
        setPrefix("classpath:/templates");
    }

    @Override
    public TemplateSource sourceAt(String uri) throws IOException {
        // check for override in theme
        String themeFile = "layouts/" + uri + ".hbs";
        log.trace("Checking for theme override for: {}", themeFile);
        if (huddleContext.getHuddle() != null) {
            File file = themeService.getActiveThemeFile(themeFile);
            if (file != null) {
                return new URLTemplateSource(file.getName(), file.toURI().toURL());
            }
        }
        return super.sourceAt(uri);
    }
}
