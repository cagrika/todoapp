package com.data.todo.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    int code;
    String exception;
}