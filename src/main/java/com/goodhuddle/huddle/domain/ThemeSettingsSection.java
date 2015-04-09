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

package com.goodhuddle.huddle.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThemeSettingsSection {

    private String code;
    private String name;
    private List<ThemeSettingsField> fields;

    public ThemeSettingsSection(String code, String name) {
        this.code = code;
        this.name = name;
        this.fields = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ThemeSettingsField> getFields() {
        return fields;
    }

    public void setFields(List<ThemeSettingsField> fields) {
        this.fields = fields;
    }

    public void addFields(ThemeSettingsField... fields) {
        Collections.addAll(this.fields, fields);
    }
}
