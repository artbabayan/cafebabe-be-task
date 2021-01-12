package com.babayan.babe.cafe.app.service.unit;

import com.babayan.babe.cafe.app.configuration.security.encryption.CustomPasswordEncoder;
import com.babayan.babe.cafe.app.model.dao.PermissionEntity;
import com.babayan.babe.cafe.app.model.dao.RoleEntity;
import com.babayan.babe.cafe.app.model.dao.UserEntity;
import com.babayan.babe.cafe.app.model.dto.Role;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.model.enums.RoleEnum;
import com.babayan.babe.cafe.app.service.BaseTest;
import com.babayan.babe.cafe.app.service.RoleService;
import com.babayan.babe.cafe.app.service.TestHelper;
import com.babayan.babe.cafe.app.service.UserService;
import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import com.babayan.babe.cafe.app.exceptions.ValidationException;
import com.babayan.babe.cafe.app.repository.UserRepository;
import com.babayan.babe.cafe.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceUnitTest extends BaseTest {
    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private RoleService roleService;

    @Mock
    private CustomPasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper mapper;

    // Test objects
    private UserEntity userEntityTest;
    private User userTest;
    private Role roleTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        RoleEntity roleEntity = generateUserEntityRole();
        roleEntity.setId(1L);

        PermissionEntity permission = generatePermission();
        permission.setId(1L);

        roleTest = generateUserRole();
        roleTest.setId(1L);
        roleEntity.setName(RoleEnum.ROLE_MANAGER.name());
        roleEntity.setPermissions(Collections.singleton(permission));

        // generate random test user entity
        userEntityTest = TestHelper.generateUserEntity();
        userEntityTest.setId(10L);
//        userEntityTest.setRoles(Collections.singleton(roleEntity));

        // generate random test user
        userTest = TestHelper.generateUser();
        userEntityTest.setId(10L);
        userTest.setRoles(Collections.singleton(roleTest));

    }

    @AfterEach
    void tearDown() { }

    @InjectMocks
    private final UserService userServiceMock = Mockito.spy(new UserServiceImpl());

    @Disabled
    @Test
    void create_success() {
        User user = TestHelper.generateUser();
        String email = user.getEmail();

        when(passwordEncoder.passwordEncoderUser()).thenReturn(new BCryptPasswordEncoder());
        when(roleService.findByName(RoleEnum.ROLE_MANAGER)).thenReturn(roleTest);
        when(userRepositoryMock.existsByEmail(email)).thenReturn(false);
        when(mapper.map(userTest, UserEntity.class)).thenReturn(any(UserEntity.class));
        when(userRepositoryMock.save(userEntityTest)).thenReturn(userEntityTest);

        //testing method
        userServiceMock.createUserByEmail(user);
        verify(userRepositoryMock, times(1)).save(userEntityTest);
    }


    /**
     * @see UserService#findById(Long)
     */
    @Test
    void findById_success() {
        when(userRepositoryMock.findById(userEntityTest.getId())).thenReturn(Optional.of(userEntityTest));
        when(mapper.map(userEntityTest, User.class)).thenReturn(userTest);

        //testing method
        userServiceMock.findById(userEntityTest.getId());
        verify(userRepositoryMock, times(1)).findById(userEntityTest.getId());
    }

    /**
     * @see UserService#findById(Long)
     */
    @Test
    void findById_null_id_fail() {
        //testing method
        Assertions.assertThrows(ValidationException.class, () -> userServiceMock.findById(null));
    }

    /**
     * @see UserService#findById(Long)
     */
    @Test
    void findById_notFound_fail() {

        //testing method
        Assertions.assertThrows(NotFoundException.class, () -> userServiceMock.findById(NON_EXISTING_ID));
    }

    /**
     * @see UserService#findByEmail(String)
     */
    @Test
    void findByEmail_success() {
        when(userRepositoryMock.findByEmail(userEntityTest.getEmail())).thenReturn(userEntityTest);
        when(mapper.map(userEntityTest, User.class)).thenReturn(userTest);

        //testing method
        userServiceMock.findByEmail(userEntityTest.getEmail());
        verify(userRepositoryMock, times(1)).findByEmail(userEntityTest.getEmail());
    }

    /**
     * @see UserService#findByEmail(String)
     */
    @Test
    void findByEmail_null_email_fail() {
        //testing method
        Assertions.assertThrows(ValidationException.class,
                () -> userServiceMock.findByEmail(null));
    }

    /**
     * @see UserService#findByEmail(String)
     */
    @Test
    void findByEmail_non_valid_email_fail() {
        //testing method
        Assertions.assertThrows(ValidationException.class,
                () -> userServiceMock.findByEmail(NON_VALID_EMAIL));
    }

    /**
     * @see UserService#findByEmail(String)
     */
    @Test
    void findByEmail_notFound_fail() {
        when(userRepositoryMock.findByEmail(NON_EXISTING_EMAIL)).thenReturn(null);

        //testing method
        Assertions.assertThrows(NotFoundException.class, () -> userServiceMock.findByEmail(NON_EXISTING_EMAIL));
        verify(userRepositoryMock, times(1)).findByEmail(NON_EXISTING_EMAIL);
    }

    /**
     * @see UserService#existsByEmail(String)
     */
    @Test()
    void exists_success() {
        when(userRepositoryMock.existsByEmail(userTest.getEmail())).thenReturn(true);

        //testing method
        userServiceMock.existsByEmail(userTest.getEmail());
        verify(userRepositoryMock, times(1)).existsByEmail(userTest.getEmail());
    }

    /**
     * @see UserService#existsByEmail(String)
     */
    @Test()
    void exists_null_user_id() {
        //testing method
        boolean b = userServiceMock.existsByEmail(null);

        Assertions.assertFalse(b);
    }

}
