package com.gsd.domain.user.model;

import com.gsd.domain.user.constant.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by Yohan lee
 * Created on 2021/11/27.
 **/

@Getter
@NoArgsConstructor
@DynamicUpdate
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;

    @Column(name = "user_email", unique = true, nullable = false)
    private String userEmail;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

}
