package com.innobyte.blog_api;

import com.innobyte.blog_api.dto.CommentRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentRequestTest {

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
    void validCommentRequestShouldPassValidation() {
        CommentRequest request = new CommentRequest();
        request.setContent("This is a valid comment.");
        request.setPostId(1L);

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void blankContentShouldFailValidation() {
        CommentRequest request = new CommentRequest();
        request.setContent(" ");
        request.setPostId(1L);

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void nullPostIdShouldFailValidation() {
        CommentRequest request = new CommentRequest();
        request.setContent("Valid comment");
        request.setPostId(null);

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void contentLongerThan5000CharactersShouldFail() {
        CommentRequest request = new CommentRequest();
        request.setContent("a".repeat(5001));
        request.setPostId(1L);

        assertFalse(validator.validate(request).isEmpty());
    }
}
