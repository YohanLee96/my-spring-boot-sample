package com.gsd.global.component.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gsd.global.error.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Yohan lee
 * Created on 2021/11/27.
 **/

@Getter
public class ApiResult {

    @JsonIgnore
    private final static String SUCCESS_CODE = "0000";
    @JsonIgnore
    private final static String ERROR_CODE = "9999";

    private final boolean success;

    private final String resultCode;

    private final Object data;

    private final Error error;

    public static ApiResult success() {
        return new ApiResult(true, SUCCESS_CODE, null, null);
    }

    public static <T> ApiResult success(T data) {
        return new ApiResult(true, SUCCESS_CODE, data, null);
    }

    public static ApiResult error(Throwable throwable) {
        return new ApiResult(false, ERROR_CODE, null, Error.of(throwable));
    }

    public static ApiResult error(ErrorCode errorCode) {
        return new ApiResult(false, ERROR_CODE, null, Error.of(errorCode));
    }


    public ApiResult(boolean success, String resultCode, Object data, Error error) {
        this.success = success;
        this.resultCode = resultCode;
        this.data = data;
        this.error = error;
    }


    @Getter
    @NoArgsConstructor
    private static class Error {

        private String errorCode;

        private String message;

        public static Error of(Throwable throwable) {
            return new Error(throwable.getMessage(), "9999");
        }

        public static Error of(ErrorCode errorCode) {
            return new Error(errorCode.getErrorMessage(), errorCode.getErrorCode());
        }


        private Error(String message, String errorCode) {
            this.message = message;
            this.errorCode = errorCode;
        }
    }

}
