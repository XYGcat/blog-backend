package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.enums.ErrorCode;
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
    public BaseResponse<Boolean> getIsLikeByIdAndType(@RequestBody Map<String,Long> request){
        Long forId = request.get("for_id");
        Integer type = Math.toIntExact(request.get("type"));
        Long userId = request.get("user_id");
        if (forId == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (type == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (userId == null) {
            return ResultUtils.success(false,"获取用户是否点赞成功");
        }else {
            Boolean like = blogLikeService.getIsLikeByIdAndType(forId, type, userId);
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
    public BaseResponse<Boolean> addLike(@RequestBody Map<String,Long> request){
        Long forId = request.get("for_id");
        Integer type = Math.toIntExact(request.get("type"));
        Long userId = request.get("user_id");

        if (forId == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (type == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (userId == null) {
            return ResultUtils.success(false, "获取用户是否点赞成功");
        }else {
            Boolean aBoolean = blogLikeService.addLike(forId, type, userId);
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
    public BaseResponse<Boolean> cancelLike(@RequestBody Map<String,Long> request){
        Long forId = request.get("for_id");
        Integer type = Math.toIntExact(request.get("type"));
        Long userId = request.get("user_id");

        if (forId == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (type == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        if (userId == null) {
            return ResultUtils.success(false, "获取用户是否点赞成功");
        }else {
            Boolean aBoolean = blogLikeService.cancelLike(forId, type, userId);
            return ResultUtils.success(aBoolean,"点赞成功");
        }
    }
}
