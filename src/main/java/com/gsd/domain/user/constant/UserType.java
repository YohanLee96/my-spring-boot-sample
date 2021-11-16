package com.gsd.domain.user.constant;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

@Getter
public enum UserType implements GrantedAuthority {
    SUPER_ADMIN(1),
    HEAD_TEACHER(2),
    TEACHER(3),
    STUDENT(4);


    private final int userTypeNo;

    UserType(int userTypeNo) {
        this.userTypeNo = userTypeNo;
    }

    public static Integer convertToInteger(UserType paramUserType) {
        return Arrays.stream(UserType.values())
                .filter(userType -> userType.getUserTypeNo() == paramUserType.getUserTypeNo())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getUserTypeNo();
    }

    public static UserType convertToUserType(Integer userTypeNo) {
        return Arrays.stream(UserType.values())
                .filter(userType -> userType.getUserTypeNo() == userTypeNo)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String getAuthority() {
        return this.name();
    }

    public boolean isStudent() {
        return UserType.STUDENT.equals(this);
    }

    public boolean isTeacher() {
        return Arrays.asList(HEAD_TEACHER, TEACHER).contains(this);
    }

    public boolean isSuperAdmin() {
        return UserType.SUPER_ADMIN.equals(this);
    }

    public boolean isAdminAuthority() {
        return Arrays.asList(SUPER_ADMIN, HEAD_TEACHER, TEACHER).contains(this);
    }

    public static UserType[] getTeacherTypes() {
        return new UserType[]{UserType.HEAD_TEACHER, UserType.TEACHER};
    }

    public static String[] getAllTypes() {
        return new String[]{
                UserType.SUPER_ADMIN.name(),
                UserType.HEAD_TEACHER.name(),
                UserType.TEACHER.name(),
                UserType.STUDENT.name()
        };

    }


}
