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

package com.goodhuddle.huddle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.goodhuddle.huddle.service.HuddleService;
import com.goodhuddle.huddle.service.impl.security.SecurityHelper;
import com.goodhuddle.huddle.web.HuddleGlobalInterceptor;
import com.goodhuddle.huddle.web.site.handlebars.HuddleViewResolver;
import org.apache.catalina.Context;
import org.apache.catalina.deploy.SecurityCollection;
import org.apache.catalina.deploy.SecurityConstraint;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@Configuration
public class HuddleWebConfig extends WebMvcConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HuddleWebConfig.class);

    @Value("${goodhuddle.admin.root:}")
    public String adminRoot;

    @Autowired
    private HuddleService huddleService;

    @Autowired
    private HuddleViewResolver huddleViewResolver;

    @Autowired
    private SecurityHelper securityHelper;

    @Bean
    public ViewResolver viewResolver() {
        return huddleViewResolver;
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory get() {
        return new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("20MB");
        factory.setMaxRequestSize("20MB");
        return factory.createMultipartConfig();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (StringUtils.isNotBlank(adminRoot)) {
            log.info("Setting document root for Admin UI to: {}", adminRoot);
            registry.addResourceHandler("/admin/**").addResourceLocations(adminRoot);
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin/login").setViewName("admin/login");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HuddleGlobalInterceptor interceptor = new HuddleGlobalInterceptor(huddleService, securityHelper);
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter jacksonConvertor(){
        MappingJackson2HttpMessageConverter convertor= new MappingJackson2HttpMessageConverter();
        convertor.setObjectMapper(objectMapper());
        return convertor;
    }

    private ObjectMapper objectMapper() {

        Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();

        bean.setIndentOutput(true);
        bean.setSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        bean.afterPropertiesSet();

        ObjectMapper objectMapper = bean.getObject();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JodaModule());

        return objectMapper;
    }
}
