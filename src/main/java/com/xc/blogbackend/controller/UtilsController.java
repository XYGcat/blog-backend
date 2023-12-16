package com.xc.blogbackend.controller;

import com.qiniu.common.QiniuException;
import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.utils.ImageNamingUtil;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
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

    /**
     * 生成七牛云上传凭证
     *
     * @return
     */
    @GetMapping("/uploadToken")
    public BaseResponse<List<String>> getuploadToken(){
        String uploadToken = qiniu.uploadToken();
        String imageName = ImageNamingUtil.generateUniqueImageName();
        List<String> tokenList = new ArrayList<>();
        tokenList.add(uploadToken);
        tokenList.add(imageName);
        return ResultUtils.success(tokenList);
    }

    /**
     * 删除七牛云图片
     *
     * @param request
     * @return
     */
    @PostMapping("/deleteFile")
    public BaseResponse<Boolean> deleteFile(@RequestBody Map<String,String> request){
        String imgUrl = request.get("imgUrl");
        String subString = StringManipulation.subString(imgUrl);
        Boolean file = qiniu.deleteFile(subString);
        if (file) {
            return ResultUtils.success(true,"删除成功！");
        }else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     *生成七牛云下载图片的token
     *
     * @param request
     * @return
     */
    @PostMapping("/downloadUrl")
    public BaseResponse<String> downloadUrl(@RequestBody Map<String,String> request) throws QiniuException {
        String imgUrl = request.get("imgUrl");
        String downloadUrl = qiniu.downloadUrl(imgUrl);

        return ResultUtils.success(downloadUrl);
    }
}
