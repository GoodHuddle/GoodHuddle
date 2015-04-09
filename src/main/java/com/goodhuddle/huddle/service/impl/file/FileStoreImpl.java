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

package com.goodhuddle.huddle.service.impl.file;

import com.goodhuddle.huddle.web.HuddleContext;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStoreImpl implements FileStore {

    private static final Logger log = LoggerFactory.getLogger(FileStoreImpl.class);

    private final HuddleContext huddleContext;

    @Value("${goodhuddle.dataDir:gh-data}")
    private File dataDir;

    @Autowired
    public FileStoreImpl(HuddleContext huddleContext) {
        this.huddleContext = huddleContext;
    }

    @Override
    public File createFile(String fullPath, byte[] data) throws IOException {
        String directory = null;
        String fileName = fullPath;
        int index = fullPath.lastIndexOf("/");
        if (index > 0) {
            directory = fullPath.substring(0, index);
            fileName = fullPath.substring(index);
        }
        return createFile(directory, fileName, data);
    }

    @Override
    public File createFile(String directoryName, String fileName, byte[] data) throws IOException {

        File file = createFile(directoryName, fileName);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
            stream.write(data);
        }
        return file;
    }

    @Override
    public File createFile(String directoryName, String fileName) throws IOException {

        File directory = getHuddleBaseDir();
        if (directoryName != null) {
            directory = new File(getHuddleBaseDir(), directoryName);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("Unable to create base directory '"
                            + directoryName + "' to store file '" + fileName + "'");
                }
            }
        }

        return new File(directory, fileName);
    }

    @Override
    public File getFile(String path) {
        File file = new File(getHuddleBaseDir(), path);
        return file.exists() ? file : null;
    }

    @Override
    public File getFile(String directoryPath, String fileName) {
        File dir = new File(getHuddleBaseDir(), directoryPath);
        File file = new File(dir, fileName);
        return file.exists() ? file : null;
    }

    @Override
    public boolean delete(String path) throws IOException {
        return delete(getFile(path));
    }

    @Override
    public boolean delete(String directoryName, String fileName) throws IOException {
        return delete(getFile(directoryName, fileName));
    }

    public boolean delete(File file) throws IOException {
        log.debug("Deleting file: {}", file);
        if (file != null) {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
                return true;
            } else {
                return file.delete();
            }
        }
        return false;
    }

    @Override
    public File moveFile(File src, String dest) {
        File destFile = new File(getHuddleBaseDir(), dest);
        src.renameTo(destFile);
        return destFile;
    }

    @Override
    public String createTempFile(byte[] data) throws IOException {
        File tempDir = getHuddleBaseTempDir();
        if (!tempDir.exists() && !tempDir.mkdirs()) {
            throw new IOException("Unable to create base directory '" + tempDir + "' to store temp files");
        }

        String fileName = UUID.randomUUID().toString();
        File file = new File(tempDir, fileName);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
            stream.write(data);
        }
        return fileName;
    }

    @Override
    public String createTempDirectory() throws IOException {
        String dirName = UUID.randomUUID().toString();
        makeDirectories(new File(getHuddleBaseTempDir(), dirName));
        return dirName;
    }

    @Override
    public File getTempFile(String tempFileId) {
        return new File(getHuddleBaseTempDir(), tempFileId);
    }

    @Override
    public File moveTempFile(String tempFileId, String path) throws IOException {
        File dest = new File(getHuddleBaseDir(), path);
        makeDirectories(dest.getParentFile());
        return moveTempFile(tempFileId, dest);
    }

    @Override
    public File moveTempFile(String tempFileId, String directoryName, String fileName) throws IOException {
        File dir = makeDirectories(new File(getHuddleBaseDir(), directoryName));
        File dest = new File(dir, fileName);
        return moveTempFile(tempFileId, dest);
    }

    public File moveTempFile(String tempFileId, File dest) throws IOException {
        File tempFile = new File(getHuddleBaseTempDir(), tempFileId);
        if (!tempFile.renameTo(dest)) {
            throw new IOException("Error renaming moving temp file from '" + tempFile + "' to '" + dest + "'");
        }
        return dest;
    }

    @Override
    public boolean deleteTempFile(String fileId) throws IOException {
        File file = getTempFile(fileId);
        if (file != null) {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
                return true;
            } else {
                return file.delete();
            }
        }
        return false;
    }

    @Override
    public File unzip(byte[] zippedData, String destinationDirectory)
            throws IOException, ZipException {

        String id = createTempFile(zippedData);
        File zipFile = getTempFile(id);
        File target = unzip(zipFile, destinationDirectory);
        deleteTempFile(id);
        return target;
    }

    @Override
    public File unzip(File zipFile, String destinationDirectory)
            throws IOException, ZipException {

        ZipFile zip = new ZipFile(zipFile);

        File target = new File(getHuddleBaseDir(), destinationDirectory);
        zip.extractAll(target.getAbsolutePath());

        return target;
    }

    private File makeDirectories(File dir) throws IOException {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Unable to create base directory '" + dir);
            }
        }
        return dir;
    }

    private File getHuddleBaseDir() {
        String huddle = huddleContext.getHuddle().getSlug();
        return new File(dataDir, huddle != null ? huddle : "default");
    }

    private File getHuddleBaseTempDir() {
        return new File(getHuddleBaseDir(), "temp");
    }
}
