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

import com.github.jknack.handlebars.Options;
import com.goodhuddle.huddle.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SignUpCountHelper {

    private final MemberService memberService;

    @Autowired
    public SignUpCountHelper(MemberService memberService) {
        this.memberService = memberService;
    }

    public CharSequence signUpCount(Options options) throws IOException {
        Object tagIdsObject = options.hash("tagIds");
        int count;
        if (tagIdsObject != null ) {
            String tagIdsStr = String.valueOf(tagIdsObject);
            tagIdsStr = tagIdsStr.substring(1, tagIdsStr.length() - 1); // strip brackets
            // todo parse array - currently we assume only one item
            long tagId = Long.parseLong(tagIdsStr);
            count = memberService.countActiveMembers(tagId);
        } else {
            count = memberService.countActiveMembers();
        }
        return String.valueOf(count);
    }
}
