package com.xc.blogbackend.model.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class RoleVo implements Serializable {

    private static final long serialVersionUID = -7756185115262635326L;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;
}
