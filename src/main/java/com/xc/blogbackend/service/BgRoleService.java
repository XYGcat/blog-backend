package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.entity.BgRole;
import com.xc.blogbackend.model.domain.entity.BgUserRole;

import java.util.List;

/**
* @author XC
* @description 针对表【bg_role(角色表)】的数据库操作Service
* @createDate 2024-12-20 09:46:56
*/
public interface BgRoleService extends IService<BgRole> {

    /**
     * 根据用户id获取用户角色
     * @param userId 用户id
     * @return 用户角色
     */
    List<BgUserRole> getUserRole(Long userId);
}
