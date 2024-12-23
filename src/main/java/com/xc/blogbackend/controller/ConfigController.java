package com.xc.blogbackend.controller;

import com.qiniu.common.QiniuException;
import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.enums.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.entity.BlogConfig;
import com.xc.blogbackend.service.BlogConfigService;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 网站设置接口
 *
 * @author 星尘
 */
@Api(tags = "网站设置接口")
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
    @ApiOperation(value = "获取网站设置")
    @GetMapping("/getSiteConfig")
    public BaseResponse<BlogConfig> getConfig(){
        BlogConfig config = blogConfigService.getConfig();

        if (config != null) {
            try {
                String avatar_bg = qiniu.downloadUrl(config.getAvatarBg());
                String bg_avatar = qiniu.downloadUrl(config.getBgAvatar());
                String qq_link = qiniu.downloadUrl(config.getQqLink());
                String we_chat_link = qiniu.downloadUrl(config.getWeChatLink());
                config.setAvatarBg(avatar_bg);
                config.setBgAvatar(bg_avatar);
                config.setQqLink(qq_link);
                config.setWeChatLink(we_chat_link);
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
    @ApiOperation(value = "增加网站访问次数")
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
    @ApiOperation(value = "修改网站设置")
    @PostMapping("/updata")
    public BaseResponse<Boolean> updateConfig(@RequestBody BlogConfig blogConfig){

        //// TODO: 2023-12-07 该页面头像和用户界面头像应该一致
        String avatar_bg = blogConfig.getAvatarBg();
        String bg_avatar = blogConfig.getBgAvatar();
        String qq_link = blogConfig.getQqLink();
        String we_chat_link = blogConfig.getWeChatLink();

        BlogConfig config = blogConfigService.getConfig();

        // 如果背景图不一致，删除原来的
        if (config != null){
            if (avatar_bg != null && config.getAvatarBg() != null && avatar_bg != config.getAvatarBg()){
                String subString = StringManipulation.subString(config.getAvatarBg());
                qiniu.deleteFile(subString);
            }
            if (bg_avatar != null && config.getBgAvatar() != null && bg_avatar != config.getBgAvatar()){
                String subString = StringManipulation.subString(config.getBgAvatar());
                qiniu.deleteFile(subString);
            }
            if (qq_link != null && config.getQqLink() != null && qq_link != config.getQqLink()){
                String subString = StringManipulation.subString(config.getQqLink());
                qiniu.deleteFile(subString);
            }
            if (we_chat_link != null && config.getWeChatLink() != null && we_chat_link != config.getWeChatLink()){
                String subString = StringManipulation.subString(config.getWeChatLink());
                qiniu.deleteFile(subString);
            }
        }

        Boolean aBoolean = blogConfigService.updateConfig(blogConfig);

        return ResultUtils.success(aBoolean,"修改网站设置成功");
    }

}
