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

    /**
     * 新增说说
     *
     * @param blogTalk
     * @return
     */
    BlogTalk publishTalk(BlogTalk blogTalk);

    /**
     *根据id获取说说详情
     *
     * @param id
     * @return
     */
    BlogTalk getTalkById(Integer id);

    /**
     * 修改说说
     *
     * @param blogTalk
     * @return
     */
    Boolean updateTalk(BlogTalk blogTalk);

    /**
     * 置顶/取消置顶 说说
     *
     * @param id
     * @param is_top
     * @return
     */
    Boolean toggleTop(Integer id,Integer is_top);

    /**
     * 切换说说公开性
     *
     * @param id
     * @param status
     * @return
     */
    Boolean togglePublic(Integer id,Integer status);

    /**
     * 删除说说
     *
     * @param id
     * @param status
     * @return
     */
    Boolean deleteTalkById(Integer id,Integer status);

    /**
     * 恢复说说
     *
     * @param id
     * @return
     */
    Boolean revertTalk(Integer id);

    /**
     * 前台获取说说列表
     *
     * @param current
     * @param size
     * @param user_id
     * @return
     */
    PageInfoResult<BlogTalk> blogGetTalkList(Integer current,Integer size,Integer user_id);

    /**
     * 取消点赞
     *
     * @param id
     * @return
     */
    Boolean talkLike(Integer id);

    /**
     * 取消点赞
     *
     * @param id
     * @return
     */
    Boolean cancelTalkLike(Integer id);
}
