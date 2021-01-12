package com.babayan.babe.cafe.app.controller.rest.v1;

import com.babayan.babe.cafe.app.configuration.security.facade.AuthorizationFacade;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author artbabayan
 */
@Validated
@Log4j2
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private UserService userService;
    @Autowired public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private AuthorizationFacade authorizationFacade;
    @Autowired public void setAuthorizationFacade(AuthorizationFacade authorizationFacade) {
        this.authorizationFacade = authorizationFacade;
    }

    /**
     * Initializes a new instance of the class
     */
    public UserController() {
        log.info("Users API initialized");
    }

    // region <USER CONTROLLER>

    /**
     * Get user by specified ID
     */
    @GetMapping(value = "/id/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") long id) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Get user by specified email
     */
    @GetMapping(value = "/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        Long currentUserID = authorizationFacade.getCurrentUserID();
        authorizationFacade.authorizeAuth(currentUserID);

        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        User user = userService.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //endregion

}
