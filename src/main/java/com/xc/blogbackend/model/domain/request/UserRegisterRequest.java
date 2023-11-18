package com.xc.blogbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 星尘
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -6085645882381694391L;

    private String username;
    private String password;
    private String checkPassword;
}
