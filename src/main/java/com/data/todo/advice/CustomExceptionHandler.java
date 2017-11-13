package com.data.todo.advice;

import com.data.todo.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse unknownException(Exception ex) {
        log.error(ex.getMessage());
        return ErrorResponse.builder()
                .exception(ex.getLocalizedMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

}
