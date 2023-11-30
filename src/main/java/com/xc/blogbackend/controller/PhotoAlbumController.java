package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogPhotoAlbum;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogPhotoAlbumService;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @Resource
    private Qiniu qiniu;

    /**
     *分页获取相册列表
     *
     * @param request
     * @return
     */
    @PostMapping("/getPhotoAlbum")
    public BaseResponse<PageInfoResult<BlogPhotoAlbum>> getAlbumList(@RequestBody Map<String,Object> request){
        String album_name = (String) request.get("album_name");
        Integer current = (Integer) request.get("current");
        Integer size = (Integer) request.get("size");

        PageInfoResult<BlogPhotoAlbum> albumList = blogPhotoAlbumService.getAlbumList(album_name, current, size);

        return ResultUtils.success(albumList,"获取相册列表成功");
    }

    /**
     * 新增相册
     *
     * @param request
     * @return
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<BlogPhotoAlbum> addAlbum(@RequestBody Map<String,Object> request){
        String album_name = (String) request.get("album_name");
        String album_cover = (String) request.get("album_cover");
        String description = (String) request.get("description");

        BlogPhotoAlbum oneAlbum = blogPhotoAlbumService.getOneAlbum(null, album_name);
        if (oneAlbum != null) {
            return ResultUtils.error(400,"已经存在相同的相册名称，换一个试试",null);
        }
        BlogPhotoAlbum blogPhotoAlbum = blogPhotoAlbumService.addAlbum(album_name, album_cover, description);
        return ResultUtils.success(blogPhotoAlbum,"创建相册成功");
    }

    /**
     * 修改相册
     *
     * @param request
     * @return
     */
    @PutMapping("/update")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> updateAlbum(@RequestBody Map<String,Object> request){
        Integer id = (Integer) request.get("id");
        String album_name = (String) request.get("album_name");
        String album_cover = (String) request.get("album_cover");
        String description = (String) request.get("description");

        BlogPhotoAlbum oneAlbum = blogPhotoAlbumService.getOneAlbum(null, album_name);
        if (oneAlbum != null && oneAlbum.getId() != id) {
            return ResultUtils.error(400,"已经存在相同的相册名称，换一个试试",null);
        }

        BlogPhotoAlbum album = blogPhotoAlbumService.getOneAlbum(id, null);

        // 删除原来存储的照片
        if (album_cover != album.getAlbum_cover()) {
            Boolean aBoolean = qiniu.deleteFile(StringManipulation.subString(album.getAlbum_cover()));
        }

        Boolean aBoolean = blogPhotoAlbumService.updateAlbum(id, album_name, album_cover, description);

        return ResultUtils.success(aBoolean,"修改相册成功");
    }

    /**
     * 删除相册
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> deleteAlbum(@PathVariable Integer id){
        BlogPhotoAlbum oneAlbum = blogPhotoAlbumService.getOneAlbum(id, null);

        Boolean aBoolean = qiniu.deleteFile(StringManipulation.subString(oneAlbum.getAlbum_cover()));

        Boolean deleteAlbum = blogPhotoAlbumService.deleteAlbum(id);

        return ResultUtils.success(deleteAlbum,"删除相册成功");
    }
}
