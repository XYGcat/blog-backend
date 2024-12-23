package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.mapper.BgMenuMapper;
import com.xc.blogbackend.mapper.BgRoleMenuMapper;
import com.xc.blogbackend.model.domain.entity.BgMenu;
import com.xc.blogbackend.model.domain.entity.BgRoleMenu;
import com.xc.blogbackend.model.domain.vo.MenuVo;
import com.xc.blogbackend.model.domain.vo.RoleVo;
import com.xc.blogbackend.service.BgMenuService;
import com.xc.blogbackend.service.BgRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* @author XC
* @description 针对表【bg_menu(菜单表)】的数据库操作Service实现
* @createDate 2024-12-20 09:47:16
*/
@Service
public class BgMenuServiceImpl extends ServiceImpl<BgMenuMapper, BgMenu>
    implements BgMenuService{

    @Resource
    private BgRoleService bgRoleService;

    @Resource
    private BgRoleMenuMapper roleMenuMapper;

    @Override
    public Boolean addMenu(BgMenu menu) {
        //如果插入的当前节点为根节点，parentId指定为0
        if(menu.getParentId().longValue() == 0){
            menu.setLevel(1);//根节点层级为1
            menu.setPath(null);//根节点路径为空
        }else{
            BgMenu parentMenu = baseMapper.selectById(menu.getParentId());
            if(parentMenu == null){
                throw new BusinessException(ErrorCode.NULL_ERROR,"未查询到对应的父节点");
            }
            menu.setLevel(parentMenu.getLevel().intValue() + 1);
            if(StringUtils.isNotEmpty(parentMenu.getPath())){
                menu.setPath(parentMenu.getPath() + "," + parentMenu.getId());
            }else{
                menu.setPath(parentMenu.getId().toString());
            }
        }
        int insert = baseMapper.insert(menu);

        return insert > 0;
    }

    @Override
    public List<MenuVo> queryMenuTree() {
        LambdaQueryWrapper<BgMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(BgMenu::getLevel,BgMenu::getSort);
        List<BgMenu> allMenu = baseMapper.selectList(wrapper);
        // 0L：表示根节点的父ID
        List<MenuVo> resultList = transferMenuVo(allMenu, 0L);
        return resultList;
    }

    @Override
    public List<MenuVo> roleQueryMenus(Long userId) {
        List<RoleVo> userRoles = new ArrayList<>();

        //1、先查询当前用户对应的角色
        if(userId == null){
            userRoles.add(new RoleVo().setRoleId(4L).setCode("visitor"));
        }else {
            userRoles = bgRoleService.getUserRole(userId);
        }
        if(!CollectionUtils.isEmpty(userRoles)){
            //2、通过角色查询菜单（默认取第一个角色）
            // todo 后续优化
            LambdaQueryWrapper<BgRoleMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BgRoleMenu::getRoleId, userRoles.get(0).getRoleId());
            List<BgRoleMenu> roleMenus = roleMenuMapper.selectList(wrapper);

            if(!CollectionUtils.isEmpty(roleMenus)){
                Set<Long> menuIds = new HashSet<>();
                for (BgRoleMenu roleMenu : roleMenus) {
                    menuIds.add(roleMenu.getMenuId());
                }
                //查询对应的菜单
                LambdaQueryWrapper<BgMenu> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(BgMenu::getId, menuIds);
                List<BgMenu> menus = baseMapper.selectList(queryWrapper);

                if(!CollectionUtils.isEmpty(menus)){
                    //将菜单下对应的父节点也一并全部查询出来
                    Set<Long> allMenuIds = new HashSet<>();
                    for (BgMenu menu : menus) {
                        allMenuIds.add(menu.getId());
                        if(StringUtils.isNotEmpty(menu.getPath())){
                            String[] pathIds = StringUtils.split(",", menu.getPath());
                            for (String pathId : pathIds) {
                                allMenuIds.add(Long.valueOf(pathId));
                            }
                        }
                    }
                    //3、查询对应的所有菜单,并进行封装展示
                    LambdaQueryWrapper<BgMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.in(BgMenu::getId, allMenuIds);
                    lambdaQueryWrapper.orderByAsc(BgMenu::getLevel,BgMenu::getSort);
                    List<BgMenu> allMenus = baseMapper.selectList(lambdaQueryWrapper);
                    List<MenuVo> resultList = transferMenuVo(allMenus, 0L);

                    return resultList;
                }
            }
        }
        return null;
    }

    /**
     * 封装菜单视图
     * @param allMenu
     * @param parentId
     * @return
     */
    private List<MenuVo> transferMenuVo(List<BgMenu> allMenu, Long parentId){
        List<MenuVo> resultList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(allMenu)){
            for (BgMenu source : allMenu) {
                if(parentId.longValue() == source.getParentId().longValue()){
                    MenuVo menuVo = new MenuVo();
                    BeanUtils.copyProperties(source, menuVo);
                    //递归查询子菜单，并封装信息
                    List<MenuVo> childList = transferMenuVo(allMenu, source.getId());
                    if(!CollectionUtils.isEmpty(childList)){
                        menuVo.setChildMenu(childList);
                    }
                    resultList.add(menuVo);
                }
            }
        }
        return resultList;
    }
}




