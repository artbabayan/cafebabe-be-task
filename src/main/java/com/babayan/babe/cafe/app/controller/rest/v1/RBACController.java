package com.babayan.babe.cafe.app.controller.rest.v1;

import com.babayan.babe.cafe.app.configuration.security.facade.AuthorizationFacade;
import com.babayan.babe.cafe.app.exceptions.NotYetImplementedException;
import com.babayan.babe.cafe.app.model.dto.Permission;
import com.babayan.babe.cafe.app.model.dto.Role;
import com.babayan.babe.cafe.app.model.request.RoleRequest;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.PermissionService;
import com.babayan.babe.cafe.app.service.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author by artbabayan
 */
@Validated
@Log4j2
@RestController
@RequestMapping(value = "/api/v1/rbac", produces = MediaType.APPLICATION_JSON_VALUE)
public class RBACController {

    private RoleService roleService;
    @Autowired public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private PermissionService permissionService;
    @Autowired public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private AuthorizationFacade authorizationFacade;
    @Autowired public void setAuthorizationFacade(AuthorizationFacade authorizationFacade) {
        this.authorizationFacade = authorizationFacade;
    }

    /**
     * Initializes a new instance of the class
     */
    public RBACController() {
        log.info("RBAC API initialized");
    }

    // region <ROLE CONTROLLER>

    /**
     *
     */
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody @Valid RoleRequest roleRequest) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        Role role = roleService.createRole(roleRequest.getRoleEnum());
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    /**
     *
     */
    @GetMapping("/roles")
    public ResponseEntity<Role> gerRoles() {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    /**
     *
     */
    @GetMapping("/roles/{roleId}")
    public ResponseEntity getRoleById(@PathVariable("roleId") Long roleId) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented

        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    /**
     *
     */
    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<?> deleteRoleById(@PathVariable("roleId") Long roleId) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    //endregion

    // region <PERMISSION CONTROLLER>

    /**
     *
     */
    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@RequestBody @Valid Permission permission) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    /**
     *
     */
    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getPermissions() {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    /**
     *
     */
    @GetMapping("/permissions/{permission-name}")
    public ResponseEntity<Permission> getPermissionByName(@PathVariable("name") String permissionName) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    /**
     *
     */
    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@RequestBody @Valid Permission permission) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    /**
     *
     */
    @DeleteMapping("/permissions/{permission-name}")
    public ResponseEntity<?> deletePermissionByKey(@PathVariable("name") String permissionName) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    //endregion

    // region <ROLE - PERMISSION CONTROLLER>

    /**
     *
     */
    @PostMapping("/roles/{roleId}/permissions/{permission-name}")
    public ResponseEntity<Role> addPermissionOnRole(@PathVariable("roleId") Long roleId, @PathVariable("name") String permissionName) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    /**
     *
     */
    @DeleteMapping("/roles/{roleId}/permissions/{permission}")
    public ResponseEntity<Role> removePermissionOnRole(@PathVariable("roleId") Long roleId, @PathVariable("name") String permissionName) {
        //check ADMIN access
        authorizationFacade.checkAdminAccess();

        //ToDo: should be implemented
        throw new NotYetImplementedException(messageService.getMessage("method.not.implemented"));
    }

    //endregion

}
