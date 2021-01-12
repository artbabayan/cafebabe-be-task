package com.babayan.babe.cafe.app.configuration.security.encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author artbabayan
 */
@Configuration
public class CustomPasswordEncoder {

    @Bean
    public PasswordEncoder passwordEncoderUser() {
        return new BCryptPasswordEncoder();
    }

}
