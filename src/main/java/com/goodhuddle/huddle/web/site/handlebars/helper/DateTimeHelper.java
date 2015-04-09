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
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class DateTimeHelper {

    private DateTimeFormatter defaultDateFormat;
    private DateTimeFormatter defaultTimeFormat;
    private DateTimeFormatter defaultDateTimeFormat;

    public DateTimeHelper() {
        defaultDateFormat = DateTimeFormat.shortDate();
        defaultTimeFormat = DateTimeFormat.shortTime();
        defaultDateTimeFormat = DateTimeFormat.shortDateTime();
    }

    public String date(ReadableInstant value, Options options) {
        return format(value, options, defaultDateFormat);
    }

    public String time(ReadableInstant value, Options options) {
        return format(value, options, defaultTimeFormat);
    }

    public String datetime(ReadableInstant value, Options options) {
        return format(value, options, defaultDateTimeFormat);
    }

    private String format(ReadableInstant value, Options options, DateTimeFormatter defaultFormat) {
        DateTimeFormatter formatter = defaultFormat;
        String pattern = options.param(0, null);
        if (pattern != null) {
            formatter = DateTimeFormat.forPattern(pattern);
        }
        return formatter.print(value);
    }
}
