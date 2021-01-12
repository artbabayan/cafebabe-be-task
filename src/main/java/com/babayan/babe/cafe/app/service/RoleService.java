package com.babayan.babe.cafe.app.service;

import com.babayan.babe.cafe.app.model.dto.Permission;
import com.babayan.babe.cafe.app.model.dto.Role;
import com.babayan.babe.cafe.app.model.enums.PermissionEnum;
import com.babayan.babe.cafe.app.model.enums.RoleEnum;

import java.util.Set;

/**
 * @author by artbabayan
 */
public interface RoleService {

    Role createRole(RoleEnum roleEnum);

    Role findById(long id);

    Role findByName(RoleEnum roleEnum);

    Role addPermissionOnRoleById(long roleId, long permissionId);

    Role addPermissionOnRoleByName(long roleId, PermissionEnum permissionEnum);

    Role addPermissionOnRoleByList(long roleId, Set<Permission> permissions);

}
