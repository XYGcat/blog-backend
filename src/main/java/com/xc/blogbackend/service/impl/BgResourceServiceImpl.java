package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BgResourceMapper;
import com.xc.blogbackend.model.domain.BgResource;
import com.xc.blogbackend.service.BgResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【bg_resource】的数据库操作Service实现
* @createDate 2024-10-19 17:17:59
*/
@Service
public class BgResourceServiceImpl extends ServiceImpl<BgResourceMapper, BgResource>
    implements BgResourceService{

    @Resource
    private BgResourceMapper bgResourceMapper;

    @Override
    public List<BgResource> getSiteToCategory(Integer categoryId) {
        QueryWrapper<BgResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",categoryId);
        List<BgResource> bgResources = bgResourceMapper.selectList(queryWrapper);
        return bgResources;
    }

    @Override
    public boolean addSiteToCategory(BgResource bgResource) {
        int insert = bgResourceMapper.insert(bgResource);
        return insert > 0;
    }
}




