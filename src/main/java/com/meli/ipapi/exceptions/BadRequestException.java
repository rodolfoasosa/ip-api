package com.meli.ipapi.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Bad Request")
public class BadRequestException extends Exception {

    public BadRequestException(String msg) {
        super(msg);
    }
}