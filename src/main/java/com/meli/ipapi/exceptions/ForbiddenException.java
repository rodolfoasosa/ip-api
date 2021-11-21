package com.meli.ipapi.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Ip banned")
public class ForbiddenException extends Exception {

    public ForbiddenException(String msg) {
        super(msg);
    }
}