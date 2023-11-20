package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.model.domain.BlogTalk;
import com.xc.blogbackend.mapper.BlogTalkMapper;
import com.xc.blogbackend.service.BlogTalkService;
import org.springframework.stereotype.Service;

/**
* @author XC
* @description 针对表【blog_talk】的数据库操作Service实现
* @createDate 2023-11-20 14:28:11
*/
@Service
public class BlogTalkServiceImpl extends ServiceImpl<BlogTalkMapper, BlogTalk>
    implements BlogTalkService{

}




