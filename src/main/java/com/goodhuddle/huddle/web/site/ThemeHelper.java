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

package com.goodhuddle.huddle.web.site;

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.MenuItem;
import com.goodhuddle.huddle.domain.ThemeSetting;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThemeHelper {

    private final MenuHelper menuHelper;
    private final HuddleService huddleService;
    private final ThemeService themeService;

    @Autowired
    public ThemeHelper(MenuHelper menuHelper, HuddleService huddleService, ThemeService themeService) {
        this.menuHelper = menuHelper;
        this.huddleService = huddleService;
        this.themeService = themeService;
    }

    public void addThemeAttributes(Model model, MenuItem menuItem) {

        Huddle huddle = huddleService.getHuddle();
        model.addAttribute("huddle", huddle);

        menuHelper.addMenuAndPath(model, menuItem);

        List<ThemeSetting> themeSettings = themeService.getThemeSettings();
        Map<String, String> settings = new HashMap<>();
        for (ThemeSetting themeSetting : themeSettings) {
            settings.put(themeSetting.getFieldCode(), themeSetting.getValue());
        }
        model.addAttribute("settings", settings);
    }
}
