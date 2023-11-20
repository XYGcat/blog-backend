package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogTalkPhotoMapper;
import com.xc.blogbackend.model.domain.BlogTalkPhoto;
import com.xc.blogbackend.service.BlogTalkPhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_talk_photo】的数据库操作Service实现
* @createDate 2023-11-20 15:28:02
*/
@Service
@Slf4j
public class BlogTalkPhotoServiceImpl extends ServiceImpl<BlogTalkPhotoMapper, BlogTalkPhoto>
    implements BlogTalkPhotoService{

    private final QueryWrapper<BlogTalkPhoto> queryWrapper = new QueryWrapper<>();

    @Resource
    private BlogTalkPhotoMapper blogTalkPhotoMapper;

    @Override
    public List<BlogTalkPhoto> getPhotoByTalkId(Integer talk_id) {
        queryWrapper.clear();
        queryWrapper.eq("talk_id",talk_id);
        List<BlogTalkPhoto> talkPhotos = blogTalkPhotoMapper.selectList(queryWrapper);
        //返回一个经过处理的对象数组
        if (talkPhotos != null) {
            ArrayList<BlogTalkPhoto> talkPhotosList = new ArrayList<>();
            for (BlogTalkPhoto blogTalkPhoto : talkPhotos){
                BlogTalkPhoto talkPhoto = new BlogTalkPhoto();
                talkPhoto.setTalk_id(blogTalkPhoto.getTalk_id());
                talkPhoto.setUrl(blogTalkPhoto.getUrl());
                talkPhotosList.add(talkPhoto);
            }
            return talkPhotosList;
        }
        return talkPhotos;
    }
}




