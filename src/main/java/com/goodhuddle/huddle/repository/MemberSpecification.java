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

package com.goodhuddle.huddle.repository;

import com.goodhuddle.huddle.domain.Huddle;
import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.domain.Tag;
import com.goodhuddle.huddle.service.request.common.MatchType;
import com.goodhuddle.huddle.service.request.member.MemberTagFilter;
import com.goodhuddle.huddle.service.request.member.SearchMembersRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class MemberSpecification {

    public static Specification<Member> search(final Huddle huddle, final SearchMembersRequest request) {
        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> member, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate conjunction = builder.conjunction();

                // huddle
                conjunction.getExpressions().add(builder.equal(member.get("huddle"), huddle));

                // keywords

                if (StringUtils.isNotBlank(request.getKeywords())) {
                    String[] terms = StringUtils.split(request.getKeywords());
                    for (String keyword : terms) {
                        if (keyword != null && keyword.length() > 0) {

                            String matchTerm = "%" + keyword.toLowerCase() + "%";
                            conjunction.getExpressions().add(builder.or(
                                    builder.like(builder.lower(member.<String>get("username")), matchTerm),
                                    builder.like(builder.lower(member.<String>get("email")), matchTerm),
                                    builder.like(builder.lower(member.<String>get("firstName")), matchTerm),
                                    builder.like(builder.lower(member.<String>get("lastName")), matchTerm)
                            ));
                        }
                    }
                }

                // security groups

                if (CollectionUtils.isNotEmpty(request.getSecurityGroupIds())) {

                    Join<Object, Object> securityGroup = member.join("securityGroup",
                            request.isIncludeNoAccess() ? JoinType.LEFT : JoinType.INNER);

                    Predicate disjunction = builder.disjunction();
                    for (Long id : request.getSecurityGroupIds()) {
                        disjunction.getExpressions().add(builder.equal(securityGroup.get("id"), id));
                    }
                    if (request.isIncludeNoAccess()) {
                        disjunction.getExpressions().add(builder.isNull(securityGroup.get("id")));
                    }
                    conjunction.getExpressions().add(disjunction);

                } else if (request.isIncludeNoAccess()) {

                    conjunction.getExpressions().add(builder.isNull(member.get("securityGroup")));

                }

                // tags

                MemberTagFilter tagFilter = request.getTags();
                if (tagFilter != null) {

                    if (tagFilter.getIncluded() != null) {

                        if (CollectionUtils.isNotEmpty(tagFilter.getIncluded().getTagIds())) {

                            MemberTagFilter.TagSet included = request.getTags().getIncluded();
                            MatchType matchType = included.getMatchType();
                            Predicate tagPredicate = matchType.equals(MatchType.all)
                                    ? builder.conjunction() : builder.disjunction();

                            for (Long tagId : included.getTagIds()) {
                                Subquery<Member> sq = query.subquery(Member.class);
                                Root<Member> subMember = sq.from(Member.class);
                                Join<Member, Tag> tag = subMember.join("tags");
                                sq.select(subMember).where(builder.equal(tag.get("id"), tagId));
                                tagPredicate.getExpressions().add(builder.in(member).value(sq));
                            }

                            conjunction.getExpressions().add(tagPredicate);
                        }
                    }

                    if (tagFilter.getExcluded() != null) {

                        if (CollectionUtils.isNotEmpty(tagFilter.getExcluded().getTagIds())) {

                            MemberTagFilter.TagSet excluded = request.getTags().getExcluded();
                            MatchType matchType = excluded.getMatchType();
                            Predicate tagPredicate = matchType.equals(MatchType.all)
                                    ? builder.disjunction() : builder.conjunction();

                            for (Long tagId : excluded.getTagIds()) {
                                Subquery<Member> sq = query.subquery(Member.class);
                                Root<Member> subMember = sq.from(Member.class);
                                Join<Member, Tag> tag = subMember.join("tags");
                                sq.select(subMember).where(builder.equal(tag.get("id"), tagId));
                                tagPredicate.getExpressions().add(builder.in(member).value(sq).not());
                            }

                            conjunction.getExpressions().add(tagPredicate);
                        }
                    }

                }

                return conjunction;
            }
        };
    }
}
