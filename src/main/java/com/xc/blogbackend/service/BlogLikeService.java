package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogLike;

/**
* @author XC
* @description 针对表【blog_like】的数据库操作Service
* @createDate 2023-11-30 18:39:18
*/
public interface BlogLikeService extends IService<BlogLike> {

    /**
     * 获取当前用户对当前文章/说说/留言 是否点赞
     *
     * @param for_id
     * @param type
     * @param user_id
     * @return
     */
    Boolean getIsLikeByIdAndType(Integer for_id,Integer type,Integer user_id);

}
