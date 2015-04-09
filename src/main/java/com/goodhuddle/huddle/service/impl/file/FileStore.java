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


import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;

public interface FileStore {

    File createFile(String directoryName, String fileName) throws IOException;

    File createFile(String fullPath, byte[] data) throws IOException;

    File createFile(String directoryPath, String fileName, byte[] data) throws IOException;

    File getFile(String path);

    File getFile(String directoryPath, String fileName);

    boolean delete(String path) throws IOException;

    boolean delete(String directoryName, String fileName) throws IOException;

    File moveFile(File src, String dest);

    File unzip(byte[] bytes, String destinationDirectory) throws IOException, ZipException;

    File unzip(File file, String destinationDirectory) throws IOException, ZipException;

    String createTempFile(byte[] data) throws IOException;

    String createTempDirectory() throws IOException;

    File getTempFile(String tempFileId);

    File moveTempFile(String tempFileId, String path) throws IOException;

    File moveTempFile(String tempFileId, String directoryName, String fileName) throws IOException;

    boolean deleteTempFile(String themeBundleId) throws IOException;

}
