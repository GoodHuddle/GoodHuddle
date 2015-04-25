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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodhuddle.huddle.domain.*;
import com.goodhuddle.huddle.repository.HuddleRepository;
import com.goodhuddle.huddle.repository.MemberRepository;
import com.goodhuddle.huddle.repository.SecurityGroupRepository;
import com.goodhuddle.huddle.service.BlogService;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.MenuService;
import com.goodhuddle.huddle.service.PageService;
import com.goodhuddle.huddle.service.exception.*;
import com.goodhuddle.huddle.service.request.blog.CreateBlogRequest;
import com.goodhuddle.huddle.service.request.huddle.CreateHuddleRequest;
import com.goodhuddle.huddle.service.request.huddle.SetupDefaultPagesRequest;
import com.goodhuddle.huddle.service.request.huddle.UpdateHuddleRequest;
import com.goodhuddle.huddle.service.request.page.CreatePageRequest;
import com.goodhuddle.huddle.web.HuddleContext;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class HuddleServiceImpl implements HuddleService {

    private static final Logger log = LoggerFactory.getLogger(HuddleServiceImpl.class);

    @Value("${goodhuddle.payments.liveSecretKey:}")
    private String liveSecretKey;

    private final HuddleRepository huddleRepository;
    private final SecurityGroupRepository securityGroupRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final HuddleContext huddleContext;

    private MenuService menuService;
    private PageService pageService;
    private BlogService blogService;

    @Autowired
    public HuddleServiceImpl(HuddleRepository huddleRepository,
                             SecurityGroupRepository securityGroupRepository,
                             MemberRepository memberRepository,
                             PasswordEncoder passwordEncoder,
                             ObjectMapper objectMapper,
                             HuddleContext huddleContext) {

        this.huddleRepository = huddleRepository;
        this.securityGroupRepository = securityGroupRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.huddleContext = huddleContext;
    }

    @Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @Override
    public Huddle getHuddle() {
        return huddleContext.getHuddle();
    }

    public Huddle getHuddle(String slug) {
        return huddleRepository.findBySlug(slug);
    }

    @Override
    public List<Huddle> getHuddles() {
        return huddleRepository.findAll();
    }

    @Transactional(readOnly = false)
    public Huddle createHuddle(CreateHuddleRequest request)
            throws HuddleExistsException, InvalidHuddleInvitationCodeException, InvalidHuddleSlugException {

        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        if (p.matcher(request.getSlug()).find()) {
            throw new InvalidHuddleSlugException("The huddle slug '" + request.getSlug()
                    + "' contains invalid characters (should be letters or digits only)");
        }

        // check the huddle URL isn't a reserved word
        if (ArrayUtils.contains(new String[]{
                "admin", "tools", "build", "themes", "goodhuddle", "default", "webhooks", "huddle", "blog", "official",
                "pricing", "about", "help"},
                request.getSlug())) {
            throw new HuddleExistsException("'" + request.getSlug() + "' is a reserved word and cannot be used for a huddle slug");
        }

        request.setSlug(request.getSlug().toLowerCase());

        // check the huddle slug is available
        Huddle existingHuddle = getHuddle(request.getSlug());
        if (existingHuddle != null) {
            log.error("Huddle '" + request.getSlug() + "' already exists");
            throw new HuddleExistsException("Huddle '" + request.getSlug() + "' already exists");
        }

        Huddle huddle = huddleRepository.save(new Huddle(request.getSlug(), request.getName(),
                request.getBaseUrl(), request.getDescription()));

        huddleContext.setHuddle(huddle);

        try {
            menuService.createMenu(MenuService.MAIN);
            menuService.createMenu(MenuService.UNLINKED);
        } catch (MenuExistsException e) {
            throw new HuddleExistsException("Error creating menu", e);
        }

        securityGroupRepository.save(
                new SecurityGroup(huddle, "member", "Member", "Standard member access", false,
                        Arrays.asList(Permissions.Member.access)));

        SecurityGroup adminSecurityGroup = securityGroupRepository.save(
                new SecurityGroup(huddle, "admin", "Administrator", "Access all areas", true, Permissions.all));

        Member admin = new Member(
                huddle,
                request.getAdminUsername(),
                request.getAdminFirstName(),
                request.getAdminLastName(),
                request.getAdminEmail(),
                null,
                null,
                adminSecurityGroup,
                passwordEncoder.encode(request.getAdminPassword()));
        admin.setHuddleOwner(true);
        memberRepository.save(admin);

        log.info("Huddle created '{}' at url '{}' with ID {}", request.getName(), request.getBaseUrl(), huddle.getId());
        return huddle;
    }

    @Override
    public Huddle updateHuddle(UpdateHuddleRequest request) {
        Huddle huddle = getHuddle();
        huddle.update(request.getName(), request.getDescription(), request.isComingSoon());
        huddleRepository.save(huddle);
        log.info("Huddle details updated");
        return huddle;
    }

    @Override
    public void setupDefaultPages(SetupDefaultPagesRequest request)
            throws PageSlugExistsException, BlogExistsException,
            BlogPostExistsException, HuddleExistsException,
                   IOException {

        Menu mainMenu = menuService.getMainMenu();

        if (request.isHome()) {
            log.info("Creating default home page");
            PageContent content = loadDefaultPageContent("home");
            pageService.createPage(new CreatePageRequest(mainMenu.getId(), "Home", "home", null, content));
        }

        if (request.isBlog()) {
            log.info("Creating default blog");
            Blog blog = blogService.createBlog(new CreateBlogRequest(mainMenu.getId(), "Blog", "blog", null, null));
            //PageContent content = loadDefaultPageContent("post");
            //blogService.createBlogPost(new CreateBlogPostRequest(blog.getId(), "Our new site is live", "live", null, content));
        }

        if (request.isAbout()) {
            log.info("Creating default about page");
            PageContent content = loadDefaultPageContent("about");
            pageService.createPage(new CreatePageRequest(mainMenu.getId(), "About", "about", null, content));
        }

        if (request.isContact()) {
            log.info("Creating default contact page");
            PageContent content = loadDefaultPageContent("contact");
            pageService.createPage(new CreatePageRequest(mainMenu.getId(), "Contact", "contact", null, content));
        }

        if (request.isDonate()) {
            Menu unlinkedMenu = menuService.getUnlinkedMenu();

            log.info("Creating default donation page");
            PageContent content = loadDefaultPageContent("donate");
            pageService.createPage(new CreatePageRequest(mainMenu.getId(), "Donate", "donate", null, content));

            log.info("Creating default donation thank you page");
            content = loadDefaultPageContent("donate-thanks");
            pageService.createPage(new CreatePageRequest(unlinkedMenu.getId(), "Thank you", "donate-thanks", null, content));
        }
    }

    @Override
    public Huddle setupWizardComplete(boolean complete) {
        Huddle huddle = getHuddle();
        huddle.setSetupWizardComplete(complete);
        huddleRepository.save(huddle);
        log.info("Setup wizard complete marked as '{}' for Huddle", complete);
        return huddle;
    }

    private PageContent loadDefaultPageContent(String slug) throws HuddleExistsException {
        try {
            ClassPathResource resource = new ClassPathResource("/setup/default-" + slug + "-content.json");
            try (InputStream in = resource.getInputStream()) {
                return objectMapper.readValue(in, PageContent.class);
            }
        } catch (IOException e) {
            throw new HuddleExistsException("Error reading default content for '" + slug + "'", e);
        }
    }
}
