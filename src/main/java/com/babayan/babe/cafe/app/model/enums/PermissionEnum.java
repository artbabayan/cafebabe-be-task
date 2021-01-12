package com.babayan.babe.cafe.app.model.enums;

/**
 * @author by artbabayan
 */
public enum PermissionEnum {
    //permission for manager
    CREATE_USER,
    UPDATE_USER_ALL_DATA,
    DELETE_USER,

    CREATE_TABLE,
    UPDATE_TABLE,
    DELETE_TABLE,

    CREATE_PRODUCT,
    UPDATE_PRODUCT,
    DELETE_PRODUCT,

    //permission for waiter
    CREATE_ORDER,
    UPDATE_ORDER,

    CREATE_PRODUCT_IN_ORDER,
    UPDATE_PRODUCT_IN_ORDER;

}
