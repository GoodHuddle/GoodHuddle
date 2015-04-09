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

package com.goodhuddle.huddle.web.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminThemeController {

    private static final Logger log = LoggerFactory.getLogger(AdminThemeController.class);

//    private final HuddleService huddleService;
//    private final ThemeService themeService;
//    private final LayoutService layoutService;
//
//    @Autowired
//    public AdminThemeController(HuddleService huddleService, ThemeService themeService, LayoutService layoutService) {
//        this.huddleService = huddleService;
//        this.themeService = themeService;
//        this.layoutService = layoutService;
//    }
//
//
//    @RequestMapping(value = "/admin/theme/upload", method = RequestMethod.GET)
//    public String showThemeUploadPage(Model model) {
//        model.addAttribute("huddle", huddleService.getHuddle());
//        return "admin/theme/upload-theme";
//    }
//
//    @RequestMapping(value = "/admin/theme/upload", method = RequestMethod.POST)
//    public String uploadTheme(@ModelAttribute UploadThemeForm form)
//            throws IOException, ThemeBundleException {
//
//        MultipartFile file = form.getFile();
//        log.debug("Uploading theme bundle file '{}' (activate={})", file.getOriginalFilename(), form.isActivate());
//        if (!file.isEmpty()) {
//            try {
//                byte[] bytes = file.getBytes();
//                File tempFile = File.createTempFile("theme", "zip");
//                try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tempFile))) {
//                    stream.write(bytes);
//                }
//                Theme theme = themeService.createThemeFromBundle(tempFile, form.isActivate());
//                tempFile.delete();
//                return "redirect:/admin/theme";
//            } catch (IOException e) {
//                throw new ThemeBundleException("Error creating theme from bundle file: " + file.getOriginalFilename(), e);
//            }
//        } else {
//            throw new ThemeBundleException("Unable to create theme, no bundle provided");
//        }
//    }
//
//    @RequestMapping(value = "/admin/theme/activate/{themeId}", method = RequestMethod.POST)
//    public String activateTheme(@PathVariable String themeId) {
//        log.debug("Setting active theme ID for huddle to '{}'", themeId);
//        huddleService.setActiveThemeId(themeId);
//        return "redirect:/admin/theme";
//    }
//
//    @RequestMapping(value = "/admin/theme/delete/{id}", method = RequestMethod.POST)
//    public String deleteTheme(@PathVariable String id) {
//        log.info("Deleting theme with ID '{}'", id);
//        themeService.deleteTheme(id);
//        return "redirect:/admin/theme";
//    }
//
//    @RequestMapping(value = "/admin/theme/{id}", method = RequestMethod.GET)
//    public String showManageLayouts(@PathVariable String id, Model model) {
//        log.info("Showing manage theme layouts page for theme with ID '{}'", id);
//        model.addAttribute("theme", themeService.getTheme(id));
//        model.addAttribute("layouts", layoutService.getLayouts(id));
//        return "admin/theme/manage-layouts";
//    }
//
//    @RequestMapping(value = "/admin/layout/{id}", method = RequestMethod.GET)
//    public String showEditLayoutPage(@PathVariable String id, Model model) {
//        Layout layout = layoutService.getLayout(id);
//        model.addAttribute("layout", layout);
//        model.addAttribute("theme", themeService.getTheme(layout.getThemeId()));
//        return "admin/theme/edit-layout";
//    }
//
//    @RequestMapping(value = "/admin/theme/{themeId}/layout/create", method = RequestMethod.GET)
//    public String showCreateLayoutPage(@PathVariable String themeId, Model model) {
//        model.addAttribute("theme", themeService.getTheme(themeId));
//        return "admin/theme/create-layout";
//    }
//
//    @RequestMapping(value = "/admin/theme/{themeId}/resources", method = RequestMethod.GET)
//    public String showTopLevelThemeFiles(@PathVariable String themeId, Model model) {
//
//        log.info("Showing manage theme resources page for theme with ID '{}'", themeId);
//        model.addAttribute("theme", themeService.getTheme(themeId));
//        List<ThemeFile> resources  = themeService.getTopLevelThemeFiles(themeId);
//        model.addAttribute("resources", resources);
//        return "admin/theme/manage-resources";
//    }
//
//    @RequestMapping(value = "/admin/theme/resource/{resourceId}", method = RequestMethod.GET)
//    public String showThemeFile(@PathVariable String resourceId, Model model) throws IOException {
//
//        ThemeFile themeFile = themeService.getThemeFile(resourceId);
//        model.addAttribute("resource", themeFile);
//        model.addAttribute("theme", themeService.getTheme(themeFile.getThemeId()));
//
//        List<ThemeFile> path = new ArrayList<>();
//        String parentId = themeFile.getDirectoryId();
//        while (parentId != null) {
//            ThemeFile parent = themeService.getThemeFile(parentId);
//            path.add(parent);
//            parentId = parent.getDirectoryId();
//        }
//        model.addAttribute("path", path);
//
//        if (themeFile.isDirectory()) {
//            model.addAttribute("resources", themeService.getThemeFilesInDirectory(themeFile.getId()));
//            return "admin/theme/manage-resources";
//        } else {
//            if (StringUtils.startsWith(themeFile.getContentType(), "text")
//                    || StringUtils.contains(themeFile.getContentType(), "javascript")) {
//                model.addAttribute("mode", "text");
//                GridFsResource content = themeService.getThemeFileContent(themeFile.getId());
//                try (InputStream in = content.getInputStream())  {
//                    String contentAsString = IOUtils.toString(in);
//                    model.addAttribute("content", contentAsString);
//                }
//            } else if (StringUtils.startsWith(themeFile.getContentType(), "image")) {
//                model.addAttribute("mode", "image");
//            }
//            return "admin/theme/view-resource";
//        }
//    }
//    }
}
