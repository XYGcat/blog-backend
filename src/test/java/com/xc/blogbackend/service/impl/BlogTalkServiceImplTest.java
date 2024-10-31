package com.xc.blogbackend.service.impl;

import com.xc.blogbackend.mapper.BlogTalkMapper;
import com.xc.blogbackend.mapper.BlogUserMapper;
import com.xc.blogbackend.model.domain.BlogTalk;
import com.xc.blogbackend.model.domain.BlogUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
        LocalDateTime createdAt = blogTalks.get(0).getCreatedAt();
        LocalDateTime updatedAt = blogTalks.get(0).getUpdatedAt();
        LocalDateTime createdAt1 = blogUsers.get(0).getCreatedAt();
        LocalDateTime updatedAt1 = blogUsers.get(0).getUpdatedAt();
        System.out.println(blogTalks);
    }
}