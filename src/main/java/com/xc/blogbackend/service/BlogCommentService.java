package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogComment;

/**
* @author XC
* @description 针对表【blog_comment】的数据库操作Service
* @createDate 2023-11-23 18:17:54
*/
public interface BlogCommentService extends IService<BlogComment> {

    /**
     * 根据 评论类型 和 类型对应的id获取评论总数
     *
     * @param for_id
     * @param type
     * @return
     */
    Long getCommentTotal(Integer for_id,Integer type);

}
