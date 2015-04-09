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

import com.goodhuddle.huddle.service.ExportService;
import com.goodhuddle.huddle.service.request.member.ExportMembersRequest;
import com.goodhuddle.huddle.web.dto.util.ValueObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/export")
@Scope("session")
public class ExportApiController {

    private static final Logger log = LoggerFactory.getLogger(ExportApiController.class);

    private final ExportService exportService;

    @Autowired
    public ExportApiController(ExportService exportService) {
        this.exportService = exportService;
    }

    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public ValueObject bulkExport(@Valid @RequestBody ExportMembersRequest request) throws IOException {
        log.debug("Bulk tagging members in set with ID {}", request.getBulkSetId());
        return new ValueObject(exportService.exportMembers(request));
    }

    @RequestMapping(value = "/{fileName:.+}", method = RequestMethod.GET)
    public void downloadExport(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        log.debug("Downloading export file: {}", fileName);
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"members.csv\"");
        File exportFile = exportService.getExportFile(fileName);
        try (FileInputStream in = new FileInputStream(exportFile)) {
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        }
    }
}
