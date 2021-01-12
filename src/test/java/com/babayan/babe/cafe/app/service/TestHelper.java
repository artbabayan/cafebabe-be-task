package com.babayan.babe.cafe.app.service;

import com.babayan.babe.cafe.app.model.dao.PermissionEntity;
import com.babayan.babe.cafe.app.model.dao.RoleEntity;
import com.babayan.babe.cafe.app.model.dao.UserEntity;
import com.babayan.babe.cafe.app.model.dto.Role;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.model.enums.PermissionEnum;
import com.babayan.babe.cafe.app.model.enums.RoleEnum;

public class TestHelper {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // region<RANDOM DATA GENERATION>

    /**
     * Generated user dto with random input parameters
     */
    protected static User generateUser() {
        User user = new User();
        user.setEmail(generateRandomEmail());
        user.setFullName("Jonathan Smith");
        user.setPassword(generateRandomString(8));
        user.setActive(true);

        return user;
    }

    /**
     * Generated user entity with random input parameters
     */
    protected static UserEntity generateUserEntity() {
        UserEntity entity = new UserEntity();
        entity.setEmail(generateRandomEmail());
        entity.setFullName("Jonathan Smith");
        entity.setPassword(generateRandomString(8));
        entity.setActive(true);

        return entity;
    }

    /**
     * Generated user role
     */
    protected static Role generateUserRole() {
        Role role = new Role();
        role.setName(RoleEnum.ROLE_MANAGER.name());

        return role;
    }

    /**
     * Generated user role entity
     */
    protected static RoleEntity generateUserEntityRole() {
        RoleEntity role = new RoleEntity();
        role.setName(RoleEnum.ROLE_MANAGER.name());

        return role;
    }

    /**
     * Generated user role entity
     */
    protected static PermissionEntity generatePermission() {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setName(PermissionEnum.CREATE_USER.name());

        return permissionEntity;
    }

    //endregion

    //region <HELP>

    /**
     * Generate random string
     */
    protected static String generateRandomString(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.toLowerCase().charAt(character));
        }
        return builder.toString();
    }

    /**
     * Generate random email
     */
    protected static String generateRandomEmail() {
        String randomString = generateRandomString(7);
        return String.format(randomString + "%s", "@foo-email.com");
    }

    //endregion

}
