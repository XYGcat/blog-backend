package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogNotify;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogNotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 通知接口
 *
 * @author 星尘
 */
@Api(tags = "通知接口")
@RestController
@RequestMapping("/notify")
public class NotifyController {

    @Resource
    private BlogNotifyService blogNotifyService;

    /**
     * 条件分页获取消息推送
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "条件分页获取消息推送")
    @PostMapping("/getNotifyList")
    public BaseResponse<PageInfoResult<BlogNotify>> getNotifyList(@RequestBody Map<String,Integer> request){
        Integer current = request.get("current");
        Integer size = request.get("size");
        Integer userId = request.get("userId");
        PageInfoResult<BlogNotify> notifyList = blogNotifyService.getNotifyList(current, size, userId);
        return ResultUtils.success(notifyList,"分页查找消息通知成功");
    }

    /**
     * 修改消息推送
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "修改消息推送")
    @PutMapping("/update/{id}")
    public BaseResponse<Boolean> updateNotify(@PathVariable Integer id){
        Boolean aBoolean = blogNotifyService.updateNotify(id);
        return ResultUtils.success(aBoolean,"已阅消息通知成功");
    }

    /**
     * 删除消息推送
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除消息推送")
    @PutMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteNotifys(@PathVariable Integer id){
        Boolean aBoolean = blogNotifyService.deleteNotifys(id);
        return ResultUtils.success(aBoolean,"删除消息通知成功");
    }


//    直接在service类中定义了
//    /**
//     * 新增消息通知
//     *
//     * @param user_id
//     * @param type
//     * @param to_id
//     * @param message
//     */
//    public void addNotify(Integer user_id,Integer type,Integer to_id,String message){
//        BlogNotify notify = blogNotifyService.createNotify(user_id, type, to_id, message);
//    }
}
