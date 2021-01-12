package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.service.OAuth2AccessTokenService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author by artbabayan
 */
@Log4j2
@Transactional(readOnly = true)
@Service
public class OAuth2AccessTokenServiceImpl implements OAuth2AccessTokenService {
    @Value("${security.oauth2.client.registration.accessTokenUri}")
    private String accessTokenUri;

    @Value("${security.oauth2.client.registration.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.registration.clientSecret}")
    private String clientSecret;

    @Value("${security.oauth2.client.registration.grantType}")
    private String grantType;

    @Value("${security.oauth2.client.registration.token.refreshToken}")
    private String grantTypeRefreshToken;

    @Value("${security.oauth2.client.registration.scopeRead}")
    private String scopeRead;

    @Value("${security.oauth2.client.registration.scopeWrite}")
    private String scopeWrite;

    //region <SERVICE>

    @PostConstruct
    private void init() {
        log.info("OAuth2AccessToken service initialized");
    }

    /**
     * Gets oauth2 token details according to registered username and password
     */
    @Override
    public OAuth2AccessToken obtainOAuth2AccessToken(String username, String password) {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(clientId);
        resourceDetails.setClientSecret(clientSecret);
        resourceDetails.setScope(Arrays.asList(scopeRead, scopeWrite));
        resourceDetails.setGrantType(grantType);
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);

        return oAuth2RestTemplate.getAccessToken();
    }

    /**
     * Revokes(remove) access token
     */
    @Override
    public void revokeAccessToken() {
        //ToDo: should be implemented this method(usage: for user logout)
    }

    //endregion

}
