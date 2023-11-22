package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogPhotoAlbum;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

/**
* @author XC
* @description 针对表【blog_photo_album】的数据库操作Service
* @createDate 2023-11-22 16:10:53
*/
public interface BlogPhotoAlbumService extends IService<BlogPhotoAlbum> {

    /**
     *获取相册列表
     *
     * @param album_name    相册名称
     * @param current
     * @param size
     * @return
     */
    PageInfoResult<BlogPhotoAlbum> getAlbumList(String album_name,Integer current,Integer size);

}
