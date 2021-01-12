package com.babayan.babe.cafe.app.configuration.security.facade;

import com.babayan.babe.cafe.app.model.dto.User;
import org.springframework.security.core.Authentication;

public interface AuthorizationFacade {

    void authorizeAuth(Long userId);

    Long getCurrentUserID();

    User getCurrentUser();

    Authentication getAuthentication();

    void checkAdminAccess();

}
