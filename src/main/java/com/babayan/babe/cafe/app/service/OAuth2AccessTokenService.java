package com.babayan.babe.cafe.app.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author by artbabayan
 */
public interface OAuth2AccessTokenService {

    OAuth2AccessToken obtainOAuth2AccessToken(String username, String password);

    void revokeAccessToken();

}
