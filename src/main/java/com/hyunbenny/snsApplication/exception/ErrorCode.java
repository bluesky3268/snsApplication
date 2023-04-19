package com.hyunbenny.snsApplication.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "DUPLICATE USERNAME"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "INVALID PASSWORD"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID TOKEN"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST NOT FOUND"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "INVALID PERMISSION"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR"),
    ;


    private HttpStatus status;
    private String message;

}
