package com.babayan.babe.cafe.app.service;

import com.babayan.babe.cafe.app.model.dto.User;

/**
 * @author by artbabayan
 */
public interface UserService {

    User createUserByEmail(User user);

    User findById(Long id);

    User findByEmail(String email);

    boolean existsByEmail(String email);

}
