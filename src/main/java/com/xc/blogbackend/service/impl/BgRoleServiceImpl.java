package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BgRoleMapper;
import com.xc.blogbackend.mapper.BgUserRoleMapper;
import com.xc.blogbackend.model.domain.entity.BgRole;
import com.xc.blogbackend.model.domain.entity.BgUserRole;
import com.xc.blogbackend.service.BgRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【bg_role(角色表)】的数据库操作Service实现
* @createDate 2024-12-20 09:46:56
*/
@Service
public class BgRoleServiceImpl extends ServiceImpl<BgRoleMapper, BgRole>
    implements BgRoleService{

    @Resource
    private BgRoleMapper bgRoleMapper;

    @Resource
    private BgUserRoleMapper bgUserRoleMapper;

    @Override
    public List<BgUserRole> getUserRole(Long userId) {
        LambdaQueryWrapper<BgUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BgUserRole::getUserId,userId);
        List<BgUserRole> userRoles = bgUserRoleMapper.selectList(wrapper);
        return userRoles;
    }
}




