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

import com.goodhuddle.huddle.domain.Layout;
import com.goodhuddle.huddle.service.LayoutService;
import com.goodhuddle.huddle.service.exception.LayoutExistsException;
import com.goodhuddle.huddle.web.builder.layout.LayoutDetailBuilder;
import com.goodhuddle.huddle.web.builder.layout.LayoutRefBuilder;
import com.goodhuddle.huddle.web.dto.layout.LayoutDetail;
import com.goodhuddle.huddle.web.dto.layout.LayoutRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layout")
public class LayoutApiController {

    private static final Logger log = LoggerFactory.getLogger(LayoutApiController.class);

    private final LayoutService layoutService;
    private final LayoutRefBuilder layoutRefBuilder;
    private final LayoutDetailBuilder layoutDetailBuilder;

    @Autowired
    public LayoutApiController(LayoutService layoutService,
                               LayoutRefBuilder layoutRefBuilder,
                               LayoutDetailBuilder layoutDetailBuilder) {
        this.layoutService = layoutService;
        this.layoutRefBuilder = layoutRefBuilder;
        this.layoutDetailBuilder = layoutDetailBuilder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<LayoutRef> getLayoutsForCurrentTheme(@RequestParam(value = "type", required = false) Layout.Type type) {
        log.debug("Retrieving layouts for current theme (type = {})", type);
        return layoutRefBuilder.build(layoutService.getLayoutsForCurrentTheme(type));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public LayoutDetail getLayout(@PathVariable long id) {
        log.debug("Retrieving layout with ID '{}'", id);
        return layoutDetailBuilder.build(layoutService.getLayout(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public LayoutDetail createLayout(@RequestBody Layout layout) throws LayoutExistsException {
        log.info("Creating layout '{}'", layout.getName());
        layoutService.createLayout(layout);
        return layoutDetailBuilder.build(layout);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteLayout(@PathVariable long id) {
        log.info("Deleting layout with ID '{}'", id);
        layoutService.deleteLayout(id);
    }
}
