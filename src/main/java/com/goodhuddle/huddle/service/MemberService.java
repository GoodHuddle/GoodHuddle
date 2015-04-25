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

import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.domain.SecurityGroup;
import com.goodhuddle.huddle.domain.SignUpResult;
import com.goodhuddle.huddle.service.exception.*;
import com.goodhuddle.huddle.service.request.member.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MemberService {

    Member getMember(long memberId);

    Member getHuddleOwner();

    Member getOrCreateMemberByEmail(String email);

    Member getLoggedInMember();

    Page<Member> searchMembers(SearchMembersRequest request);


    Member createMember(CreateMemberRequest request) throws UsernameExistsException, EmailExistsException;

    Member joinMailingList(JoinMailingListRequest request) throws EmailExistsException;

    SignUpResult signUpMember(SignUpMemberRequest request) throws NoSuchBlockException;


    Member updateMember(UpdateMemberRequest request) throws UsernameExistsException, EmailExistsException;

    Member updatePassword(long id, String password);

    List<SecurityGroup> getSecurityGroups();

    SecurityGroup createSecurityGroup(SecurityGroup securityGroup);

    void processContactUsRequest(ContactUsRequest request);

    void sendPasswordResetEmail(ForgotPasswordRequest request) throws NoSuchMemberException, ResetPasswordFailedException, EmailsNotSetupException;

    Member getMemberForPasswordResetCode(String resetCode);

    Member resetPassword(ResetPasswordRequest request);


}
