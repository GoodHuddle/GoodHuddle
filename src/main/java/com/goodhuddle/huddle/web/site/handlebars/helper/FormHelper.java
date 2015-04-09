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

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.Map;

@Service
public class FormHelper {

    public CharSequence hasErrors(Options options) throws IOException {
        BindingResult result = getBindingResult(options);
        if (result != null && result.hasErrors()) {
            return options.fn();
        } else {
            return options.inverse();
        }
    }

    public CharSequence errorMessage(String field, Options options) {
        FieldError fieldError = getFieldError(field, options);
        if (fieldError != null) {
            return new Handlebars.SafeString(
                    "<span class='help-block'>" + fieldError.getDefaultMessage() + "</span>");
        }
        return null;
    }

    public String errorClass(String field, Options options) {
        String cssClass = options.hash("class", "has-error");
        FieldError fieldError = getFieldError(field, options);
        return fieldError != null ? cssClass : "";
    }

    private BindingResult getBindingResult(Options options) {
        Map model = (Map) options.context.model();
        String target = "request";
        return (BindingResult) model.get("org.springframework.validation.BindingResult." + target);
    }

    private FieldError getFieldError(String field, Options options) {
        BindingResult result = getBindingResult(options);
        if (result != null) {
            FieldError fieldError = result.getFieldError(field);
            if (fieldError != null) {
                return fieldError;
            }
        }
        return null;
    }
}
