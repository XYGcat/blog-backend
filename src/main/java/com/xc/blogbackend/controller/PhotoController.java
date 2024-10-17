package com.xc.blogbackend.controller;

import com.qiniu.common.QiniuException;
import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogPhoto;
import com.xc.blogbackend.model.domain.request.DelPhotoRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogPhotoService;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 相册图片接口
 *
 * @author 星尘
 */
@Api(tags = "相册图片接口")
@RestController
@RequestMapping("/photo")
public class PhotoController {

    @Resource
    private BlogPhotoService blogPhotoService;

    @Resource
    private Qiniu qiniu;

    /**
     * 后台
     * 根据相册id 分页获取图片列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "后台 根据相册id分页获取图片列表")
    @PostMapping("/getPhotoListByAlbumId")
    public BaseResponse<PageInfoResult<BlogPhoto>> getPhotosByAlbumId(@RequestBody Map<String,Object> request){
        Integer current = (Integer) request.get("current");
        Integer size = (Integer) request.get("size");
        Integer id = (Integer) request.get("id");
        Integer status = (Integer) request.get("status");

        PageInfoResult<BlogPhoto> photosByAlbumId = blogPhotoService.getPhotosByAlbumId(current, size, id, status);

        return ResultUtils.success(photosByAlbumId,"获取相册图片成功");
    }

    /**
     * 前台
     * 根据相册id 获取相册的所有图片
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "前台 根据相册id获取相册的所有图片")
    @GetMapping("/getAllPhotosByAlbumId/{id}")
    public BaseResponse<List<BlogPhoto>> getAllPhotosByAlbumId(@PathVariable Integer id){
        List<BlogPhoto> allPhotosByAlbumId = blogPhotoService.getAllPhotosByAlbumId(id);
        for (BlogPhoto v : allPhotosByAlbumId){
            try {
                String url = qiniu.downloadUrl(v.getUrl());
                v.setUrl(url);
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }
        return ResultUtils.success(allPhotosByAlbumId,"获取相册所有照片成功");
    }

    /**
     *批量新增图片 判断是否有id来新增 并且需要传入相册id记录相册
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "批量新增图片")
    @PostMapping("/add")
    public BaseResponse<Boolean> addPhotos(@RequestBody Map<String,Object> request){
        List<BlogPhoto> photoList = (List<BlogPhoto>) request.get("photoList");
        Boolean aBoolean = blogPhotoService.addPhotos(photoList);
        return ResultUtils.success(aBoolean,"新增图片成功");
    }

    /**
     * 批量删除图片
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "批量删除图片")
    @PutMapping("/delete")
    public BaseResponse<Boolean> deletePhotos(@RequestBody DelPhotoRequest request) throws UnsupportedEncodingException {
        List<BlogPhoto> imgList = request.getImgList();
        Integer type = request.getType();

        List<Integer> idList = imgList.stream().map(v -> {
            return v.getId();
        }).collect(Collectors.toList());
        Boolean deletePhotos = blogPhotoService.deletePhotos(idList, type);

        if (type == 2) {
            // 远程删除图片
            List<String> keys = imgList.stream().map(v -> {
                String subString = StringManipulation.subString(v.getUrl());
                return subString;
            }).collect(Collectors.toList());
            Boolean deleteFile = qiniu.deleteFile(keys);
        }

        return ResultUtils.success(deletePhotos,"删除图片成功");
    }

    /**
     * 批量恢复图片
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "批量恢复图片")
    @PutMapping("/revert")
    public BaseResponse<Boolean> revertPhotos(@RequestBody Map<String,List<Integer>> request){
        List<Integer> idList = request.get("idList");
        Boolean aBoolean = blogPhotoService.revertPhotos(idList);
        return ResultUtils.success(aBoolean,"恢复图片成功");
    }
}
