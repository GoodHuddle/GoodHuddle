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

import com.goodhuddle.huddle.service.ThemeService;
import com.goodhuddle.huddle.service.exception.ThemeBundleException;
import com.goodhuddle.huddle.web.builder.theme.ThemeSettingsFormDetailBuilder;
import com.goodhuddle.huddle.web.dto.theme.ThemeSettingsFormDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theme/setting/form")
public class ThemeSettingsFormApiController {

    private static final Logger log = LoggerFactory.getLogger(ThemeSettingsFormApiController.class);

    private final ThemeService themeService;
    private final ThemeSettingsFormDetailBuilder themeSettingsFormDetailBuilder;

    @Autowired
    public ThemeSettingsFormApiController(ThemeService themeService,
                                          ThemeSettingsFormDetailBuilder themeSettingsFormDetailBuilder) {
        this.themeService = themeService;
        this.themeSettingsFormDetailBuilder = themeSettingsFormDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ThemeSettingsFormDetail getThemeSettingsForm() throws ThemeBundleException {
        return themeSettingsFormDetailBuilder.build(themeService.getThemeSettingsForm());
    }
}
