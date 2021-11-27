package com.gsd.global.security;

import com.gsd.global.error.ErrorCode;
import lombok.Getter;

/**
 * Created by Yohan lee
 * Created on 2021/11/27.
 **/
@Getter
public enum SecurityErrorCode implements ErrorCode {
    ACCESS_DENIED_MESSAGE("접근 권한이 없습니다.", "S-001"),
    UNAUTHORIZED_MESSAGE("로그인이 필요합니다.", "S-002"),
    INVALID_ACCOUNT_INFORMATION_MESSAGE("이메일 또는 패스워드가 잘못되었습니다.", "S-003"),
    UNKNOWN_ERROR_MESSAGE("알 수 없는 에러", "S-006")
    ;


    private final String errorMessage;

    private final String errorCode;

    SecurityErrorCode(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}