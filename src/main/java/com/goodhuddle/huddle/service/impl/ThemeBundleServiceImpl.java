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

package com.goodhuddle.huddle.service.impl;

import com.goodhuddle.huddle.domain.ThemeBundle;
import com.goodhuddle.huddle.service.ThemeBundleService;
import com.goodhuddle.huddle.service.ThemeService;
import com.goodhuddle.huddle.service.exception.ThemeBundleException;
import com.goodhuddle.huddle.service.exception.ThemeBundleLoadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ThemeBundleServiceImpl implements ThemeBundleService {

    private static final Logger log = LoggerFactory.getLogger(ThemeBundleServiceImpl.class);

    private final ThemeService themeService;

    @Autowired
    public ThemeBundleServiceImpl(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Override
    public List<ThemeBundle> getThemeBundles() throws ThemeBundleLoadException {

        try {
            List<ThemeBundle> themeBundles = Arrays.asList(
                    loadSystemThemeBundle("simpleblue"),
                    loadSystemThemeBundle("simplegreen")
            );
            log.debug("Found {} available theme bundles", themeBundles.size());
            return themeBundles;
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new ThemeBundleLoadException("Error loading theme bundles", e);
        }
    }

    @Override
    public Resource getThemeBundleThumbnail(String slug) {
        return getThemeBundleResource(slug, "thumbnail.png");
    }

    @Override
    public void installTheme(String slug) throws ThemeBundleException {
        themeService.createThemeFromResource(getThemeBundleResource(slug, "theme.zip"), true);
    }

    private ThemeBundle loadSystemThemeBundle(String slug)
            throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Resource bundleDescriptor = getThemeBundleResource(slug, "site.xml");
        Document siteDoc = dBuilder.parse(bundleDescriptor.getInputStream());
        Element themeNode = (Element) siteDoc.getElementsByTagName("theme").item(0);
        String id = themeNode.getElementsByTagName("id").item(0).getTextContent();
        String name = themeNode.getElementsByTagName("name").item(0).getTextContent();
        String description = themeNode.getElementsByTagName("description").item(0).getTextContent();

        log.debug("Loaded system theme bundle: " + id);
        return new ThemeBundle(id, name, description);
    }

    private Resource getThemeBundleResource(String slug, String resource) {
        String path = "/default-themes/" + slug + "/" + resource;
        log.debug("Loading theme bundle resource from: " + path);
        return new ClassPathResource(path);
    }
}
