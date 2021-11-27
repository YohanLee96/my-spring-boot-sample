package com.gsd.domain.user.constant;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

@Getter
public enum UserType implements GrantedAuthority {
    SUPER_ADMIN,
    ADMIN,
    MEMBER
    ;

    @Override
    public String getAuthority() {
        return this.name();
    }

    public boolean isAdminAuthority() {
        return Arrays.asList(SUPER_ADMIN, ADMIN).contains(this);
    }


}
