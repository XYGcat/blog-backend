package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogLike;

/**
* @author XC
* @description 针对表【bg_like】的数据库操作Service
* @createDate 2023-11-30 18:39:18
*/
public interface BlogLikeService extends IService<BlogLike> {

    /**
     * 获取当前用户对当前文章/说说/留言 是否点赞
     *
     * @param forId
     * @param type
     * @param userId
     * @return
     */
    Boolean getIsLikeByIdAndType(Integer forId,Integer type,Integer userId);

    /**
     * 点赞
     *
     * @param forId
     * @param type
     * @param userId
     * @return
     */
    Boolean addLike(Integer forId,Integer type,Integer userId);

    /**
     * 取消点赞
     *
     * @param forId
     * @param type
     * @param userId
     * @return
     */
    Boolean cancelLike(Integer forId,Integer type,Integer userId);

}
