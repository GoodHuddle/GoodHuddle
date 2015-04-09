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

package com.goodhuddle.huddle.web.site.handlebars.helper;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LayoutHelper {

    public String extend(String layout, Options options) throws IOException {

        // Parse sections and discard output (this adds section content to context.data)
        options.fn(options.context);

        Template template = options.handlebars.compile(layout);
        return template.apply(options.context);
    }

    public String include(String layout, Options options) throws IOException {
        Object prefix = options.hash("prefix");
        if (prefix != null) {
            layout = prefix + layout;
        }
        Template template = options.handlebars.compile(layout);
        return template.apply(options.context);
    }

    public CharSequence section(String name, Options options) throws IOException {
        CharSequence result = options.fn(options.context);
        List<String> addedContentIds = new ArrayList<>();

        List<SectionContent> sectionContentList = getSectionContentList(options.context, name);
        for (SectionContent sectionContent : sectionContentList) {
            String contentId = sectionContent.getContentId();
            if (contentId == null || !addedContentIds.contains(contentId)) {
                switch (sectionContent.getAction()) {
                    case prepend:
                        result = sectionContent.getContent() + result;
                        break;
                    case append:
                        result = result + sectionContent.getContent();
                        break;
                    case replace:
                        result = sectionContent.getContent();
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected action: " + sectionContent.getAction());
                }
                addedContentIds.add(contentId);
            }
        }

        return result;
    }

    public String prepend(String sectionName, Options options) throws IOException {
        addSectionContent(options.context, sectionName, Action.prepend, options.fn, getContentId(options));
        return null;
    }

    public String append(String sectionName, Options options) throws IOException {
        addSectionContent(options.context, sectionName, Action.append, options.fn, getContentId(options));
        return null;
    }

    public String replace(String sectionName, Options options) throws IOException {
        addSectionContent(options.context, sectionName, Action.replace, options.fn, getContentId(options));
        return null;
    }


    //-------------------------------------------------------------------------

    private String getContentId(Options options) {
        return options.hash("addOnlyOnce");
    }

    protected void addSectionContent(Context context, String sectionName, Action action, Template template, String contentId)
            throws IOException {
        String content = template.apply(context);
        getSectionContentList(context, sectionName).add(new SectionContent(action, content, contentId));
    }

    protected List<SectionContent> getSectionContentList(Context context, String sectionName) {
        Map<String, List<SectionContent>> sectionMap = context.data("_sections");
        if (sectionMap == null) {
            sectionMap = new HashMap<>();
            context.data("_sections", sectionMap);
        }
        List<SectionContent> sectionContentList = sectionMap.get(sectionName);
        if (sectionContentList == null) {
            sectionContentList = new ArrayList<>();
            sectionMap.put(sectionName, sectionContentList);
        }
        return sectionContentList;
    }

    //-------------------------------------------------------------------------

    enum Action {
        prepend, append, replace
    }

    private final class SectionContent {

        private Action action;
        private String content;
        private String contentId;

        private SectionContent(Action action, String content, String contentId) {
            this.action = action;
            this.content = content;
            this.contentId = contentId;
        }

        public Action getAction() {
            return action;
        }

        public String getContent() {
            return content;
        }

        public String getContentId() {
            return contentId;
        }
    }
}
