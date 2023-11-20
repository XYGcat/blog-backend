package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogTalk;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

/**
* @author XC
* @description 针对表【blog_talk】的数据库操作Service
* @createDate 2023-11-20 14:28:11
*/
public interface BlogTalkService extends IService<BlogTalk> {

    /**
     * 分页获取说说
     *
     * @param current   当前页
     * @param size      数量
     * @param status    状态
     * @return
     */
    PageInfoResult<BlogTalk> getTalkList(Integer current,Integer size,Integer status);

}
