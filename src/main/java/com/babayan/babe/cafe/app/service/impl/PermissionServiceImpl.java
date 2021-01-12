package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.PermissionService;
import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.ResourceAlreadyInUseException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import com.babayan.babe.cafe.app.model.dao.PermissionEntity;
import com.babayan.babe.cafe.app.model.dto.Permission;
import com.babayan.babe.cafe.app.repository.PermissionRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author by artbabayan
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {
    private PermissionRepository permissionRepository;
    @Autowired public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private ModelMapper mapper;
    @Autowired public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    // region <PERMISSION SERVICE>

    @PostConstruct
    private void init() {
        log.info("Permission service initialized.");
    }

    /**
     * @see PermissionService#createPermission(Permission)
     */
    @Transactional
    public Permission createPermission(Permission permission) {
        String permissionName = permission.getName();
        if (StringUtils.isEmpty(permissionName)) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        if (existsByName(permissionName)) {
            log.info(String.format("The permission [%s] already exists.", permissionName));
            throw new ResourceAlreadyInUseException(messageService.getMessage("validation.existing.permission"));
        }

        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setName(permission.getName());

        permissionEntity = permissionRepository.saveAndFlush(permissionEntity);
        log.info(String.format("Permission [%s] successfully created.", permission.getName()));

        return mapper.map(permissionEntity, Permission.class);
    }

    /**
     * @see PermissionService#findById(long)
     */
    @Override
    public Permission findById(long id) {
        PermissionEntity entity = permissionRepository.findById(id);
        return mapper.map(entity, Permission.class);
    }

    /**
     * @see PermissionService#findByName(String)
     */
    @Override
    public Permission findByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        PermissionEntity entity = permissionRepository.findByName(name);
        if (entity == null) {
            throw new NotFoundException(String.format("Permission with name [%s] not found", name));
        }

        return mapper.map(entity, Permission.class);
    }

    /**
     * @see PermissionService#findAll()
     */
    @Override
    public Set<Permission> findAll() {
        Set<Permission> permissions = new HashSet<>();

        List<PermissionEntity> entities = permissionRepository.findAll();
        for (PermissionEntity entity : entities) {
            Permission permission = mapper.map(entity, Permission.class);
            permissions.add(permission);
        }

        return permissions;
    }

    /**
     * @see PermissionService#existsByName(String)
     */
    @Override
    public boolean existsByName(String name) {
        return permissionRepository.existsByName(name);
    }

    //endregion

    //region <HELPER>

    private void checkByPermissionName(String name) {
        Permission permission = findByName(name);
        if (permission.getId() > 0) {
            log.info(String.format("The permission [%s] already exists.", name));
            throw new ResourceAlreadyInUseException(messageService.getMessage("validation.existing.permission"));
        }
    }

    //endregion

}
