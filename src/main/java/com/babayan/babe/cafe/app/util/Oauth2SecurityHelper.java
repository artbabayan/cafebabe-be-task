package com.babayan.babe.cafe.app.util;

import com.babayan.babe.cafe.app.configuration.security.custom.userdetails.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author by artbabayan
 */
public class Oauth2SecurityHelper {

    private Oauth2SecurityHelper() {
        throw new IllegalStateException("Helper class");
    }

    public static Long retrieveUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
            if (principal != null) {
                return principal.getId();
            }
        }
        return null;
    }

}
