package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.entity.BgMenu;
import com.xc.blogbackend.model.domain.vo.MenuVo;
import com.xc.blogbackend.service.BgMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单接口
 */
@Api(tags = "菜单接口")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private BgMenuService bgMenuService;

    @ApiOperation(value = "添加菜单")
    @PostMapping("/addMenu")
    public BaseResponse<Boolean> addMenu(BgMenu menu) {
        Boolean b = bgMenuService.addMenu(menu);
        return ResultUtils.success(b);
    }

    @ApiOperation(value = "获取菜单树")
    @GetMapping(value = "/queryMenuTree")
    public BaseResponse<List<MenuVo>> queryTreeMenu(){
        List<MenuVo> menuVos = bgMenuService.queryMenuTree();
        return ResultUtils.success(menuVos);
    }

    @ApiOperation(value = "获取角色菜单")
    @GetMapping(value = "/roleQueryMenus")
    public BaseResponse<List<MenuVo>> roleQueryMenus(@RequestParam(required = false) Long userId){
        List<MenuVo> menuVos = bgMenuService.roleQueryMenus(userId);
        return ResultUtils.success(menuVos);
    }
}
