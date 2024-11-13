package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.entity.BlogPhotoAlbum;
import com.xc.blogbackend.model.domain.resDto.PageInfoResult;

import java.util.List;

/**
* @author XC
* @description 针对表【bg_photo_album】的数据库操作Service
* @createDate 2023-11-22 16:10:53
*/
public interface BlogPhotoAlbumService extends IService<BlogPhotoAlbum> {

    /**
     *获取相册列表
     *
     * @param albumName    相册名称
     * @param current
     * @param size
     * @return
     */
    PageInfoResult<BlogPhotoAlbum> getAlbumList(String albumName,Integer current,Integer size);

    /**
     * 新增相册
     *
     * @param albumName
     * @param albumCover
     * @param description
     * @return
     */
    BlogPhotoAlbum addAlbum(String albumName,String albumCover,String description);

    /**
     * 根据id 或 相册名称获取相册信息
     *
     * @param id
     * @param albumName
     * @return
     */
    BlogPhotoAlbum getOneAlbum(Long id,String albumName);

    /**
     * 编辑相册
     *
     * @param id
     * @param albumName
     * @param albumCover
     * @param description
     * @return
     */
    Boolean updateAlbum(Long id,String albumName,String albumCover,String description);

    /**
     * 根据id删除相册
     *
     * @param id
     * @return
     */
    Boolean deleteAlbum(Long id);

    /**
     * 前台
     * 获取全部相册列表
     *
     * @return
     */
    List<BlogPhotoAlbum> getAllAlbumList();
}
