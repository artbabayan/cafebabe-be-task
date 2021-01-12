package com.babayan.babe.cafe.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * @author by artbabayan
 */
@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationHelper {
    /**
     * Checks user email on correct mail form
     */
    public static boolean isValidEmailForm(String email) {
        EmailValidator validator = EmailValidator.getInstance();

        return validator.isValid(email);
    }

}
