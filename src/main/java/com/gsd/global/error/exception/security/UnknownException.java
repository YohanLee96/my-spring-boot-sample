package com.gsd.global.error.exception.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by Yohan lee
 * Created on 2021/11/27.
 **/
public class UnknownException extends AuthenticationException {
    public UnknownException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnknownException(String msg) {
        super(msg);
    }
}
