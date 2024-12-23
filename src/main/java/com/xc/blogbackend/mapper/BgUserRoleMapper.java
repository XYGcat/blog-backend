package com.xc.blogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.blogbackend.model.domain.entity.BgUserRole;
import com.xc.blogbackend.model.domain.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author XC
* @description 针对表【bg_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-12-20 09:47:08
* @Entity com.xc.blogbackend.model.domain.entity.BgUserRole
*/
public interface BgUserRoleMapper extends BaseMapper<BgUserRole> {

    /**
     * 根据用户id获取用户角色
     *
     * @param userId
     * @return
     */
    List<RoleVo> getUserRole(@Param("userId") Long userId);
}




