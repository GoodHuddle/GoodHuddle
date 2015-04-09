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

import com.goodhuddle.huddle.domain.Layout;
import com.goodhuddle.huddle.repository.LayoutRepository;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.LayoutService;
import com.goodhuddle.huddle.service.exception.LayoutExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LayoutServiceImpl implements LayoutService {

    private static final Logger log = LoggerFactory.getLogger(LayoutServiceImpl.class);

    private final HuddleService huddleService;
    private final LayoutRepository layoutRepository;

    @Autowired
    public LayoutServiceImpl(HuddleService huddleService, LayoutRepository layoutRepository) {
        this.huddleService = huddleService;
        this.layoutRepository = layoutRepository;
    }

    @Override
    public List<Layout> getLayouts(long themeId) {
        return layoutRepository.findByHuddleAndThemeId(huddleService.getHuddle(), themeId);
    }

    @Override
    public List<Layout> getLayoutsForCurrentTheme(Layout.Type type) {
        return layoutRepository.findByHuddleAndTypeAndThemeActiveIsTrue(huddleService.getHuddle(), type);
    }

    @Override
    public Layout getLayout(long layoutId) {
        return layoutRepository.findByHuddleAndId(huddleService.getHuddle(), layoutId);
    }

    @Override
    public Layout createLayout(Layout layout) throws LayoutExistsException {
        Layout existing = layoutRepository.findByHuddleAndThemeIdAndName(huddleService.getHuddle(),
                layout.getTheme().getId(), layout.getName());
        if (existing != null) {
            throw new LayoutExistsException("Layout '"
                    + layout.getName() + "' already exists in theme with ID " + layout.getTheme().getId());
        }
        layout = layoutRepository.save(layout);
        log.info("Layout '{}' created with ID {}", layout.getName(), layout.getId());
        return layout;
    }

    @Override
    public void deleteLayout(long layoutId) {
        Layout layout = layoutRepository.findByHuddleAndId(huddleService.getHuddle(), layoutId);
        if (layout != null) {
            layoutRepository.delete(layout);
            log.info("Deleted layout with ID {}", layoutId);
        } else {
            log.debug("Ignoring request to delete theme with ID {} as it does not exist in current huddle", layoutId);
        }
    }
}
