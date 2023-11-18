package com.xc.blogbackend.service;
import java.util.Date;

import com.xc.blogbackend.model.domain.BlogUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
public class BlogUserServiceTest {
    @Resource
    private BlogUserService blogUserService;

    @Test
    public void testAddUser(){
        BlogUser blogUser = new BlogUser();
        blogUser.setUsername("123456789");
        blogUser.setPassword("123456789.xc");
        blogUser.setRole(0);
        blogUser.setNick_name("xc");
//        blogUser.setAvatar("");
        blogUser.setQq("1111");
        blogUser.setIp("1111");
        boolean result = blogUserService.save(blogUser);
        System.out.println(blogUser.getId());
        Assertions.assertTrue(result);
    }

}