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

import com.goodhuddle.huddle.service.ThemeBundleService;
import com.goodhuddle.huddle.service.exception.ThemeBundleException;
import com.goodhuddle.huddle.service.exception.ThemeBundleLoadException;
import com.goodhuddle.huddle.web.builder.theme.ThemeBundleDetailBuilder;
import com.goodhuddle.huddle.web.dto.theme.ThemeBundleDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/theme/bundle")
public class ThemeBundleApiController {

    private static final Logger log = LoggerFactory.getLogger(ThemeBundleApiController.class);

    private final ThemeBundleService themeBundleService;
    private final ThemeBundleDetailBuilder themeBundleDetailBuilder;

    @Autowired
    public ThemeBundleApiController(ThemeBundleService themeBundleService, ThemeBundleDetailBuilder themeBundleDetailBuilder) {
        this.themeBundleService = themeBundleService;
        this.themeBundleDetailBuilder = themeBundleDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ThemeBundleDetail> getThemeBundles() throws ThemeBundleLoadException {
        log.debug("Retrieving theme bundle list");
        return themeBundleDetailBuilder.build(themeBundleService.getThemeBundles());
    }

    @RequestMapping(value = "/{slug}/thumbnail", method = RequestMethod.GET)
    public Resource downloadThumbnail(@PathVariable String slug) {
        log.debug("Showing thumbnail for theme bundle '{}'", slug);
        return themeBundleService.getThemeBundleThumbnail(slug);
    }

    @RequestMapping(value = "{slug}/install", method = RequestMethod.POST)
    public void installThemeBundle(@PathVariable("slug") String slug) throws ThemeBundleException {
        log.debug("Installing theme bundle '{}'", slug);
        themeBundleService.installTheme(slug);
    }

}
