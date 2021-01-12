package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.exceptions.ResourceAlreadyInUseException;
import com.babayan.babe.cafe.app.model.dao.RoleEntity;
import com.babayan.babe.cafe.app.model.dto.Permission;
import com.babayan.babe.cafe.app.model.dto.Role;
import com.babayan.babe.cafe.app.model.enums.PermissionEnum;
import com.babayan.babe.cafe.app.model.enums.RoleEnum;
import com.babayan.babe.cafe.app.repository.RoleRepository;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.PermissionService;
import com.babayan.babe.cafe.app.service.RoleService;
import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;

/**
 * @author by artbabayan
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;
    @Autowired public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private PermissionService permissionService;
    @Autowired public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private ModelMapper mapper;
    @Autowired public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    // region <ROLE SERVICE>

    @PostConstruct
    private void init() {
        log.info("Role service initialized");
    }

    /**
     * Creates a new user role with specific permissions.
     */
    @Transactional
    @Override
    public Role createRole(RoleEnum roleEnum) {
        checkByRoleName(roleEnum);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleEnum.name());

        roleEntity = roleRepository.saveAndFlush(roleEntity);
        log.info(String.format("Role [%s] successfully created.", roleEntity.getName()));

        return mapper.map(roleEntity, Role.class);
    }

    /**
     * @see RoleService#findById(long)
     */
    @Override
    public Role findById(long id) {
        Optional<RoleEntity> optional = roleRepository.findById(id);
        if (optional.isEmpty()) {
            log.info(String.format("Role with id [%s] not found", id));
            throw new NotFoundException(messageService.getMessage("error.role.not.found"));
        }

        return mapper.map(optional.get(), Role.class);
    }

    /**
     * @see RoleService#findByName(RoleEnum)
     */
    @Override
    public Role findByName(RoleEnum roleEnum) {
        if (StringUtils.isEmpty(roleEnum.name())) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        RoleEntity roleEntity = roleRepository.findByName(roleEnum.name());
        if (roleEntity == null) {
            log.info(String.format("Role with name [%s] not found.", roleEnum.name()));
            throw new NotFoundException(messageService.getMessage("error.role.not.found"));
        }

        return mapper.map(roleEntity, Role.class);
    }

    /**
     * @see RoleService#addPermissionOnRoleById(long, long)
     */
    @Transactional
    @Override
    public Role addPermissionOnRoleById(long roleId, long permissionId) {
        Role role = findById(roleId);

        Permission permission = permissionService.findById(permissionId);
        if (role.getPermissions().contains(permission)) {
            log.info(String.format("Role with id [%s] already has this permission wit name [%s]",
                    role.getId(), permission.getName()));
            throw new ValidationException("Existing permission");

        }

        role.getPermissions().add(permission);

        RoleEntity roleEntity = mapper.map(role, RoleEntity.class);
        roleEntity = roleRepository.saveAndFlush(roleEntity);
        log.info(String.format("Added new permission for role with id [%s] successfully.", roleEntity.getId()));

        return mapper.map(roleEntity, Role.class);
    }

    @Transactional
    @Override
    public Role addPermissionOnRoleByName(long roleId, PermissionEnum permissionEnum) {
        Role role = findById(roleId);

        Permission permission = permissionService.findByName(permissionEnum.name());
        if (role.getPermissions().contains(permission)) {
            log.info(String.format("Role with id [%s] already has this permission wit name [%s]",
                    role.getId(), permission.getName()));
            throw new ValidationException(messageService.getMessage("validation.existing.permission"));
        }

        role.getPermissions().add(permission);

        RoleEntity roleEntity = mapper.map(role, RoleEntity.class);
        roleEntity = roleRepository.saveAndFlush(roleEntity);
        log.info(String.format("Added new permission for role with id [%s] successfully.", roleEntity.getId()));

        return mapper.map(roleEntity, Role.class);
    }

    @Transactional
    @Override
    public Role addPermissionOnRoleByList(long roleId, Set<Permission> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        Role role = findById(roleId);
        for (Permission permission : permissions) {
            role.getPermissions().add(permission);
        }

        RoleEntity roleEntity = mapper.map(role, RoleEntity.class);
        roleEntity = roleRepository.saveAndFlush(roleEntity);
        log.info(String.format("Added new permission for role with id [%s] successfully.", roleEntity.getId()));

        return mapper.map(roleEntity, Role.class);
    }

    //endregion

    // region <HELPER>

    private void checkByRoleName(RoleEnum roleEnum) {
        Role role;
        try {
            role = findByName(roleEnum);
        } catch (NotFoundException ex) {
            return;
        }

        if (role.getId() > 0) {
            log.info(String.format("The user role [%s] already exists.", roleEnum.name()));
            throw new ResourceAlreadyInUseException(messageService.getMessage("error.role.exist"));
        }
    }

    //endregion

}
