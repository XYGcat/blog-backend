package com.xc.blogbackend.controller;

import com.qiniu.common.QiniuException;
import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.BlogConfig;
import com.xc.blogbackend.service.BlogConfigService;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 网站设置接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private BlogConfigService blogConfigService;

    @Resource
    private Qiniu qiniu;

    /**
     *获取网站设置
     *
     * @return
     */
    @GetMapping("/getdata")
    public BaseResponse<BlogConfig> getConfig(){
        BlogConfig config = blogConfigService.getConfig();

        if (config != null) {
            try {
                String avatar_bg = qiniu.downloadUrl(config.getAvatar_bg());
                String blog_avatar = qiniu.downloadUrl(config.getBlog_avatar());
                String qq_link = qiniu.downloadUrl(config.getQq_link());
                String we_chat_link = qiniu.downloadUrl(config.getWe_chat_link());
                config.setAvatar_bg(avatar_bg);
                config.setBlog_avatar(blog_avatar);
                config.setQq_link(qq_link);
                config.setWe_chat_link(we_chat_link);
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
            return ResultUtils.success(config,"获取网站设置成功");
        }else {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
    }

    /**
     * 增加网站访问次数
     *
     * @return
     */
    @PutMapping("/addView")
    public BaseResponse<String> addView(){
        String res = blogConfigService.addView();
        if (res == "添加成功") {
            return ResultUtils.success(res,"增加访问量成功");
        }else {
            return ResultUtils.success(res,"请先初始化网站信息");
        }
    }

    /**
     * 修改网站设置
     *
     * @param blogConfig
     * @return
     */
    @PostMapping("/updata")
    public BaseResponse<Boolean> updateConfig(@RequestBody BlogConfig blogConfig){

        //// TODO: 2023-12-07 该页面头像和用户界面头像应该一致
        String avatar_bg = blogConfig.getAvatar_bg();
        String blog_avatar = blogConfig.getBlog_avatar();
        String qq_link = blogConfig.getQq_link();
        String we_chat_link = blogConfig.getWe_chat_link();

        BlogConfig config = blogConfigService.getConfig();

        // 如果背景图不一致，删除原来的
        if (config != null){
            if (avatar_bg != null && config.getAvatar_bg() != null && avatar_bg != config.getAvatar_bg()){
                String subString = StringManipulation.subString(config.getAvatar_bg());
                qiniu.deleteFile(subString);
            }
            if (blog_avatar != null && config.getBlog_avatar() != null && blog_avatar != config.getBlog_avatar()){
                String subString = StringManipulation.subString(config.getBlog_avatar());
                qiniu.deleteFile(subString);
            }
            if (qq_link != null && config.getQq_link() != null && qq_link != config.getQq_link()){
                String subString = StringManipulation.subString(config.getQq_link());
                qiniu.deleteFile(subString);
            }
            if (we_chat_link != null && config.getWe_chat_link() != null && we_chat_link != config.getWe_chat_link()){
                String subString = StringManipulation.subString(config.getWe_chat_link());
                qiniu.deleteFile(subString);
            }
        }

        Boolean aBoolean = blogConfigService.updateConfig(blogConfig);

        return ResultUtils.success(aBoolean,"修改网站设置成功");
    }

}
