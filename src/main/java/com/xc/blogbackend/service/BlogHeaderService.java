package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogHeader;
import java.util.List;

/**
* @author XC
* @description 针对表【bg_header】的数据库操作Service
* @createDate 2023-11-23 16:14:16
*/
public interface BlogHeaderService extends IService<BlogHeader> {

    /**
     * 获取所有背景图
     *
     * @return
     */
    List<BlogHeader> getAllHeader();

    /**
     *新增 / 修改 背景
     *
     * @param blogHeader
     * @return
     */
    Boolean addOrUpdateHeader(BlogHeader blogHeader);

    /**
     * 根据路由名获取背景信息
     *
     * @param route_name
     * @return
     */
    BlogHeader getOneByPath(String route_name);

    /**
     * 删除背景
     *
     * @param id
     * @return
     */
    Boolean deleteHeader(Integer id);

}
