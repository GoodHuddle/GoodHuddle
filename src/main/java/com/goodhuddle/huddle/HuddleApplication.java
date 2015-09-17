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

import com.ecwid.mailchimp.MailChimpClient;
import com.goodhuddle.huddle.service.impl.mail.EmailQueueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories("com.goodhuddle.huddle.repository")
@EnableScheduling
public class HuddleApplication {

    private static final Logger log = LoggerFactory.getLogger(HuddleApplication.class);

    @Value("${keystore.file}")
    private Resource keystoreFile;

    @Value("${keystore.password}")
    private String keystorePassword;

    public static void main(String[] args) {
        SpringApplication.run(HuddleApplication.class, args);
    }

    @Bean
    public MessageListenerAdapter sendEmailAdapter(EmailQueueProcessor receiver) {
        MessageListenerAdapter messageListener
                = new MessageListenerAdapter(receiver);
        messageListener.setDefaultListenerMethod("sendMailout");
        return messageListener;
    }

    @Bean
    public SimpleMessageListenerContainer container(MessageListenerAdapter messageListener,
                                                    ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setMessageListener(messageListener);
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName(EmailQueueProcessor.QUEUE_NAME);
        return container;
    }

    @Bean
    public MailChimpClient mailChimpClient() {
        return new MailChimpClient();
    }

}
