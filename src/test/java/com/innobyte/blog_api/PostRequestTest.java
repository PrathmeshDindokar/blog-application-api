package com.innobyte.blog_api;

import com.innobyte.blog_api.dto.PostRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void validPostRequestShouldPassValidation() {
        PostRequest request = new PostRequest();
        request.setTitle("My first blog post");
        request.setContent("This is valid blog content.");

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void blankTitleShouldFailValidation() {
        PostRequest request = new PostRequest();
        request.setTitle(" ");
        request.setContent("Valid content");

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void blankContentShouldFailValidation() {
        PostRequest request = new PostRequest();
        request.setTitle("Valid title");
        request.setContent("");

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void titleLongerThan200CharactersShouldFail() {
        PostRequest request = new PostRequest();
        request.setTitle("a".repeat(201));
        request.setContent("Valid content");

        assertFalse(validator.validate(request).isEmpty());
    }
}