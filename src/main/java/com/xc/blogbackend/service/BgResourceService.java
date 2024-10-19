package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BgResource;

import java.util.List;

/**
* @author XC
* @description 针对表【bg_resource】的数据库操作Service
* @createDate 2024-10-19 17:17:59
*/
public interface BgResourceService extends IService<BgResource> {

    /**
     * 根据分类获取站点
     * @param categoryId
     * @return
     */
    List<BgResource> getSiteToCategory(Integer categoryId);

    /**
     * 添加站点
     * @param bgResource
     * @return
     */
    boolean addSiteToCategory(BgResource bgResource);
}
