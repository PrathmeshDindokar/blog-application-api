package com.innobyte.blog_api;

import com.innobyte.blog_api.dto.LoginRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private final ValidatorFactory factory =
            Validation.buildDefaultValidatorFactory();

    private final Validator validator = factory.getValidator();

    @Test
    void validLoginShouldPassValidation() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        assertTrue(validator.validate(request).isEmpty());
        factory.close();
    }

    @Test
    void blankUsernameShouldFailValidation() {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("password123");

        assertFalse(validator.validate(request).isEmpty());
        factory.close();
    }

    @Test
    void blankPasswordShouldFailValidation() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword(" ");

        assertFalse(validator.validate(request).isEmpty());
        factory.close();
    }
}