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

import com.goodhuddle.huddle.domain.Theme;
import com.goodhuddle.huddle.service.ThemeService;
import com.goodhuddle.huddle.service.exception.ThemeBundleException;
import com.goodhuddle.huddle.web.builder.theme.ThemeDetailBuilder;
import com.goodhuddle.huddle.web.dto.theme.ThemeDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/theme")
public class ThemeApiController {

    private static final Logger log = LoggerFactory.getLogger(ThemeApiController.class);

    private final ThemeService themeService;
    private final ThemeDetailBuilder themeDetailBuilder;

    @Autowired
    public ThemeApiController(ThemeService themeService, ThemeDetailBuilder themeDetailBuilder) {
        this.themeService = themeService;
        this.themeDetailBuilder = themeDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ThemeDetail> getThemes() {
        log.debug("Retrieving theme list");
        return themeDetailBuilder.build(themeService.getThemes());
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ThemeDetail getActiveTheme() {
        log.debug("Retrieving active theme");
        return themeDetailBuilder.build(themeService.getActiveTheme());
    }

    @RequestMapping(value = "/{id}/active", method = RequestMethod.PUT)
    public void setActiveTheme(@PathVariable long id) {
        log.debug("Setting active theme to {}", id);
        themeService.setActiveTheme(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ThemeDetail getTheme(@PathVariable long id) {
        log.debug("Retrieving theme with ID '{}'", id);
        return themeDetailBuilder.build(themeService.getTheme(id));
    }

    @RequestMapping(value = "/{id}/thumbnail", method = RequestMethod.GET)
    public Resource downloadThumbnail(@PathVariable long id) {
        log.debug("Showing thumbnail for theme with ID '{}'", id);
        return themeService.getThemeThumbnail(id);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ThemeDetail createTheme(@RequestBody Theme theme) {
        log.info("Creating theme '{}'", theme.getName());
        return themeDetailBuilder.build(themeService.createTheme(theme));
    }

    @RequestMapping(value = "/bundle", method = RequestMethod.POST)
    public ThemeDetail uploadTheme(@ModelAttribute  MultipartFile file) throws IOException, ThemeBundleException {
        log.debug("Uploading theme bundle file '{}'", file.getOriginalFilename());
        return themeDetailBuilder.build(themeService.createThemeFromUpload(file, true));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteTheme(@PathVariable long id) throws IOException {
        log.info("Deleting theme with ID '{}'", id);
        themeService.deleteTheme(id);
    }
}
