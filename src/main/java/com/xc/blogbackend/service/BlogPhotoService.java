package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogPhoto;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

import java.util.List;

/**
* @author XC
* @description 针对表【blog_photo】的数据库操作Service
* @createDate 2023-11-30 13:53:22
*/
public interface BlogPhotoService extends IService<BlogPhoto> {

    /**
     * 根据相册id删除图片
     *
     * @param album_id
     * @return
     */
    Boolean deletePhotosByAlbumId(Integer album_id);

    /**
     * 后台
     * 分页获取图片列表
     *
     * @param current
     * @param size
     * @param id
     * @param status
     * @return
     */
    PageInfoResult<BlogPhoto> getPhotosByAlbumId(Integer current, Integer size, Integer id, Integer status);

    /**
     * 前台
     * 获取相册的所有照片
     *
     * @param album_id
     * @return
     */
    List<BlogPhoto> getAllPhotosByAlbumId(Integer album_id);

    /**
     * 批量新增图片
     *
     * @param photoList
     * @return
     */
    Boolean addPhotos(List<BlogPhoto> photoList);

    /**
     * 批量删除图片
     *
     * @param idList
     * @param type
     * @return
     */
    Boolean deletePhotos(List<Integer> idList,Integer type);

    /**
     * 批量恢复图片
     *
     * @param idList
     * @return
     */
    Boolean revertPhotos(List<Integer> idList);
}
