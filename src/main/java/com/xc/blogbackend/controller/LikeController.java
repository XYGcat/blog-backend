package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.service.BlogLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 点赞接口
 *
 * @author 星尘
 */
@Api(tags = "点赞接口")
@RestController
@RequestMapping("/like")
public class LikeController {

    @Resource
    private BlogLikeService blogLikeService;

    /**
     * 获取当前用户对当前文章/说说/留言 是否点赞
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "获取当前用户对当前文章/说说/留言 是否点赞")
    @PostMapping("/getIsLikeByIdAndType")
    public BaseResponse<Boolean> getIsLikeByIdAndType(@RequestBody Map<String,Integer> request){
        Integer for_id = request.get("for_id");
        Integer type = request.get("type");
        Integer user_id = request.get("user_id");
        if (for_id == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (type == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (user_id == null) {
            return ResultUtils.success(false,"获取用户是否点赞成功");
        }else {
            Boolean like = blogLikeService.getIsLikeByIdAndType(for_id, type, user_id);
            return ResultUtils.success(like,"获取用户是否点赞成功");
        }
    }

    /**
     * 点赞
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "点赞")
    @PostMapping("/addLike")
    public BaseResponse<Boolean> addLike(@RequestBody Map<String,Integer> request){
        Integer for_id = request.get("for_id");
        Integer type = request.get("type");
        Integer user_id = request.get("user_id");

        if (for_id == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (type == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (user_id == null) {
            return ResultUtils.success(false, "获取用户是否点赞成功");
        }else {
            Boolean aBoolean = blogLikeService.addLike(for_id, type, user_id);
            return ResultUtils.success(aBoolean,"点赞成功");
        }
    }

    /**
     * 取消点赞
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "取消点赞")
    @PostMapping("/cancelLike")
    public BaseResponse<Boolean> cancelLike(@RequestBody Map<String,Integer> request){
        Integer for_id = request.get("for_id");
        Integer type = request.get("type");
        Integer user_id = request.get("user_id");

        if (for_id == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (type == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (user_id == null) {
            return ResultUtils.success(false, "获取用户是否点赞成功");
        }else {
            Boolean aBoolean = blogLikeService.cancelLike(for_id, type, user_id);
            return ResultUtils.success(aBoolean,"点赞成功");
        }
    }
}
