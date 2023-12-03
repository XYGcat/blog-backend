package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.service.BlogCommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 评论接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private BlogCommentService blogCommentService;

    /**
     * 获取关于当前的评论总数 子评论 + 父评论
     *
     * @param request
     * @return
     */
    @PostMapping("/getCommentTotal")
    public BaseResponse<Long> getCommentTotal(@RequestBody Map<String,Integer> request){
        Integer for_id = request.get("for_id");
        Integer type = request.get("type");
        Long commentTotal = blogCommentService.getCommentTotal(for_id, type);
        return ResultUtils.success(commentTotal,"获取评论总条数成功");
    }
}
