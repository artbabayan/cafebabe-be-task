package com.babayan.babe.cafe.app.service.impl;

import com.babayan.babe.cafe.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @author by artbabayan
 */
@Service
public class MessageServiceImpl implements MessageService {
    private MessageSource messageSource;
    @Autowired public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String key) {
        return getMessage(key, null);
    }

    @Override
    public String getMessage(String key, Object args) {
        return getMessage(key, args == null ? null : new Object[]{args});
    }

    @Override
    public String getMessage(String key, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(key, args, locale);
    }

}
