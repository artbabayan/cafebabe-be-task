package com.babayan.babe.cafe.app.service;

import com.babayan.babe.cafe.app.model.dto.Table;

/**
 * @author by artbabayan
 */
public interface TableService {

    Table createTable(Table table);

    Table findById(Long id);

    Table findByTableNumber(int number);

    Table findByUserId(Long userId);

    Table findByUserEmail(String email);

    Table assignTableToUser(int tableNumber, Long userId);

    boolean existByTableNumber(int number);

}
