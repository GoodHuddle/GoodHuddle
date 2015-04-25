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
import com.goodhuddle.huddle.repository.BlockIdMappingRepository;
import com.goodhuddle.huddle.repository.PageRepository;
import com.goodhuddle.huddle.repository.PageRevisionRepository;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.service.PageService;
import com.goodhuddle.huddle.service.exception.PageSlugExistsException;
import com.goodhuddle.huddle.service.impl.file.FileStore;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import com.goodhuddle.huddle.service.request.menu.UpdateMenuItemDetailsRequest;
import com.goodhuddle.huddle.service.request.page.CreatePageRequest;
import com.goodhuddle.huddle.service.request.page.UpdatePageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class PageServiceImpl implements PageService {

    private static final Logger log = LoggerFactory.getLogger(PageServiceImpl.class);

    private final HuddleService huddleService;
    private final SecurityHelper securityHelper;
    private final PageRepository pageRepository;
    private final PageRevisionRepository pageRevisionRepository;
    private final BlockIdMappingRepository blockIdMappingRepository;
    private final MenuService menuService;
    private final FileStore fileStore;

    @Autowired
    public PageServiceImpl(HuddleService huddleService,
                           SecurityHelper securityHelper,
                           PageRepository pageRepository,
                           PageRevisionRepository pageRevisionRepository,
                           BlockIdMappingRepository blockIdMappingRepository,
                           MenuService menuService,
                           FileStore fileStore) {
        this.huddleService = huddleService;
        this.securityHelper = securityHelper;
        this.pageRepository = pageRepository;
        this.pageRevisionRepository = pageRevisionRepository;
        this.blockIdMappingRepository = blockIdMappingRepository;
        this.menuService = menuService;
        this.fileStore = fileStore;
    }

    @Override
    public List<Page> getPages() {
        return pageRepository.findByHuddle(huddleService.getHuddle());
    }

    @Override
    public Page getPage(long pageId) {
        return pageRepository.findByHuddleAndId(huddleService.getHuddle(), pageId);
    }

    @Override
    public Page getPageForSlug(String slug) {
        return pageRepository.findByHuddleAndSlug(huddleService.getHuddle(), slug);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Page createPage(CreatePageRequest request) throws PageSlugExistsException {

        Page slugOwner = pageRepository.findByHuddleAndSlug(huddleService.getHuddle(), request.getSlug());
        if (slugOwner != null) {
            throw new PageSlugExistsException("The page slug '" + request.getSlug() + "' is already in use");
        }

        PageContent content = request.getContent();
        if (content == null) {
            content = new PageContent();
        }
        Page page = pageRepository.save(new Page(huddleService.getHuddle(),
                request.getTitle(), request.getSlug(), request.getLayout(), content));

        updateBlockIds(page);

        MenuItem menuItem = menuService.createMenuItem(
                request.getTitle(), request.getMenuId(), request.getParentItemId(), request.getPosition(),
                MenuItem.Type.page, page.getId(), page.getUrl());

        page.setMenuItem(menuItem);
        pageRepository.save(page);

        log.info("Page '{}' created with ID {}", page.getSlug(), page.getId());
        return page;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Page updatePage(UpdatePageRequest request) throws PageSlugExistsException {

        Page slugOwner = pageRepository.findByHuddleAndSlug(huddleService.getHuddle(), request.getSlug());
        if (slugOwner != null && !(slugOwner.getId().equals(request.getId()))) {
            throw new PageSlugExistsException("The page slug '" + request.getSlug() + "' is already in use");
        }

        Page page = pageRepository.findByHuddleAndId(huddleService.getHuddle(), request.getId());
        page.update(request.getTitle(), request.getSlug(), request.getLayout(), request.getContent());
        updateBlockIds(page);
        page = pageRepository.save(page);

        Member currentMember = securityHelper.getCurrentMember();
        pageRevisionRepository.save(new PageRevision(page, currentMember));

        menuService.updateMenuItemDetails(new UpdateMenuItemDetailsRequest(
                page.getMenuItem().getId(), page.getTitle(), page.getUrl()
        ));
        log.info("Updated page with ID {}", page.getId());
        return page;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deletePage(long pageId) {
        Page page = pageRepository.findByHuddleAndId(huddleService.getHuddle(), pageId);
        if (page != null) {
            pageRepository.delete(pageId);
            menuService.deleteMenuItem(page.getMenuItem().getId(), true);
            log.info("Deleted page with ID {}", pageId);
        } else {
            log.debug("Ignoring delete request of page {} as it does not exist", pageId);
        }
    }

    @Override
    public File saveAttachment(long pageId, String fileName, byte[] data) throws IOException {
        return fileStore.createFile(getAttachmentPath(pageId, fileName), data);
    }

    @Override
    public File getAttachment(long pageId, String fileName) throws IOException {
        String path = getAttachmentPath(pageId, fileName);
        log.debug("Downloading file attachment: {}", path);
        return fileStore.getFile(path);
    }

    @Override
    public PageContent.Block getBlock(long blockId) {
        BlockIdMapping mapping = blockIdMappingRepository.findOne(blockId);
        if (mapping != null) {
            PageContent content = mapping.getPage().getContent();
            if (content != null) {
                for (PageContent.Row row : content.getRows()) {
                    for (PageContent.Cell cell : row.getCells()) {
                        for (PageContent.Block block : cell.getBlocks()) {
                            if (block.getId() != null && block.getId().equals(blockId)) {
                                return block;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getAttachmentPath(long pageId, String fileName) {
        return "/attachments/page/" + pageId + "/" + fileName;
    }

    private void updateBlockIds(Page page) {

        // delete old mapping
        blockIdMappingRepository.deleteByPage(page);

        // create new mappings
        PageContent content = page.getContent();
        if (content != null) {
            for (PageContent.Row row : content.getRows()) {
                for (PageContent.Cell cell : row.getCells()) {
                    for (PageContent.Block block : cell.getBlocks()) {
                        BlockIdMapping idMapping = blockIdMappingRepository.save(new BlockIdMapping(page));
                        block.setId(idMapping.getId());
                    }
                }
            }
        }
    }
}
