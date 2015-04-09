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

package com.goodhuddle.huddle.service;

import com.goodhuddle.huddle.domain.Theme;
import com.goodhuddle.huddle.domain.ThemeSetting;
import com.goodhuddle.huddle.domain.ThemeSettingsForm;
import com.goodhuddle.huddle.service.exception.ThemeBundleException;
import com.goodhuddle.huddle.service.request.theme.UpdateThemeSettingsRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ThemeService {

    List<Theme> getThemes();

    Theme getTheme(long themeId);

    Resource getThemeThumbnail(long id);

    Theme getActiveTheme();

    void setActiveTheme(long themeId);

    Theme createTheme(Theme theme);

    Theme createThemeFromUpload(MultipartFile themeBundle, boolean activate) throws ThemeBundleException;

    Theme createThemeFromFile(File themeBundle, boolean activate) throws ThemeBundleException;

    Theme createThemeFromResource(Resource themeBundle, boolean activate) throws ThemeBundleException;

    void deleteTheme(long themeId) throws IOException;

    File getActiveThemeFile(String relativePath);

    List<ThemeSetting> getThemeSettings();

    List<ThemeSetting> updateThemeSettings(UpdateThemeSettingsRequest request);

    ThemeSettingsForm getThemeSettingsForm() throws ThemeBundleException;

    File saveThemeSettingAttachment(String path, String originalFilename, byte[] bytes) throws IOException;

    Resource getThemeSettingAttachment(String path);


//    List<ThemeFile> getTopLevelThemeFiles(long themeId);
//
//    List<ThemeFile> getThemeFilesInDirectory(long directoryId);
//
//    ThemeFile getThemeFile(long id);
//
//    ThemeFile createThemeFile(long themeId, Long directoryId,
//                              String filename, String contentType, InputStream inputStream,
//                              boolean overwrite)
//            throws ThemeFileExistsException;
//
//    ThemeFile createThemeDirectory(CreateThemeDirectoryRequest request)
//            throws ThemeFileExistsException;
//
//    ThemeFile updateThemeFileContent(long themeFileId, String content);
//
//    void deleteThemeFile(long themeFileId);


}
