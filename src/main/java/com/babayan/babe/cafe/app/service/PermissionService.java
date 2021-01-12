package com.babayan.babe.cafe.app.service;

import com.babayan.babe.cafe.app.model.dto.Permission;

import java.util.Set;

/**
 * @author by artbabayan
 */
public interface PermissionService {

    Permission createPermission(Permission permission);

    Permission findById(long id);

    Permission findByName(String name);

    Set<Permission> findAll();

    boolean existsByName(String name);

}
