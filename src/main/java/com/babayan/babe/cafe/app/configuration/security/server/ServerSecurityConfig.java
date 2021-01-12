package com.babayan.babe.cafe.app.configuration.security.server;

import com.babayan.babe.cafe.app.configuration.security.encryption.CustomPasswordEncoder;
import com.babayan.babe.cafe.app.configuration.security.custom.userdetails.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author artbabayan
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomUserDetailsServiceImpl userDetailsService;
    @Autowired public void setUserDetailsService(CustomUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private CustomPasswordEncoder customPasswordEncoder;
    @Autowired public void setUserPasswordEncoder(CustomPasswordEncoder customPasswordEncoder) {
        this.customPasswordEncoder = customPasswordEncoder;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(customPasswordEncoder.passwordEncoderUser());
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring()
                .antMatchers("/api/v1/auth/sign-in")
                .antMatchers("/api/v1/auth/sign-up")
                .antMatchers("/actuator/**")
                .antMatchers("/instances/**")
                .antMatchers("/v2/api-docs/**",
                        "swagger-ui.html#",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html/**",
                        "/swagger-ui/**",
                        "/webjars/**");
    }

}
