package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.model.domain.BlogTag;
import com.xc.blogbackend.mapper.BlogTagMapper;
import com.xc.blogbackend.service.BlogTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_tag】的数据库操作Service实现
* @createDate 2023-11-16 11:55:15
*/
@Service
@Slf4j
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag>
    implements BlogTagService{

    @Resource
    private BlogTagMapper blogTagMapper;

    @Override
    public List<BlogTag> getTagDictionary() {
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","tag_name", "createdAt", "updatedAt");
        List<BlogTag> blogTags = blogTagMapper.selectList(queryWrapper);
        return blogTags;
    }
}




