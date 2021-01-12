package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.configuration.security.encryption.CustomPasswordEncoder;
import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.ResourceAlreadyInUseException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import com.babayan.babe.cafe.app.model.dao.UserEntity;
import com.babayan.babe.cafe.app.model.dto.Role;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.model.enums.RoleEnum;
import com.babayan.babe.cafe.app.repository.UserRepository;
import com.babayan.babe.cafe.app.service.MessageService;
import com.babayan.babe.cafe.app.service.RoleService;
import com.babayan.babe.cafe.app.service.UserService;
import com.babayan.babe.cafe.app.util.ValidationHelper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author by artbabayan
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    @Autowired public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private RoleService roleService;
    @Autowired public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private MessageService messageService;
    @Autowired public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private CustomPasswordEncoder customPasswordEncoder;
    @Autowired public void setUserPasswordEncoder(CustomPasswordEncoder customPasswordEncoder) {
        this.customPasswordEncoder = customPasswordEncoder;
    }

    private ModelMapper mapper;
    @Autowired public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    // region <USER SERVICE>

    @PostConstruct
    private void init() {
        log.info("User service initialized");
    }

    /**
     * @see UserService#createUserByEmail(User)
     */
    @Transactional
    @Override
    public User createUserByEmail(User user) {
        String email = user.getEmail();
        if (user.getId() != null || StringUtils.isEmpty(email) || !ValidationHelper.isValidEmailForm(email)) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        //ensure that email non exist
        checkUserExistsByEmail(email);

        user.setActive(true);
        user.setEmailVerified(false);
        user.setUsername(email);
        user.setFullName(user.getFullName());

        String encodedPassword = customPasswordEncoder.passwordEncoderUser().encode(user.getPassword());
        user.setPassword(encodedPassword);

        String tempPassword = customPasswordEncoder.passwordEncoderUser().encode(encodedPassword);
        user.setTempPassword(tempPassword);

        Role role = roleService.findByName(RoleEnum.ROLE_WAITER); // set default user role 'waiter'
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);

        UserEntity entity = mapper.map(user, UserEntity.class);
        // save/store user entity
        entity = userRepository.saveAndFlush(entity);
        log.info(String.format("User with email [%s] successfully created", user.getEmail()));

        return mapper.map(entity, User.class);
    }

    /**
     * @see UserService#findById(Long)
     */
    @Override
    public User findById(Long id) {
        if (id == null) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        Optional<UserEntity> optional = userRepository.findById(id);
        if (!optional.isPresent()){
            log.error(String.format("User with email [%s] not found.", id));
            throw new NotFoundException(messageService.getMessage("error.user.not.found"));
        }
        log.info(String.format("User with id [%s] successfully fetched", id));
        return mapper.map(optional.get(), User.class);
    }

    /**
     * @see UserService#findByEmail(String)
     */
    @Override
    public User findByEmail(String email) {
        if (StringUtils.isEmpty(email) || !ValidationHelper.isValidEmailForm(email)) {
            throw new ValidationException(messageService.getMessage("validation.unable.to.process.for.null.object"));
        }

        UserEntity entity = userRepository.findByEmail(email);
        if (entity == null) {
            log.error(String.format("User with email [%s] not found.", email));
            throw new NotFoundException(messageService.getMessage("error.user.not.found"));
        }

        log.info(String.format("User with email [%s] successfully fetched", email));
        return mapper.map(entity, User.class);
    }

    /**
     * @see UserService#existsByEmail(String)
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //endregion

    //region <HELPER>

    private void checkUserExistsByEmail(String email) {
        if (existsByEmail(email)) {
            log.error(String.format("User with email [%s] is exist", email));
            throw new ResourceAlreadyInUseException(messageService.getMessage("error.user.exist"));
        }
    }

    //endregion

}
