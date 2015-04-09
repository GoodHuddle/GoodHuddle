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

import com.goodhuddle.huddle.domain.Page;
import com.goodhuddle.huddle.service.exception.PageSlugExistsException;
import com.goodhuddle.huddle.service.request.page.CreatePageRequest;
import com.goodhuddle.huddle.service.request.page.UpdatePageRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PageService {

    List<Page> getPages();

    Page getPage(long pageId);

    Page getPageForSlug(String slug);

    Page createPage(CreatePageRequest request) throws PageSlugExistsException;

    Page updatePage(UpdatePageRequest request) throws PageSlugExistsException;

    void deletePage(long pageId);

    File saveAttachment(long pageId, String fileName, byte[] data) throws IOException;

    File getAttachment(long pageId, String fileName) throws IOException;

}
