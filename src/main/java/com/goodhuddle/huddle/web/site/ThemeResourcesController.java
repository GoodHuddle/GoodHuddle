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

import com.goodhuddle.huddle.service.ThemeService;
import com.goodhuddle.huddle.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
public class ThemeResourcesController {

    private static final Logger log = LoggerFactory.getLogger(ThemeResourcesController.class);

    private final ThemeService themeService;

    @Autowired
    public ThemeResourcesController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @RequestMapping(value = "/resources/**", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadFile(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String url = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        log.trace("Retrieving theme file with path: {}", url);
        File file = themeService.getActiveThemeFile(url);
        if (file != null) {
            if (file.getName().endsWith(".pdf")) {
                response.setContentType("application/pdf");
            }
            return new FileSystemResource(file);
        } else {
            throw new ResourceNotFoundException("No theme file found for: " + url);
        }
    }
}
