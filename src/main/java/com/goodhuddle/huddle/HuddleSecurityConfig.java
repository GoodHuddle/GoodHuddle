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

import com.goodhuddle.huddle.domain.Permissions;
import com.goodhuddle.huddle.service.impl.security.HuddleUserDetailsService;
import com.goodhuddle.huddle.service.impl.security.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HuddleSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private HuddleUserDetailsService huddleUserDetailsService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(huddleUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // todo support csrf properly
        http.csrf().disable();

        http
            .authorizeRequests()
                .antMatchers("/member/signup").permitAll()
                .antMatchers("/member/*")
                    .hasAuthority(Permissions.Member.access)
                .anyRequest().permitAll()
        .and()
            .formLogin()
                .loginPage("/member/login")
                .defaultSuccessUrl("/member/profile")
                .permitAll()
        .and()
            .logout()
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




    //-------------------------------------------------------------------------

    @Configuration
    @Order(1)
    public static class ApiSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private HuddleUserDetailsService huddleUserDetailsService;

        @Autowired
        private LoginSuccessHandler loginSuccessHandler;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(huddleUserDetailsService)
                    .passwordEncoder(passwordEncoder);
        }


        protected void configure(HttpSecurity http) throws Exception {

            // todo support csrf properly
            http.csrf().disable();

            http
                .authorizeRequests()
                    .antMatchers("/admin/index.html").hasAuthority(Permissions.Admin.access)
                    .antMatchers("/api/**").hasAuthority(Permissions.Admin.access)
//                .and()
//                    .requiresChannel()
//                        .antMatchers("/admin/**").requiresSecure()
//                        .antMatchers("/api/**").requiresSecure()
                .and()
                    .formLogin()
                        .loginPage("/admin/login")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/admin/login.html?error=true")
                        .permitAll()
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(new DelegatingAuthEntryPoint())
                .and()
                    .logout()
                        .logoutUrl("/admin/logout")
                        .permitAll();

        }
    }

    //-------------------------------------------------------------------------

    private static class DelegatingAuthEntryPoint implements AuthenticationEntryPoint {

        private Http403ForbiddenEntryPoint http403;
        private LoginUrlAuthenticationEntryPoint loginUrl;

        private DelegatingAuthEntryPoint() {
            this.http403 = new Http403ForbiddenEntryPoint();
            this.loginUrl = new LoginUrlAuthenticationEntryPoint("/admin/login");
        }

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
                throws IOException, ServletException {
            if (request.getServletPath().startsWith("/api")) {
                http403.commence(request, response, authException);
            } else {
                loginUrl.commence(request, response, authException);
            }
        }
    }
}
