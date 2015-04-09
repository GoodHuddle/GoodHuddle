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

package com.goodhuddle.huddle.service.impl.template;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TemplateProcessor {

    private static final Logger log = LoggerFactory.getLogger(TemplateProcessor.class);

    private final Handlebars handlebars;

    public TemplateProcessor() {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setSuffix(".hbs");
        this.handlebars = new Handlebars(loader);
    }

    public String process(String templateName, Object context) throws IOException {
        Template template = handlebars.compile(templateName);
        return template.apply(context);
    }
}
