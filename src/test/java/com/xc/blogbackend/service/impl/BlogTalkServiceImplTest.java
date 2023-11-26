package com.xc.blogbackend.service.impl;

import com.xc.blogbackend.mapper.BlogTalkMapper;
import com.xc.blogbackend.mapper.BlogUserMapper;
import com.xc.blogbackend.model.domain.BlogTalk;
import com.xc.blogbackend.model.domain.BlogUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BlogTalkServiceImplTest {

    @Resource
    private BlogTalkMapper blogTalkMapper;

    @Resource
    private BlogUserMapper blogUserMapper;

    @Test
    void getTalkList() {
        assertNotNull(blogTalkMapper); // 确保对象已经注入
        assertNotNull(blogUserMapper);
        List<BlogTalk> blogTalks = blogTalkMapper.selectList(null);
        List<BlogUser> blogUsers = blogUserMapper.selectList(null);
        Date createdAt = blogTalks.get(0).getCreatedAt();
        Date updatedAt = blogTalks.get(0).getUpdatedAt();
        Date createdAt1 = blogUsers.get(0).getCreatedAt();
        Date updatedAt1 = blogUsers.get(0).getUpdatedAt();
        System.out.println(blogTalks);
    }
}