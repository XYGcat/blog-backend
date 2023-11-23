package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogHeader;

import java.util.List;

/**
* @author XC
* @description 针对表【blog_header】的数据库操作Service
* @createDate 2023-11-23 16:14:16
*/
public interface BlogHeaderService extends IService<BlogHeader> {

    /**
     * 获取所有背景图
     *
     * @return
     */
    List<BlogHeader> getAllHeader();

}
