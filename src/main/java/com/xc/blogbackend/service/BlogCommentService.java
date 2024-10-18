package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogComment;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

import java.util.Map;

/**
* @author XC
* @description 针对表【bg_comment】的数据库操作Service
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

    /**
     * 前台分页获取父级评论
     *
     * @param request
     * @return
     */
    PageInfoResult<BlogComment> frontGetParentComment(Map<String,Object> request);

    /**
     * 前台条件分页查找子级评论列表
     *
     * @param request
     * @return
     */
    PageInfoResult<BlogComment> frontGetChildrenComment(Map<String,Object> request);

    /**
     * 后台条件分页查找评论列表
     *
     * @param request
     * @return
     */
    PageInfoResult<BlogComment> backGetCommentList(Map<String,Object> request);

    /**
     * 新增评论
     *
     * @return
     */
    BlogComment createComment(BlogComment blogComment,String ip);

    /**
     * 回复评论
     *
     * @return
     */
    BlogComment applyComment(BlogComment blogComment,String ip);

    /**
     * 点赞评论
     *
     * @param id
     * @return
     */
    Boolean thumbUpComment(Integer id);

    /**
     * 取消点赞评论
     *
     * @param id
     * @return
     */
    Boolean cancelThumbUp(Integer id);

    /**
     * 前台删除评论
     *
     * @param id
     * @param parent_id
     * @return
     */
    Boolean deleteComment(Integer id,Integer parent_id);
}
