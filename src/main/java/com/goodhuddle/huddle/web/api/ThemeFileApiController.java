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

import com.goodhuddle.huddle.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThemeFileApiController {

    private static final Logger log = LoggerFactory.getLogger(ThemeFileApiController.class);

    private final ThemeService themeService;

    @Autowired
    public ThemeFileApiController(ThemeService themeService) {
        this.themeService = themeService;
    }
//
//    @RequestMapping(value = "/api/theme/{the}/", method = RequestMethod.GET)
//    public ThemeFileRef getThemeFile(@PathVariable String path) {
//        log.debug("Retrieving theme file: '{}'", path);
//        File file = themeService.getThemeFile();
//        ThemeFileRef ref = new ThemeFileRef();
//        return themeService.getThemeFile(path);
//    }

//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public void deleteThemeFile(@PathVariable long id) {
//        log.debug("Deleting theme file with ID '{}'", id);
//        themeService.deleteThemeFile(id);
//    }
//
//    @RequestMapping(value = "/{id}/content", method = RequestMethod.GET)
//    public void getThemeFileContent(@PathVariable long id, HttpServletResponse response) throws IOException {
//        log.trace("Retrieving theme file content for: {}", id);
//        GridFsResource content = themeService.getThemeFileContent(id);
//        if (content != null) {
//            response.setContentType(content.getContentType());
//            try (InputStream fileStream = content.getInputStream()) {
//                IOUtils.copy(fileStream, response.getOutputStream());
//            }
//        } else {
//            throw new ResourceNotFoundException("No theme file found for: " + id);
//        }
//    }

//
//    @RequestMapping(value = "{themeId}", method = RequestMethod.POST)
//    public ThemeFileList uploadFile(@PathVariable long themeId,
//                                    @ModelAttribute BulkFileUpload fileUpload) throws IOException {
//        return uploadFile(themeId, null, fileUpload);
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public ThemeFile createDirectory(@Valid @RequestBody CreateThemeDirectoryRequest request)
//            throws ThemeFileExistsException {
//        log.debug("Creating theme directory '{}' in theme {}", request.getDirectoryName(), request.getThemeId());
//        return themeService.createThemeDirectory(request);
//    }
//
//    @RequestMapping(value = "{themeId}/{directoryId}", method = RequestMethod.POST)
//    public ThemeFileList uploadFile(@PathVariable long themeId, @PathVariable Long directoryId,
//                                    @ModelAttribute BulkFileUpload fileUpload) throws IOException {
//
//        log.info("Uploading files to theme");
//        List<ThemeFile> results = new ArrayList<>();
//        for (MultipartFile file : fileUpload.getFiles()) {
//            try(InputStream inputStream = file.getInputStream()) {
//                try {
//                    log.debug("Uploading file '{}' into GridFS with content type: {}",
//                            file.getOriginalFilename(), file.getContentType());
//                    results.add(themeService.createThemeFile(
//                            themeId, directoryId, file.getOriginalFilename(), file.getContentType(), inputStream, true));
//                } catch (ThemeFileExistsException e) {
//                    log.error("Unable to upload file: " + file.getOriginalFilename(), e);
//                }
//            }
//        }
//        return new ThemeFileList(results);
//    }
//
//    @RequestMapping(value = "/{id}/content", method = RequestMethod.PUT)
//    public ThemeFile updateThemeFileContent(@PathVariable long id, @RequestBody String content)
//            throws IOException, ThemeFileException {
//
//        log.debug("Uploading theme file content for '{}'", id);
//        return themeService.updateThemeFileContent(id, content);
//    }
}
