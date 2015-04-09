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

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.Page;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.service.PageService;
import com.goodhuddle.huddle.service.request.page.UpdatePageRequest;
import com.goodhuddle.huddle.web.builder.menu.MenuDetailBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class PageController {

    private static final Logger log = LoggerFactory.getLogger(PageController.class);

    private PageService pageService;
    private HuddleService huddleService;
    private MenuService menuService;
    private MenuDetailBuilder menuDetailBuilder;
    private ThemeHelper themeHelper;

    @Autowired
    public PageController(PageService pageService,
                          HuddleService huddleService,
                          MenuService menuService,
                          MenuDetailBuilder menuDetailBuilder,
                          ThemeHelper themeHelper) {
        this.pageService = pageService;
        this.huddleService = huddleService;
        this.menuService = menuService;
        this.menuDetailBuilder = menuDetailBuilder;
        this.themeHelper = themeHelper;
    }

    @RequestMapping("/")
    public String showLandingPage(Model model) {
        return showPage("home", model);
    }

    @RequestMapping("/{slug}")
    public String showPage(@PathVariable String slug, Model model) {

        if (slug.equals("admin")) {
            return "redirect:/admin/";
        }

        log.debug("Showing page with slug '{}'", slug);

        Huddle huddle = huddleService.getHuddle();
        if (huddle == null) {
            // not setup yet - should only happen very early on
            return "redirect:/_huddles/create";
        }

        model.addAttribute("huddle", huddle);

        Page page = pageService.getPageForSlug(slug);
        model.addAttribute("menu", menuDetailBuilder.build(menuService.getMainMenu()));
        themeHelper.addThemeAttributes(model, page != null ? page.getMenuItem() : null);

        if (page != null) {
            model.addAttribute("page", page);
            return "page/" + (page.getLayout() != null ? page.getLayout() : "default-page");
        } else {
            log.debug("No page found for '{}'", slug);
            return "error/page-not-found";
        }
    }

    @RequestMapping(value = "/page/preview", method = RequestMethod.POST)
    @ResponseBody
    public void savePagePreview(@RequestBody UpdatePageRequest page, HttpSession session) {
        log.info("Saving page preview");
        session.setAttribute("huddle.page.preview", page);
    }


    @RequestMapping(value = "/page/{pageId}/attachment/{fileName:.+}", method = RequestMethod.GET)
    public void downloadPageAttachment(@PathVariable long pageId,
                                       @PathVariable String fileName,
                                       HttpServletResponse response)
            throws IOException {

        log.debug("Showing feature image for blog post for '{}/{}'", pageId, fileName);
        File file = pageService.getAttachment(pageId, fileName);
        if (file != null) {
            Tika tika = new Tika();
            String contentType = tika.detect(file);
            response.setContentType(contentType);
            try (InputStream fileStream = new FileInputStream(file)) {
                IOUtils.copy(fileStream, response.getOutputStream());
            }
        } else {
            log.warn("Attempt to download missing page attachment: {}/{}", pageId, fileName);
        }
    }

    @RequestMapping(value = "/page/preview", method = RequestMethod.GET)
    public String previewPage(Model model, HttpSession session) {
        UpdatePageRequest pageUpdate = (UpdatePageRequest) session.getAttribute("huddle.page.preview");

        if (pageUpdate != null) {
            log.info("Showing page preview for: {}", pageUpdate.getId());

            if (pageUpdate.getId() != null) {
                Page page = pageService.getPage(pageUpdate.getId());
                themeHelper.addThemeAttributes(model, page.getMenuItem());
            } else {
                themeHelper.addThemeAttributes(model, null);
            }
            Page page = new Page(huddleService.getHuddle(), pageUpdate.getTitle(), pageUpdate.getSlug(),
                    pageUpdate.getLayout(), pageUpdate.getContent());
            model.addAttribute("page", page);
            model.addAttribute("huddle", huddleService.getHuddle());
            return "page/" + (page.getLayout() != null ? page.getLayout() : "default-page");

        } else {
            log.debug("No preview found");
            themeHelper.addThemeAttributes(model, null);
            return "error/page-not-found";
        }
    }


}
