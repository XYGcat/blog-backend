package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.entity.BgMenu;
import com.xc.blogbackend.model.domain.vo.MenuVo;

import java.util.List;

/**
* @author XC
* @description 针对表【bg_menu(菜单表)】的数据库操作Service
* @createDate 2024-12-20 09:47:16
*/
public interface BgMenuService extends IService<BgMenu> {

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    Boolean addMenu(BgMenu menu);

    /**
     * 获取菜单树
     * @return
     */
    List<MenuVo> queryMenuTree();

    /**
     * 获取角色对应的菜单
     * @return
     */
    List<MenuVo> roleQueryMenus(Long userId);
}
