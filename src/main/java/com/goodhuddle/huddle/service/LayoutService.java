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

package com.goodhuddle.huddle.service;

import com.goodhuddle.huddle.domain.Layout;
import com.goodhuddle.huddle.service.exception.LayoutExistsException;

import java.util.List;

public interface LayoutService {

    List<Layout> getLayouts(long themeId);

    List<Layout> getLayoutsForCurrentTheme(Layout.Type type);

    Layout getLayout(long layoutId);

    Layout createLayout(Layout layout) throws LayoutExistsException;

    void deleteLayout(long layoutId);

}
