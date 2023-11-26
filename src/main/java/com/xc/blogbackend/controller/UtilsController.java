package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.utils.ImageNamingUtil;
import com.xc.blogbackend.utils.Qiniu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/upload")
public class UtilsController {

    @Resource
    private Qiniu qiniu;

    @GetMapping("/uploadToken")
    public BaseResponse<List<String>> getuploadToken(){
        String uploadToken = qiniu.uploadToken();
        String imageName = ImageNamingUtil.generateUniqueImageName();
        List<String> tokenList = new ArrayList<>();
        tokenList.add(uploadToken);
        tokenList.add(imageName);
        return ResultUtils.success(tokenList);
    }
}
