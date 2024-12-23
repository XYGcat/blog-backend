package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.vo.RoleVo;
import com.xc.blogbackend.service.BgRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色接口
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private BgRoleService roleService;

    @ApiOperation(value = "获取用户角色")
    @GetMapping(value = "/getUserRole")
    public BaseResponse<List<RoleVo>> getUserRole(Long userId) {
        List<RoleVo> userRole = roleService.getUserRole(userId);
        return ResultUtils.success(userRole);
    }
}
