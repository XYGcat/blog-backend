package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogNotify;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

/**
* @author XC
* @description 针对表【bg_notify】的数据库操作Service
* @createDate 2023-12-01 17:26:20
*/
public interface BlogNotifyService extends IService<BlogNotify> {

    /**
     *获取当前用户的消息推送
     *
     * @param current
     * @param size
     * @param user_id
     * @return
     */
    PageInfoResult<BlogNotify> getNotifyList(Integer current, Integer size, Integer user_id);

    /**
     * 新增消息通知
     *
     * @param user_id
     * @param type
     * @param to_id
     * @param message
     * @return
     */
    BlogNotify addNotify(Integer user_id,Integer type,Integer to_id,String message);

    /**
     * 已阅消息通知
     *
     * @param id
     * @return
     */
    Boolean updateNotify(Integer id);

    /**
     * 删除消息通知
     *
     * @param id
     * @return
     */
    Boolean deleteNotifys(Integer id);
}
