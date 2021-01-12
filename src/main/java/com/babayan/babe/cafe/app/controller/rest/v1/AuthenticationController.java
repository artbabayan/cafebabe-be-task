package com.babayan.babe.cafe.app.controller.rest.v1;

import com.babayan.babe.cafe.app.configuration.security.encryption.CustomPasswordEncoder;
import com.babayan.babe.cafe.app.configuration.security.facade.AuthorizationFacade;
import com.babayan.babe.cafe.app.exceptions.InternalServerErrorException;
import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.UnauthorizedException;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.model.request.AccessTokenModel;
import com.babayan.babe.cafe.app.model.request.LoginRequest;
import com.babayan.babe.cafe.app.model.request.SignUpRequest;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.OAuth2AccessTokenService;
import com.babayan.babe.cafe.app.service.UserService;
import com.babayan.babe.cafe.app.util.ValidationHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

/**
 * @author artbabayan
 */
@Validated
@Log4j2
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AuthenticationController {
    private UserService userService;
    @Autowired public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private OAuth2AccessTokenService oAuth2AccessTokenService;
    @Autowired public void setOauth2AccessTokenService(OAuth2AccessTokenService oAuth2AccessTokenService) {
        this.oAuth2AccessTokenService = oAuth2AccessTokenService;
    }

    private AuthenticationManager authenticationManager;
    @Autowired public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private CustomPasswordEncoder passwordEncoder;
    @Autowired public void setPasswordEncoder(CustomPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private AuthorizationFacade authorizationFacade;
    @Autowired public void setAuthorizationFacade(AuthorizationFacade authorizationFacade) {
        this.authorizationFacade = authorizationFacade;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /** Initializes a new instance of the class */
    public AuthenticationController() {
        log.info("Authentication API initialized");
    }

    //region <CONTROLLER>

    /**
     * Sign-in user with valid username and password into system.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<User> loginUser(@RequestBody @Valid SignUpRequest request) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        request.setEmail(request.getEmail().toLowerCase());
        ValidationHelper.isValidEmailForm(request.getEmail()); //checking a malformed email address

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        user = userService.createUserByEmail(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Sign-in user with valid username and password into system.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<AccessTokenModel> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        obtainAuthenticateUser(loginRequest);
        OAuth2AccessToken oAuth2AccessToken = getAccessToken(loginRequest);

        AccessTokenModel model = new AccessTokenModel();
        model.setAccessToken(oAuth2AccessToken.getValue());
        model.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
        model.setTokenType(oAuth2AccessToken.getTokenType());
        model.setExpiresIn(oAuth2AccessToken.getExpiresIn());
        model.setExpiration(oAuth2AccessToken.getExpiration());

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    //endregion

    // region <HELPER>

    /**
     *
     */
    private void obtainAuthenticateUser(LoginRequest loginRequest) {
        loginRequest.setEmail(loginRequest.getEmail().toLowerCase());
        ValidationHelper.isValidEmailForm(loginRequest.getEmail()); //checking a malformed email address

        User user;
        try {
            user = userService.findByEmail(loginRequest.getEmail());
        } catch (NotFoundException ex) {
            throw new UnauthorizedException(messageService.getMessage("error.wrong.username.or.password"));
        }

        checkUserIsActive(user);
        checkUserPasswordIsMatch(loginRequest, user);
        loginRequest.setPassword(user.getPassword());

        Authentication authentication = authenticateUser(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     *
     */
    private Authentication authenticateUser(LoginRequest loginRequest) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException | EntityNotFoundException ex) {
            log.error(messageService.getMessage("error.unauthorized.bad.credential"));
            throw new UnauthorizedException(messageService.getMessage("error.wrong.username.or.password"));
        }

        return authenticate;
    }

    /**
     *
     */
    private void checkUserIsActive(User user) {
        if (!user.isActive()) {
            log.info(String.format("User id [%s] is not active", user.getId()));
            throw new InternalServerErrorException(messageService.getMessage("error.user.non.active"));
        }
    }

    /**
     *
     */
    private void checkUserPasswordIsMatch(LoginRequest loginRequest, User user) {
        boolean isMatch = passwordEncoder.passwordEncoderUser().matches(loginRequest.getPassword(), user.getPassword());
        if (!isMatch) {
            throw new UnauthorizedException(messageService.getMessage("error.wrong.username.or.password"));
        }
    }

    /**
     *
     */
    private OAuth2AccessToken getAccessToken(LoginRequest loginRequest) {
        OAuth2AccessToken oAuth2AccessToken = oAuth2AccessTokenService
                .obtainOAuth2AccessToken(loginRequest.getEmail().toLowerCase(), loginRequest.getPassword());
        log.info(String.format("User with login [%s] successfully sign-in, access_token [%s], refresh_token [%s]",
                loginRequest.getEmail(), oAuth2AccessToken.getValue(), oAuth2AccessToken.getRefreshToken().getValue()));

        return oAuth2AccessToken;
    }

    //endregion

}
