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

import au.com.bytecode.opencsv.CSVWriter;
import com.goodhuddle.huddle.domain.Member;
import com.goodhuddle.huddle.domain.MemberBulkSet;
import com.goodhuddle.huddle.repository.MemberRepository;
import com.goodhuddle.huddle.service.ExportService;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MemberBulkActionService;
import com.goodhuddle.huddle.service.impl.file.FileStore;
import com.goodhuddle.huddle.service.request.member.ExportMembersRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@Scope("session")
public class ExportServiceImpl implements ExportService {

    private static final Logger log = LoggerFactory.getLogger(ExportServiceImpl.class);

    private final HuddleService huddleService;
    private final MemberBulkActionService memberBulkActionService;
    private final MemberRepository memberRepository;
    private final FileStore fileStore;

    @Autowired
    public ExportServiceImpl(HuddleService huddleService,
                             MemberBulkActionService memberBulkActionService,
                             MemberRepository memberRepository,
                             FileStore fileStore) {

        this.huddleService = huddleService;
        this.memberBulkActionService = memberBulkActionService;
        this.memberRepository = memberRepository;
        this.fileStore = fileStore;
    }

    @Override
    public String exportMembers(ExportMembersRequest request) throws IOException {

        MemberBulkSet memberBulkSet = memberBulkActionService.getMemberBulkSet(request.getBulkSetId());

        String fileName = "members-" + System.currentTimeMillis() + ".csv";
        File file = fileStore.createFile("exports", fileName);
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            writer.writeNext(new String[] { "Username", "First name", "Last name", "Email"});

            String[] entries = new String[4];
            for (Long memberId : memberBulkSet.getMemberIds()) {
                Member member = memberRepository.findOne(memberId);
                entries[0] = member.getUsername();
                entries[1] = member.getFirstName();
                entries[2] = member.getLastName();
                entries[3] = member.getEmail();
                writer.writeNext(entries);
            }
        }
        log.debug("Exported {} members to: {}", memberBulkSet.getMemberIds().size(),  file);
        return fileName;
    }

    @Override
    public File getExportFile(String fileName) {
        return fileStore.getFile("exports", fileName);
    }
}
