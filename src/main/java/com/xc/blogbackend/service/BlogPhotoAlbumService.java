package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogPhotoAlbum;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

import java.util.List;

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

    /**
     * 新增相册
     *
     * @param album_name
     * @param album_cover
     * @param description
     * @return
     */
    BlogPhotoAlbum addAlbum(String album_name,String album_cover,String description);

    /**
     * 根据id 或 相册名称获取相册信息
     *
     * @param id
     * @param album_name
     * @return
     */
    BlogPhotoAlbum getOneAlbum(Integer id,String album_name);

    /**
     * 编辑相册
     *
     * @param id
     * @param album_name
     * @param album_cover
     * @param description
     * @return
     */
    Boolean updateAlbum(Integer id,String album_name,String album_cover,String description);

    /**
     * 根据id删除相册
     *
     * @param id
     * @return
     */
    Boolean deleteAlbum(Integer id);

    /**
     * 前台
     * 获取全部相册列表
     *
     * @return
     */
    List<BlogPhotoAlbum> getAllAlbumList();
}
