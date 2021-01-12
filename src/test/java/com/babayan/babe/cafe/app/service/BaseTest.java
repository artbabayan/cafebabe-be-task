package com.babayan.babe.cafe.app.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Configuration class for unit and integration tests
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseTest extends TestHelper {
    protected static final Long NON_EXISTING_ID = 9999999999L;
    protected static final String NON_EXISTING_EMAIL = "non_existing_email@gmail.com";
    protected static final String NON_VALID_EMAIL = "non_existing_email.gmail.com";

}
