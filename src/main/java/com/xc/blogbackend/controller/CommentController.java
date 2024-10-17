package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogComment;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogCommentService;
import com.xc.blogbackend.service.BlogNotifyService;
import com.xc.blogbackend.utils.CurrentTypeName;
import com.xc.blogbackend.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 评论接口
 *
 * @author 星尘
 */
@Api(tags = "评论接口")
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private BlogCommentService blogCommentService;

    @Resource
    private BlogNotifyService blogNotifyService;

    /**
     * 获取关于当前的评论总数 子评论 + 父评论
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "获取评论总条数")
    @PostMapping("/getCommentTotal")
    public BaseResponse<Long> getCommentTotal(@RequestBody Map<String,Integer> request){
        Integer for_id = request.get("for_id");
        Integer type = request.get("type");
        Long commentTotal = blogCommentService.getCommentTotal(for_id, type);
        return ResultUtils.success(commentTotal,"获取评论总条数成功");
    }

    /**
     * 前台
     * 条件分页查找父级评论列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "前台 条件分页获取父级评论列表")
    @PostMapping("/frontGetParentComment")
    public BaseResponse<PageInfoResult<BlogComment>> frontGetParentComment(@RequestBody Map<String,Object> request){
        PageInfoResult<BlogComment> pageInfoResult = blogCommentService.frontGetParentComment(request);

        return ResultUtils.success(pageInfoResult,"分页查找评论成功");
    }

    /**
     * 前台
     * 条件分页查找子级评论列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "前台 条件分页获取子级评论列表")
    @PostMapping("/frontGetChildrenComment")
    public BaseResponse<PageInfoResult<BlogComment>> frontGetChildrenComment(@RequestBody Map<String,Object> request){
        PageInfoResult<BlogComment> pageInfoResult = blogCommentService.frontGetChildrenComment(request);

        return ResultUtils.success(pageInfoResult,"分页查找子评论成功");
    }

    /**
     * 后台
     * 条件分页获取评论
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "后台 条件分页获取评论")
    @PostMapping("/backGetCommentList")
    public BaseResponse<PageInfoResult<BlogComment>> backGetCommentList(@RequestBody Map<String,Object> request){
        PageInfoResult<BlogComment> pageInfoResult = blogCommentService.backGetCommentList(request);
        return ResultUtils.success(pageInfoResult,"分页查找评论成功");
    }

    /**
     * 新增评论
     *
     * @param blogComment
     * @return
     */
    @ApiOperation(value = "新增评论")
    @PostMapping("/add")
    public BaseResponse<BlogComment> addComment(@RequestBody BlogComment blogComment, HttpServletRequest httpServletRequest){
        String clientIp = IpUtils.getClientIp(httpServletRequest);
        BlogComment comment = blogCommentService.createComment(blogComment, clientIp);
        // from_id表示当前登陆人id 发表评论的人和当前登录人不一样才进行消息提示 author_id表示当前被评论的作者的id
        Integer from_id = blogComment.getFrom_id();
        Integer author_id = blogComment.getAuthor_id();
        Integer type = blogComment.getType();
        Integer for_id = blogComment.getFor_id();
        String from_name = blogComment.getFrom_name();
        String content = blogComment.getContent();
        String typeName = CurrentTypeName.getCurrentTypeName(blogComment.getType());
        // 不是作者自己评论的才给作者消息提示
        if (from_id != author_id) {
            blogNotifyService.addNotify(author_id,type,for_id,
                    "您的" + typeName  + "收到了来自于：" + from_name + "的评论:" + content);
        }
        return ResultUtils.success(comment,"新增评论成功");
    }

    /**
     * 回复评论
     *
     * @param blogComment
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "回复评论")
    @PostMapping("/apply")
    public BaseResponse<BlogComment> applyComment(@RequestBody BlogComment blogComment, HttpServletRequest httpServletRequest){
        String clientIp = IpUtils.getClientIp(httpServletRequest);

        BlogComment comment = blogCommentService.applyComment(blogComment, clientIp);

        Integer from_id = blogComment.getFrom_id();
        Integer author_id = blogComment.getAuthor_id();
        Integer type = blogComment.getType();
        Integer for_id = blogComment.getFor_id();
        String from_name = blogComment.getFrom_name();
        Integer to_id = blogComment.getTo_id();
        String content = blogComment.getContent();
        String typeName = CurrentTypeName.getCurrentTypeName(blogComment.getType());

        if (from_id != to_id) {
            blogNotifyService.addNotify(to_id,type,for_id,
                    "您收到了来自于：" + from_name + "的评论回复:" + content);
        }
        return ResultUtils.success(comment,"回复评论成功");
    }

    /**
     * 点赞评论
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "点赞评论")
    @PutMapping("/thumbUp/{id}")
    public BaseResponse<Boolean> thumbUpComment(@PathVariable Integer id){
        Boolean aBoolean = blogCommentService.thumbUpComment(id);
        return ResultUtils.success(aBoolean,"点赞成功");
    }

    /**
     * 取消点赞评论
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "取消点赞评论")
    @PutMapping("/cancelThumbUp/{id}")
    public BaseResponse<Boolean> cancelThumbUp(@PathVariable Integer id){
        Boolean aBoolean = blogCommentService.cancelThumbUp(id);
        return ResultUtils.success(aBoolean,"取消点赞成功");
    }

    /**
     * 前台后台
     * 删除评论
     *
     * @param id
     * @param parent_id
     * @return
     */
    @ApiOperation(value = "前台后台 删除评论")
    @DeleteMapping("/delete/{id}/{parent_id}")
    public BaseResponse<Boolean> deleteComment(@PathVariable Integer id,@PathVariable Integer parent_id){
        Boolean aBoolean = blogCommentService.deleteComment(id, parent_id);

        return ResultUtils.success(aBoolean,"删除评论成功");
    }

}
