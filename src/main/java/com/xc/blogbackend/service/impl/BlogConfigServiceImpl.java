package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogConfigMapper;
import com.xc.blogbackend.model.domain.BlogConfig;
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

    @Override
    public BlogConfig getConfig() {
        QueryWrapper<BlogConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit 1");
        List<BlogConfig> blogConfigs = blogConfigMapper.selectList(queryWrapper);
        if (blogConfigs != null && !blogConfigs.isEmpty()) {
            return blogConfigs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String addView() {
        String flag;
        BlogConfig config;
        List<BlogConfig> blogConfigs = blogConfigMapper.selectList(null);
        if (!blogConfigs.isEmpty()) {
            BlogConfig blogConfig = blogConfigs.get(0);
            blogConfig.setView_time(blogConfig.getView_time() + 1);
            boolean byId = this.updateById(blogConfig);
            flag = "添加成功";
        }else {
            flag = "需要初始化";
        }
        return flag;
    }

    @Override
    public Boolean updateConfig(BlogConfig blogConfig) {
        Integer id = blogConfig.getId();
        BlogConfig config = blogConfigMapper.selectById(id);
        if (config != null) {
            int i = blogConfigMapper.updateById(blogConfig);
            return i > 0;
        }else {
            int insert = blogConfigMapper.insert(blogConfig);
            return insert > 0;
        }
    }
}




