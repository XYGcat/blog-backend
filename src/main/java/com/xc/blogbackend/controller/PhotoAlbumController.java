package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogPhotoAlbum;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogPhotoAlbumService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 相册列表接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/photoAlbum")
public class PhotoAlbumController {

    @Resource
    private BlogPhotoAlbumService blogPhotoAlbumService;

    @PostMapping("/getphotoAlbum")
    public BaseResponse<PageInfoResult<BlogPhotoAlbum>> getphotoAlbum(@RequestBody Map<String,Object> request){
        String album_name = (String) request.get("album_name");
        Integer current = (Integer) request.get("current");
        Integer size = (Integer) request.get("size");

        PageInfoResult<BlogPhotoAlbum> albumList = blogPhotoAlbumService.getAlbumList(album_name, current, size);

        return ResultUtils.success(albumList,"获取相册列表成功");
    }
}
