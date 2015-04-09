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

package com.goodhuddle.huddle.service.request.member;

import com.goodhuddle.huddle.service.request.common.MatchType;

import java.util.List;

public class MemberTagFilter {

    private TagSet included;
    private TagSet excluded;

    public TagSet getIncluded() {
        return included;
    }

    public void setIncluded(TagSet included) {
        this.included = included;
    }

    public TagSet getExcluded() {
        return excluded;
    }

    public void setExcluded(TagSet excluded) {
        this.excluded = excluded;
    }

    //-------------------------------------------------------------------------

    public static final class TagSet {

        private List<Long> tagIds;
        private MatchType matchType;

        public List<Long> getTagIds() {
            return tagIds;
        }

        public void setTagIds(List<Long> tagIds) {
            this.tagIds = tagIds;
        }

        public MatchType getMatchType() {
            return matchType;
        }

        public void setMatchType(MatchType matchType) {
            this.matchType = matchType;
        }
    }
}
