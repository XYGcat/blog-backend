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
}
