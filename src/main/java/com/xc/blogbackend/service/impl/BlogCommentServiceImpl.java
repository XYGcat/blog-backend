package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.model.domain.BlogComment;
import com.xc.blogbackend.mapper.BlogCommentMapper;
import com.xc.blogbackend.service.BlogCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author XC
* @description 针对表【blog_comment】的数据库操作Service实现
* @createDate 2023-11-23 18:17:54
*/
@Service
@Slf4j
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment>
    implements BlogCommentService{

    @Resource
    private BlogCommentMapper blogCommentMapper;

    @Override
    public Long getCommentTotal(Integer for_id, Integer type) {
        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("for_id", for_id).eq("type", type);
        Long count = blogCommentMapper.selectCount(queryWrapper);
        return count;
    }
}




