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

import com.goodhuddle.huddle.domain.ThemeSetting;
import com.goodhuddle.huddle.service.ThemeService;
import com.goodhuddle.huddle.service.request.theme.UpdateThemeSettingsRequest;
import com.goodhuddle.huddle.web.builder.theme.ThemeSettingDetailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/theme/setting")
public class ThemeSettingApiController {

    private static final Logger log = LoggerFactory.getLogger(ThemeSettingApiController.class);

    private final ThemeService themeService;
    private final ThemeSettingDetailBuilder themeSettingDetailBuilder;

    @Autowired
    public ThemeSettingApiController(ThemeService themeService, ThemeSettingDetailBuilder themeSettingDetailBuilder) {
        this.themeService = themeService;
        this.themeSettingDetailBuilder = themeSettingDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, String> getThemeSettings() {
        List<ThemeSetting> settings = themeService.getThemeSettings();
        Map<String, String> results = new HashMap<>();
        for (ThemeSetting setting : settings) {
            results.put(setting.getFieldCode(), setting.getValue());
        }
        return results;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void updateThemeSettings(@Valid @RequestBody UpdateThemeSettingsRequest request) {
        log.debug("Updating {} theme settings", request.getSettings().size());
        themeService.updateThemeSettings(request);
    }


    @RequestMapping(value = "/attachment/{path:.+}", method = RequestMethod.POST)
    public void uploadSettingAttachment(@PathVariable String path, @ModelAttribute MultipartFile file) throws IOException {
        themeService.saveThemeSettingAttachment(path, file.getOriginalFilename(), file.getBytes());
    }

    @RequestMapping(value = "/attachment/{path:.+}", method = RequestMethod.GET)
    public Resource downloadSettingAttachment(@PathVariable String path) {
        log.debug("Downloading theme setting attachment for '{}'", path);
        return themeService.getThemeSettingAttachment(path);
    }
}

