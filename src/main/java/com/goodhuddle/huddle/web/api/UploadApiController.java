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

import com.goodhuddle.huddle.service.impl.file.FileStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class UploadApiController {

    private static final Logger log = LoggerFactory.getLogger(UploadApiController.class);

    private final FileStore fileStore;

    @Autowired
    public UploadApiController(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    @RequestMapping(value = "/api/file/{id}", method = RequestMethod.GET)
    public Resource download(@PathVariable String id) throws IOException {
        File file = fileStore.getTempFile(id);
        log.debug("Downloading file '{}' for ID '{}'", file.getAbsolutePath(), id);
        return new FileSystemResource(file);
    }

    @RequestMapping(value = "/api/file", method = RequestMethod.POST)
    public String upload(@ModelAttribute MultipartFile file) throws IOException {
        String fileRef = fileStore.createTempFile(file.getBytes());
        log.debug("Uploaded file '{}' to '{}'", file.getOriginalFilename(), fileRef);
        return fileRef;
    }
}
