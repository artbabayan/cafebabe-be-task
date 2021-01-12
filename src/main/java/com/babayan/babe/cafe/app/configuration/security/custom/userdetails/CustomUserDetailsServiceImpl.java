package com.babayan.babe.cafe.app.configuration.security.custom.userdetails;

import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.service.UserService;
import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.UnauthorizedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author artbabayan
 */
@Log4j2
@Service(value = "userDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private UserService userService;
    @Autowired public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user;
        try {
            user = userService.findByEmail(email);
        } catch (NotFoundException ex) {
            log.error(String.format("User with email=%s not found", email));
            throw new UnauthorizedException("Incorrect email or password");
        }
        return new CustomUserDetails(user);
    }

}
