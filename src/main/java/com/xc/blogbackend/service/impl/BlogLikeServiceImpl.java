package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.model.domain.BlogLike;
import com.xc.blogbackend.mapper.BlogLikeMapper;
import com.xc.blogbackend.service.BlogLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author XC
* @description 针对表【blog_like】的数据库操作Service实现
* @createDate 2023-11-30 18:39:18
*/
@Service
@Slf4j
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike>
    implements BlogLikeService{

    @Resource
    private BlogLikeMapper blogLikeMapper;

    @Override
    public Boolean getIsLikeByIdAndType(Integer for_id, Integer type, Integer user_id) {
        QueryWrapper<BlogLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("for_id",for_id)
                    .eq("type",type)
                    .eq("user_id",user_id);
        Long count = blogLikeMapper.selectCount(queryWrapper);
        return count > 0;
    }
}




