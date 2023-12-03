package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.service.BlogLikeService;
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
}
