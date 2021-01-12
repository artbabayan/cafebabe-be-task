package com.babayan.babe.cafe.app.util;

import com.babayan.babe.cafe.app.model.dto.Permission;
import com.babayan.babe.cafe.app.model.dto.Product;
import com.babayan.babe.cafe.app.model.dto.Role;
import com.babayan.babe.cafe.app.model.dto.Table;
import com.babayan.babe.cafe.app.model.dto.User;
import com.babayan.babe.cafe.app.model.enums.GenderEnum;
import com.babayan.babe.cafe.app.model.enums.PermissionEnum;
import com.babayan.babe.cafe.app.model.enums.RoleEnum;
import com.babayan.babe.cafe.app.model.enums.TableStatusEnum;
import com.babayan.babe.cafe.app.service.PermissionService;
import com.babayan.babe.cafe.app.service.ProductService;
import com.babayan.babe.cafe.app.service.RoleService;
import com.babayan.babe.cafe.app.service.TableService;
import com.babayan.babe.cafe.app.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author by artbabayan
 */
@Log4j2
@Service
public class UserBotService {
    @Value("${security.bot.email}")
    private String botEmail;

    @Value("${security.bot.password}")
    private String botPassword;

    private PermissionService permissionService;
    @Autowired public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    private UserService userService;
    @Autowired public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private RoleService roleService;
    @Autowired public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private ProductService productService;
    @Autowired public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    private TableService tableService;
    @Autowired public void setTableService(TableService tableService) {
        this.tableService = tableService;
    }

    public void initUser() {
        //creates role permissions
        createPermission();

        //creates roles
        createManagerRole();
        createWaiterRole();

        createManagerUser();
    }

    public void initWaiter() {
        createWaiterUser();
    }

    public void initProduct() {
        createProduct();
    }

    public void initTable() {
        createTable();
    }

    ////////////////////////////////////////////////////////////////////

    //region <USER>

    /**
     *
     */
    private void createPermission() {
        Permission permission = new Permission();

        List<PermissionEnum> permissionEnums = Arrays.asList(PermissionEnum.values());
        permissionEnums.forEach(permissionEnum -> {
            permission.setName(permissionEnum.name());
            permissionService.createPermission(permission);
        });
    }

    /**
     *
     */
    private void createManagerRole() {
        Role role = roleService.createRole(RoleEnum.ROLE_MANAGER);

        Set<Permission> permissions = permissionService.findAll();
        roleService.addPermissionOnRoleByList(role.getId(), permissions);
    }

    /**
     *
     */
    private void createWaiterRole() {
        Role role = roleService.createRole(RoleEnum.ROLE_WAITER);

        roleService.addPermissionOnRoleByName(role.getId(), PermissionEnum.CREATE_ORDER);
        roleService.addPermissionOnRoleByName(role.getId(), PermissionEnum.UPDATE_ORDER);

        roleService.addPermissionOnRoleByName(role.getId(), PermissionEnum.CREATE_PRODUCT_IN_ORDER);
        roleService.addPermissionOnRoleByName(role.getId(), PermissionEnum.UPDATE_PRODUCT_IN_ORDER);
    }

    /**
     * Creates system admin/manager
     */
    private void createManagerUser() {
        User user = new User();
        user.setEmail(botEmail);
        user.setPassword(botPassword);
        user.setGender(GenderEnum.MALE);
        user.setFullName("Bot and Bot");
        user.setRoles(Collections.singleton(roleService.findByName(RoleEnum.ROLE_MANAGER)));

        userService.createUserByEmail(user);
    }

    private void createWaiterUser() {
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setEmail("waiter_" + i + "@foo.com");
            user.setPassword("waiter12345");
            user.setGender(GenderEnum.MALE);
            user.setFullName(StringHelper.generateRandomString(8));

            userService.createUserByEmail(user);
        }
    }

    //endregion

    // region <PRODUCT>

    /**
     *
     */
    private void createProduct() {
        for (int i = 0; i < 30; i++) {
            Product product = new Product();
            product.setName(StringHelper.generateRandomString(8));
            product.setCategory(StringHelper.generateRandomString(8));
            product.setPrice(BigDecimal.valueOf(IntegerHelper.generateRandomNumber(4)));

            productService.createProduct(product);
        }
    }

    //endregion

    // region <PRODUCT>

    /**
     *
     */
    private void createTable() {
        for (int i = 1; i < 11; i++) {
            Table table = new Table();
            table.setTableNumber(i);
            table.setStatus(TableStatusEnum.AVAILABLE);

            tableService.createTable(table);
        }
    }

    //endregion

}
