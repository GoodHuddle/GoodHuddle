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

import com.goodhuddle.huddle.domain.*;
import com.goodhuddle.huddle.repository.LayoutRepository;
import com.goodhuddle.huddle.repository.ThemeRepository;
import com.goodhuddle.huddle.repository.ThemeSettingRepository;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.ThemeService;
import com.goodhuddle.huddle.service.exception.ThemeBundleException;
import com.goodhuddle.huddle.service.impl.file.FileStore;
import com.goodhuddle.huddle.service.request.theme.UpdateThemeSettingsRequest;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ThemeServiceImpl implements ThemeService {

    private static final Logger log = LoggerFactory.getLogger(ThemeServiceImpl.class);

    private final HuddleService huddleService;
    private final ThemeRepository themeRepository;
    private final LayoutRepository layoutRepository;
    private final ThemeSettingRepository themeSettingRepository;
    private final FileStore fileStore;


    @Autowired
    public ThemeServiceImpl(HuddleService huddleService,
                            ThemeRepository themeRepository,
                            LayoutRepository layoutRepository,
                            ThemeSettingRepository themeSettingRepository,
                            FileStore fileStore) {
        this.huddleService = huddleService;
        this.themeRepository = themeRepository;
        this.layoutRepository = layoutRepository;
        this.themeSettingRepository = themeSettingRepository;
        this.fileStore = fileStore;
    }

    @Override
    public List<Theme> getThemes() {
        return themeRepository.findByHuddle(huddleService.getHuddle());
    }

    @Override
    public Theme getTheme(long themeId) {
        return themeRepository.findByHuddleAndId(huddleService.getHuddle(), themeId);
    }

    @Override
    public Resource getThemeThumbnail(long themeId) {
        File thumbnail = getThemeFile(themeId, "thumbnail.jpg");
        if (thumbnail == null || !thumbnail.exists()) {
            thumbnail = getThemeFile(themeId, "thumbnail.png");
        }
        return thumbnail != null && thumbnail.exists()
                ? new FileSystemResource(thumbnail)
                : new ClassPathResource("/static/images/no-theme-thumbnail.png");
    }

    @Override
    public Theme getActiveTheme() {
        return themeRepository.findByHuddleAndActiveIsTrue(huddleService.getHuddle());
    }

    @Override
    @Transactional(readOnly = false)
    public void setActiveTheme(long themeId) {
        Theme theme = themeRepository.findByHuddleAndId(huddleService.getHuddle(), themeId);
        log.info("Setting theme '{}' as active", theme.getName());
        Theme current = getActiveTheme();
        if (current != null) {
            current.setActive(false);
            themeRepository.save(current);
        }
        theme.setActive(true);
        themeRepository.save(theme);
    }

    @Override
    public Theme createTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    @Override
    @Transactional(readOnly = false)
    public Theme createThemeFromUpload(MultipartFile themeBundle, boolean activate) throws ThemeBundleException {
        try {
            log.info("Importing theme from zip file: {}", themeBundle.getOriginalFilename());

            String tempFileId = fileStore.createTempFile(themeBundle.getBytes());
            Theme theme = createThemeFromFile(fileStore.getTempFile(tempFileId), activate);
            fileStore.deleteTempFile(tempFileId);
            return theme;

        } catch (IOException e) {
            throw new ThemeBundleException("Error creating theme from bundle: " + themeBundle, e);
        }
    }

    @Override
    public Theme createThemeFromFile(File themeBundle, boolean activate) throws ThemeBundleException {
        try {
            log.info("Importing theme from zip file: {}", themeBundle);
            String tempId = UUID.randomUUID().toString();
            File tempThemeDir = fileStore.unzip(themeBundle, "/theme/" + tempId);
            return createThemeFromUnzipped(tempThemeDir, activate);
        } catch (ZipException | IOException e) {
            throw new ThemeBundleException("Error creating theme from bundle: " + themeBundle, e);
        }
    }

    @Override
    public Theme createThemeFromResource(Resource themeBundle, boolean activate) throws ThemeBundleException {
        try {
            log.info("Importing theme from zip file: {}", themeBundle);
            String tempId = UUID.randomUUID().toString();

            File tempThemeDir = fileStore.unzip(IOUtils.toByteArray(themeBundle.getInputStream()), "/theme/" + tempId);
            return createThemeFromUnzipped(tempThemeDir, activate);
        } catch (ZipException | IOException e) {
            throw new ThemeBundleException("Error creating theme from bundle: " + themeBundle, e);
        }
    }

    public Theme createThemeFromUnzipped(File unzippedDir, boolean activate) throws ThemeBundleException {
        try {
            // load theme details from theme descriptor file in zip
            File siteDescriptor = new File(unzippedDir, "site.xml");
            if (!siteDescriptor.exists()) {
                throw new ThemeBundleException("'site.xml' must be included in the theme bundle");
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document siteDoc = dBuilder.parse(siteDescriptor);

            Element themeNode = (Element) siteDoc.getElementsByTagName("theme").item(0);
            String name = themeNode.getElementsByTagName("name").item(0).getTextContent();
            String description = themeNode.getElementsByTagName("description").item(0).getTextContent();

            Theme theme = themeRepository.save(new Theme(huddleService.getHuddle(), name, description));
            File themeDir = fileStore.moveFile(unzippedDir, "/theme/" + theme.getId());
            File layoutDir = new File(themeDir, "layouts");

            loadLayouts(new File(layoutDir, "page"), Layout.Type.page, theme);
            loadLayouts(new File(layoutDir, "blog"), Layout.Type.blog, theme);
            loadLayouts(new File(layoutDir, "blog/post"), Layout.Type.blogPost, theme);

            if (activate) {
                setActiveTheme(theme.getId());
            }

            return theme;

        } catch (Exception e) {
            throw new ThemeBundleException("Error creating theme from bundle: " + unzippedDir, e);
        }
    }

    protected void loadLayouts(File directory, Layout.Type type, Theme theme) throws IOException {

        log.debug("Loading {} layouts from: {}", type, directory);
        if (directory.exists())  {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".hbs")) {
                        String name = file.getName().substring(0, file.getName().length() - ".hbs".length());
                        log.debug("Adding theme layout: {}", name);
                        layoutRepository.save(new Layout(theme, name, type));
                    }
                }
            }
        }
    }

    @Override
    public void deleteTheme(long themeId) throws IOException {
        Theme theme = themeRepository.findByHuddleAndId(huddleService.getHuddle(), themeId);
        if (theme != null) {
            themeRepository.delete(theme);
            fileStore.delete("theme", String.valueOf(themeId));
        } else {
            log.debug("Ignoring request to delete theme with ID {} as it does not exist in current huddle", themeId);
        }
    }

    @Override
    public File getActiveThemeFile(String path) {
        Theme theme = getActiveTheme();
        if (theme != null) {
            return getThemeFile(theme.getId(), path);
        } else {
            log.trace("No active theme for huddle, unable to find theme file with path: " + path);
            return null;
        }
    }

    @Override
    public List<ThemeSetting> getThemeSettings() {
        return themeSettingRepository.findByHuddle(huddleService.getHuddle());
    }

    @Override
    public List<ThemeSetting> updateThemeSettings(UpdateThemeSettingsRequest request) {
        Huddle huddle = huddleService.getHuddle();
        themeSettingRepository.deleteByHuddle(huddleService.getHuddle());
        List<ThemeSetting> settings = new ArrayList<>();
        for (String code : request.getSettings().keySet()) {
            String value = request.getSettings().get(code);
            settings.add(new ThemeSetting(huddle, code, value));
        }
        themeSettingRepository.save(settings);
        return settings;
    }

    public ThemeSettingsForm getThemeSettingsForm() throws ThemeBundleException {

        Theme theme = getActiveTheme();
        File siteDescriptor = getThemeFile(theme.getId(), "site.xml");

        ThemeSettingsForm form = new ThemeSettingsForm();
        if (siteDescriptor.exists()) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document siteDoc = dBuilder.parse(siteDescriptor);

                Element themeNode = (Element) siteDoc.getElementsByTagName("theme").item(0);
                NodeList settingsNodes = themeNode.getElementsByTagName("settings");
                if (settingsNodes.getLength() > 0) {
                    Element settingsNode = (Element) settingsNodes.item(0);
                    NodeList sectionNodes = settingsNode.getElementsByTagName("section");
                    for (int i = 0; i < sectionNodes.getLength(); i++) {
                        Element sectionNode = (Element) sectionNodes.item(i);
                        String code = sectionNode.getAttribute("code");
                        String name = sectionNode.getAttribute("name");
                        ThemeSettingsSection section = new ThemeSettingsSection(code, name);

                        NodeList fieldNodes = sectionNode.getElementsByTagName("field");
                        for (int n = 0; n < fieldNodes.getLength(); n++) {
                            Element fieldNode = (Element) fieldNodes.item(n);
                            code = fieldNode.getAttribute("code");
                            name = fieldNode.getAttribute("name");
                            ThemeSettingType type = ThemeSettingType.valueOf(fieldNode.getAttribute("type"));
                            section.getFields().add(new ThemeSettingsField(code, name, type));
                        }

                        form.getSections().add(section);
                    }
                }

            } catch (ParserConfigurationException | SAXException | IOException e) {
                throw new ThemeBundleException("Error reading site.xml from theme: " + theme.getName(), e);
            }
        }
        return form;
    }

    @Override
    public File saveThemeSettingAttachment(String path, String originalFilename, byte[] bytes) throws IOException {
        return fileStore.createFile(getSettingAttachmentPath(path), bytes);
    }

    @Override
    public Resource getThemeSettingAttachment(String path) {
        return new FileSystemResource(fileStore.getFile(getSettingAttachmentPath(path)));
    }

    private String getSettingAttachmentPath(String path) {
        return "/attachments/theme/" + path;
    }

    public File getThemeFile(long themeId, String path) {
        File file = fileStore.getFile("/theme/" + themeId + "/" + path);
        if (file == null) {
            log.trace("Theme resource {} not found, using default", path);
        }
        return file;
    }



//    @Override
//    public List<ThemeFile> getTopLevelThemeFiles(long themeId) {
//        return themeFileRepository.findByThemeIdAndDirectoryIdIsNull(themeId);
//    }
//
//    @Override
//    public List<ThemeFile> getThemeFilesInDirectory(long directoryId) {
//        return themeFileRepository.findByDirectoryId(directoryId);
//    }
//
//    @Override
//    public ThemeFile getThemeFile(long id) {
//        return themeFileRepository.findOne(id);
//    }

//    public Theme createThemeFromDirectory(File directory, String name, String description) throws IOException {
//        log.info("Loading theme '{}' from directory: {}", name, directory);
//        Theme theme = themeRepository.save(new Theme(name, description));
//        //loadLayoutFiles(directory, theme);
//        //loadThemeFiles(new File(directory, "theme"), theme, null, "/theme/" + theme.getId());
//        return theme;
//    }

//    @Override
//    public ThemeFile createThemeFile(long themeId, Long directoryId, String filename,
//                                     String contentType, InputStream inputStream, boolean overwrite)
//            throws ThemeFileExistsException {
//
//        String fullPath;
//        ThemeFile directory = null;
//        if (directoryId != null) {
//            directory = themeFileRepository.findOne(directoryId);
//            fullPath = directory.getPath() + "/" + filename;
//        } else {
//            fullPath = "/theme/" + themeId + "/" + filename;
//        }
//
//        ThemeFile existing = themeFileRepository.findByDirectoryIdAndName(directoryId, filename);
//        if (existing != null) {
//            if (overwrite) {
//                log.info("Replacing existing file with ID: {}", existing.getId());
//                themeFileRepository.delete(existing.getId());
//                gridFsOperations.delete(new Query().addCriteria(Criteria.where("filename").is(fullPath)));
//            } else {
//                throw new ThemeFileExistsException("Theme file '" + filename + "' already exists");
//            }
//        }
//
//        Theme theme = themeRepository.findOne(themeId);
//        ThemeFile file = themeFileRepository.save(
//                new ThemeFile(theme, directory, filename, fullPath, contentType));
//        if (inputStream != null) {
//            gridFsOperations.store(inputStream, file.getPath(), file.getContentType());
//        }
//        return file;
//    }

//    @Override
//    public ThemeFile createThemeDirectory(CreateThemeDirectoryRequest request)
//            throws ThemeFileExistsException {
//
//
//        String fullPath;
//        ThemeFile directory = null;
//        if (request.getDirectoryId() != null) {
//            log.debug("Adding sub-directory to {}", request.getDirectoryId());
//            directory = themeFileRepository.findOne(request.getDirectoryId());
//            fullPath = directory.getPath() + "/" + request.getDirectoryName();
//        } else {
//            log.debug("Adding directory to top level");
//            fullPath = "/theme/" + request.getDirectoryId() + "/" + request.getDirectoryName();
//        }
//
//        ThemeFile existing = themeFileRepository.findByDirectoryIdAndName(
//                request.getDirectoryId(), request.getDirectoryName());
//        if (existing != null) {
//            throw new ThemeFileExistsException("Theme directory '" + request.getDirectoryName() + "' already exists");
//        }
//
//        Theme theme = themeRepository.findOne(request.getThemeId());
//
//        ThemeFile file = themeFileRepository.save(
//                new ThemeFile(theme, directory, request.getDirectoryName(), fullPath, ThemeFile.DIRECTORY_TYPE));
//        log.info("Theme directory '{}' created with ID {}", request.getDirectoryName(), file.getId());
//        return file;
//    }
//
//    @Override
//    public ThemeFile updateThemeFileContent(long themeFileId, String content) {
//        ThemeFile file = themeFileRepository.findOne(themeFileId);
//        log.debug("Storing theme file with path: {}", file.getPath());
//        GridFsResource oldFile = gridFsOperations.getResource(file.getPath());
//        gridFsOperations.store(IOUtils.toInputStream(content), file.getPath(), file.getContentType());
//        if (oldFile != null) {
//            gridFsOperations.delete(new Query().addCriteria(Criteria.where("_id").is(oldFile.getId())));
//        }
//        return file;
//    }
//
//    @Override
//    public void deleteThemeFile(long themeFileId) {
//        ThemeFile themeFile = themeFileRepository.findOne(themeFileId);
//        if (themeFile != null) {
//
//            // delete sub-files first
//            if (themeFile.isDirectory()) {
//                List<ThemeFile> childFiles = getThemeFilesInDirectory(themeFileId);
//                for (ThemeFile childFile : childFiles) {
//                    deleteThemeFile(childFile.getId());
//                }
//            }
//
//            themeFileRepository.delete(themeFileId);
//            log.info("Theme file with ID {} deleted", themeFileId);
//        }
//    }


//    protected void loadThemeFiles(File directory, Theme theme, ThemeFile themeDir, String path) throws IOException {
//
//        log.debug("Loading theme files from: {}", directory);
//        File[] files = directory.listFiles();
//        if (files != null) {
//            for (File file : files) {
//                log.debug("Processing theme file: {}", file);
//                String filePath = path + "/" + file.getName();
//                Tika tika = new Tika();
//                String contentType = tika.detect(file);
//                ThemeFile themeFile = new ThemeFile(theme, themeDir, file.getName(), filePath, contentType);
//                if (!file.isDirectory()) {
//                    try (InputStream inputStream = new FileSystemResource(file).getInputStream()) {
//                        createThemeFile(themeFile, inputStream);
//                    }
//                } else {
//                    themeFile.setContentType(ThemeFile.DIRECTORY_TYPE);
//                    ThemeFile dir = createThemeFile(themeFile, null);
//                    loadThemeFiles(file, theme, dir, filePath);
//                }
//            }
//        }
//    }

//    protected ThemeFile createThemeFile(ThemeFile file, InputStream inputStream) {
//        file = themeFileRepository.save(file);
//        if (inputStream != null) {
//            gridFsOperations.store(inputStream, file.getPath(), file.getContentType());
//        }
//        return file;
//    }
}
