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

package com.goodhuddle.huddle.web.builder.theme;

import com.goodhuddle.huddle.domain.ThemeSettingsForm;
import com.goodhuddle.huddle.web.builder.AbstractDTOBuilder;
import com.goodhuddle.huddle.web.dto.theme.ThemeSettingsFormDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeSettingsFormDetailBuilder extends AbstractDTOBuilder<ThemeSettingsFormDetail, ThemeSettingsForm> {

    private final ThemeSettingsSectionDetailBuilder themeSettingsSectionDetailBuilder;

    @Autowired
    public ThemeSettingsFormDetailBuilder(ThemeSettingsSectionDetailBuilder themeSettingsSectionDetailBuilder) {
        this.themeSettingsSectionDetailBuilder = themeSettingsSectionDetailBuilder;
    }

    @Override
    protected ThemeSettingsFormDetail buildNullSafe(ThemeSettingsForm entity) {
        return new ThemeSettingsFormDetail(themeSettingsSectionDetailBuilder.build(entity.getSections()));
    }
}
