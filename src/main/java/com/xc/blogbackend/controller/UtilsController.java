package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.utils.ImageNamingUtil;
import com.xc.blogbackend.utils.Qiniu;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/deleteFile")
    public BaseResponse<Boolean> deleteFile(@RequestBody Map<String,String> request){
        String article_cover = request.get("article_cover");
        Boolean file = qiniu.deleteFile(article_cover);
        if (file) {
            return ResultUtils.success(true,"删除成功！");
        }else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }
}
