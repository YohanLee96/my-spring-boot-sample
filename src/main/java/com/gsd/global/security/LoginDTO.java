package com.gsd.global.security;

import com.gsd.domain.user.constant.UserType;
import com.gsd.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Created by Yohan lee
 * Created on 2021/11/27.
 **/

@Getter
@NoArgsConstructor
public class LoginDTO {

    public static LoginDTO.LoginSuccess LoginSuccess(String accessToken, String userName, UserType userType) {
        return new LoginDTO.LoginSuccess(accessToken, userName, userType);
    }

    public static LoginDTO.LoginSuccess LoginSuccess(String accessToken, User user) {
     return new LoginDTO.LoginSuccess(accessToken, user.getUserName(), user.getUserType());
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {


        @NotEmpty(message = "이메일은 필수 값 입니다.")
        private String email;

        @NotEmpty(message = "패스워드는 필수 값 입니다.")
        private String password;
    }


    @Getter
    @NoArgsConstructor
    public static class LoginSuccess {
        private String accessToken;

        private String userName;

        private UserType userType;

        public LoginSuccess(String accessToken, String userName, UserType userType) {
            this.accessToken = accessToken;
            this.userName = userName;
            this.userType = userType;
        }
    }
}
