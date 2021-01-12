package com.babayan.babe.cafe.app.service;

/**
 *
 */
public interface MessageService {

    String getMessage(String key);

    String getMessage(String key, Object args);

    String getMessage(String key, Object[] args);

}
