package com.gsd.global.component.jwt;

import com.gsd.domain.user.constant.UserType;
import io.jsonwebtoken.Claims;
import lombok.Getter;

@Getter
public class ClaimDTO {
    private final String subject;
    private final Long userId;
    private final UserType userType;
    private final Long exp;

    public ClaimDTO(Claims claims) {
        this.userId = Long.valueOf((Integer)claims.get("userId"));
        this.subject = (String)claims.get("subject");
        this.userType = UserType.valueOf(claims.get("role").toString());
        this.exp = Long.valueOf((Integer)claims.get("exp"));
    }
}
