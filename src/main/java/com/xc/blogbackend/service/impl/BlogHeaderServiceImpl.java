package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.model.domain.BlogHeader;
import com.xc.blogbackend.mapper.BlogHeaderMapper;
import com.xc.blogbackend.service.BlogHeaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_header】的数据库操作Service实现
* @createDate 2023-11-23 16:14:16
*/
@Service
@Slf4j
public class BlogHeaderServiceImpl extends ServiceImpl<BlogHeaderMapper, BlogHeader>
    implements BlogHeaderService{

    @Resource
    private BlogHeaderMapper blogHeaderMapper;

    private final QueryWrapper<BlogHeader> queryWrapper = new QueryWrapper<>();

    @Override
    public List<BlogHeader> getAllHeader() {
        queryWrapper.clear();
        queryWrapper.select("id", "route_name", "bg_url");
        List<BlogHeader> blogHeaders = blogHeaderMapper.selectList(queryWrapper);

        return blogHeaders;
    }
}




