package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogMessage;
import com.xc.blogbackend.model.domain.request.PageRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogMessageService;
import com.xc.blogbackend.service.BlogNotifyService;
import com.xc.blogbackend.utils.RandomUsernameGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 留言服务接口
 *
 * @author 星尘
 */
@Api(tags = "留言接口")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private BlogMessageService blogMessageService;

    @Resource
    private BlogNotifyService blogNotifyService;

    /**
     * 分页获取留言
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页获取留言")
    @PostMapping("/getMessageList")
    public BaseResponse<PageInfoResult<BlogMessage>> getMessageList(@RequestBody PageRequest request){
        Integer current = request.getCurrent();
        Integer size = request.getSize();
        String message = request.getMessage();
        List<String> time = request.getTime();
        String tag = request.getTag();
        Integer user_id = request.getUser_id();

        PageInfoResult<BlogMessage> messageList = blogMessageService.getMessageList
                (current, size, message, time,tag,user_id);

        return ResultUtils.success(messageList,"分页获取留言成功");
    }

    /**
     * 获取热门标签
     *
     * @return
     */
    @ApiOperation(value = "获取留言所有标签")
    @GetMapping("/getHotTagList")
    public BaseResponse<List<Map<String, Object>>> getMessageTag(){
        List<Map<String, Object>> messageTag = blogMessageService.getMessageTag();
        return ResultUtils.success(messageTag,"获取留言所有标签成功");
    }

    /**
     * 新增留言
     *
     * @param blogMessage
     * @return
     */
    @ApiOperation(value = "新增留言")
    @PostMapping("/add")
    public BaseResponse<Boolean> addMessage(@RequestBody BlogMessage blogMessage){
        Integer user_id = blogMessage.getUser_id();
        String message = blogMessage.getMessage();
        String nick_name = blogMessage.getNick_name();
        if (user_id == null) {
            nick_name = RandomUsernameGenerator.generateRandomUsername();
        }
        Boolean aBoolean = blogMessageService.addMessage(blogMessage);

        // 发布消息推送
        if (user_id != 1) {
            blogNotifyService.addNotify(1, 3, null, "您收到了来自于：" + nick_name + "的留言:" + message);
        }

        return ResultUtils.success(aBoolean,"发布成功");
    }

    /**
     * 修改留言
     *
     * @param blogMessage
     * @return
     */
    @ApiOperation(value = "修改留言")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateMessage(@RequestBody BlogMessage blogMessage){
        Boolean aBoolean = blogMessageService.updateMessage(blogMessage);
        return ResultUtils.success(aBoolean,"修改成功");
    }

    /**
     * 删除留言
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "删除留言")
    @PutMapping("/delete")
    public BaseResponse<Integer> deleteMessage(@RequestBody Map<String,List<Integer>> request){
        List<Integer> idList = request.get("idList");
        Integer integer = blogMessageService.deleteMessage(idList);
        return ResultUtils.success(integer,"删除留言成功");
    }

    /**
     * 点赞留言
     *
     * @param id
     * @return
     */
    @PutMapping("/like/{id}")
    public BaseResponse<Boolean> likeMessage(@PathVariable Integer id){
        Boolean aBoolean = blogMessageService.likeMessage(id);
        return ResultUtils.success(aBoolean,"留言点赞成功");
    }

    /**
     * 取消点赞留言
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "取消点赞留言")
    @PutMapping("/cancelLike/{id}")
    public BaseResponse<Boolean> cancelLikeMessage(@PathVariable Integer id){
        Boolean aBoolean = blogMessageService.cancelLikeMessage(id);
        return ResultUtils.success(aBoolean,"取消留言点赞成功");
    }
}
