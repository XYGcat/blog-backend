package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.model.domain.BlogConfig;
import com.xc.blogbackend.mapper.BlogConfigMapper;
import com.xc.blogbackend.service.BlogConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_config】的数据库操作Service实现
* @createDate 2023-11-23 14:04:12
*/
@Service
@Slf4j
public class BlogConfigServiceImpl extends ServiceImpl<BlogConfigMapper, BlogConfig>
    implements BlogConfigService{

    @Resource
    private BlogConfigMapper blogConfigMapper;

    private final QueryWrapper<BlogConfig> queryWrapper = new QueryWrapper<>();

    @Override
    public BlogConfig getConfig() {
        queryWrapper.clear();
        queryWrapper.last("limit 1");
        List<BlogConfig> blogConfigs = blogConfigMapper.selectList(queryWrapper);
        if (blogConfigs != null && !blogConfigs.isEmpty()) {
            return blogConfigs.get(0);
        } else {
            return null;
        }
    }
}




