package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogMessage;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

import java.util.List;
import java.util.Map;

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
    PageInfoResult<BlogMessage> getMessageList
    (Integer current, Integer size, String message, List<String> time,String tag,Integer user_id);

    /**
     * 获取热门的标签
     *
     * @return
     */
    List<Map<String, Object>> getMessageTag();

    /**
     * 发布留言
     *
     * @param blogMessage
     * @return
     */
    Boolean addMessage(BlogMessage blogMessage);

    /**
     * 修改留言
     *
     * @param blogMessage
     * @return
     */
    Boolean updateMessage(BlogMessage blogMessage);

    /**
     * 根据id列表删除留言
     *
     * @param idList
     * @return
     */
    Integer deleteMessage(List<Integer> idList);

    /**
     * 点赞留言
     *
     * @param id
     * @return
     */
    Boolean likeMessage(Integer id);

    /**
     * 取消点赞留言
     *
     * @param id
     * @return
     */
    Boolean cancelLikeMessage(Integer id);
}
