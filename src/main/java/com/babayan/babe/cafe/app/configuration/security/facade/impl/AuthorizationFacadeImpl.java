package com.babayan.babe.cafe.app.configuration.security.facade.impl;

import com.babayan.babe.cafe.app.model.dto.Role;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.model.enums.RoleEnum;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.UserService;
import com.babayan.babe.cafe.app.util.Oauth2SecurityHelper;
import com.babayan.babe.cafe.app.configuration.security.facade.AuthorizationFacade;
import com.babayan.babe.cafe.app.exceptions.UnauthorizedException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author artbabayan
 */
@Log4j2
@Component
public class AuthorizationFacadeImpl implements AuthorizationFacade {
    private UserService userService;
    @Autowired public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    //region <SERVICE>

    @Override
    public void authorizeAuth(Long userId) {
        if (getCurrentUserID() == null || !getCurrentUserID().equals(userId)) {
            log.error(String.format("Authenticated user [%s] is not authorized " +
                    "to access requested user [%s] data.", getCurrentUserID(), userId));
            throw new UnauthorizedException(messageService.getMessage("error.unauthorized.access.not.auth.user"));
        }
    }

    @Override
    public Long getCurrentUserID() {
        return obtainUserId();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null)
            return null;

        return userService.findById(getCurrentUserID());
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public void checkAdminAccess() {
        User currentUser = getCurrentUser();
        if (!isAdminAccess(currentUser)) {
            throw new UnauthorizedException(messageService.getMessage("error.unauthorized.access.role"));
        }
    }

    //endregion

    //region <HELPER>

    private boolean isAdminAccess(User user) {
        return getRoleName(user).equals(RoleEnum.ROLE_MANAGER.name());
    }

    public String getRoleName(User user) {
        Set<Role> roles = user.getRoles();
        checkRole(roles, user.getId());

        return user.getRoles().iterator().next().getName();
    }

    private static void checkRole(Set<Role> roles, Long userId) {
        if (CollectionUtils.isEmpty((roles))) {
            throw new ValidationException(String.format("User role is null or empty, user id [%s]", userId));
        }
    }

    private Long obtainUserId() {
        return Oauth2SecurityHelper.retrieveUserId();
    }

    //endregion

}
