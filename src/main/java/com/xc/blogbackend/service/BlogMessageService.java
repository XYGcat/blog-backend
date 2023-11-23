package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogMessage;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

import java.util.List;

/**
* @author XC
* @description 针对表【blog_message】的数据库操作Service
* @createDate 2023-11-23 16:35:28
*/
public interface BlogMessageService extends IService<BlogMessage> {

    /**
     * 分页获取留言
     *
     * @param current
     * @param size
     * @param message
     * @param time
     * @return
     */
    PageInfoResult<BlogMessage> getMessageList(Integer current, Integer size, String message, List<String> time);

}
