package com.sparta.newsfeedteamproject.dto.comment;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CommentReqDtoTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void should_ThrowException_when_ContentsBlank(){
        //given
        String contents = "";
        CommentReqDto mockCommentReqDto = Mockito.mock(CommentReqDto.class);
        when(mockCommentReqDto.getContents()).thenReturn(contents);

        //when - then
        Exception exception = assertThrows(ConstraintViolationException.class, ()->{
            validator.validate(mockCommentReqDto).forEach(violation -> {
                throw new ConstraintViolationException("Validation failed", null);
            });
        });
    }

}