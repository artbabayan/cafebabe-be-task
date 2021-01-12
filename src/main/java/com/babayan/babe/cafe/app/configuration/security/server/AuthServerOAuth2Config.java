package com.babayan.babe.cafe.app.configuration.security.server;

import com.babayan.babe.cafe.app.configuration.security.encryption.CustomPasswordEncoder;
import com.babayan.babe.cafe.app.configuration.security.custom.CustomTokenEnhancer;
import com.babayan.babe.cafe.app.configuration.security.custom.userdetails.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Arrays;

/**
 * @author artbabayan
 */
@Configuration
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {
    @Value("${security.oauth2.client.registration.clientId}")
    private String clientId;

    @Value("${security.oauth2.client.registration.clientSecret}")
    private String clientSecret;

    @Value("${security.oauth2.client.registration.grantType}")
    private String grantType;

    @Value("${security.oauth2.client.registration.authorizationCode}")
    private String authorizationCode;

    @Value("${security.oauth2.client.registration.token.refreshToken}")
    private String refreshToken;

    @Value("${security.oauth2.client.registration.scopeRead}")
    private String scopeRead;

    @Value("${security.oauth2.client.registration.scopeWrite}")
    private String scopeWrite;

    @Value("${security.oauth2.client.registration.token.duration.access_token}")
    private int accessTokenInSeconds;

    @Value("${security.oauth2.client.registration.token.duration.refresh_token}")
    private int refreshTokenInSeconds;

    private JedisConnectionFactory redisConnectionFactory;
    @Autowired public void setRedisConnectionFactory(JedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    private AuthenticationManager authenticationManager;
    @Autowired @Qualifier("authenticationManagerBean") public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private CustomPasswordEncoder passwordEncoder;
    @Autowired public void setPasswordEncoder(CustomPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private CustomUserDetailsServiceImpl customUserDetailsService;
    @Autowired public void setCustomUserDetailsService(CustomUserDetailsServiceImpl customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder.passwordEncoderUser());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(clientId)
                .secret(new BCryptPasswordEncoder().encode(clientSecret))
                .authorizedGrantTypes(grantType, authorizationCode, refreshToken)
                .scopes(scopeRead, scopeWrite)
                .accessTokenValiditySeconds(accessTokenInSeconds)
                .refreshTokenValiditySeconds(refreshTokenInSeconds);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer()));

        endpoints
                .tokenStore(redisTokenStore())
                .tokenEnhancer(tokenEnhancerChain)

                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService);
    }

    @Bean
    public WebResponseExceptionTranslator loggingExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

                // Carry on handling the exception
                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                OAuth2Exception excBody = responseEntity.getBody();

                return new ResponseEntity<>(excBody, headers, responseEntity.getStatusCode());
            }
        };
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    //endregion

}
