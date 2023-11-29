package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogTalkPhoto;

import java.util.List;

/**
* @author XC
* @description 针对表【blog_talk_photo】的数据库操作Service
* @createDate 2023-11-20 15:28:02
*/
public interface BlogTalkPhotoService extends IService<BlogTalkPhoto> {

    /**
     * 根据说说id获取图片列表
     *
     * @param talk_id   说说id
     * @return
     */
   List<BlogTalkPhoto> getPhotoByTalkId(Integer talk_id);

    /**
     * 新增说说图片
     *
     * @param imgList
     * @return
     */
   List<BlogTalkPhoto> publishTalkPhoto(List<BlogTalkPhoto> imgList);

    /**
     * 根据说说id删除图片
     *
     * @param talk_id
     * @return
     */
   Boolean deleteTalkPhoto(Integer talk_id);
}
