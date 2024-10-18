package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogTalkPhotoMapper;
import com.xc.blogbackend.model.domain.BlogTalkPhoto;
import com.xc.blogbackend.service.BlogTalkPhotoService;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author XC
* @description 针对表【bg_talk_photo】的数据库操作Service实现
* @createDate 2023-11-20 15:28:02
*/
@Service
@Slf4j
public class BlogTalkPhotoServiceImpl extends ServiceImpl<BlogTalkPhotoMapper, BlogTalkPhoto>
    implements BlogTalkPhotoService{

    @Resource
    private BlogTalkPhotoMapper blogTalkPhotoMapper;

    @Resource
    private Qiniu qiniu;

    @Override
    public List<BlogTalkPhoto> getPhotoByTalkId(Integer talk_id) {
        QueryWrapper<BlogTalkPhoto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("talk_id",talk_id);
        List<BlogTalkPhoto> talkPhotos = blogTalkPhotoMapper.selectList(queryWrapper);
        //返回一个经过处理的对象数组
        if (talkPhotos != null) {
            List<BlogTalkPhoto> talkPhotosList = new ArrayList<>();
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

    @Override
    public List<BlogTalkPhoto> publishTalkPhoto(List<BlogTalkPhoto> imgList) {
        saveBatch(imgList);
        return imgList;
    }

    @Override
    public Boolean deleteTalkPhoto(Integer talk_id) {
        List<String> keys = new ArrayList<>();
        QueryWrapper<BlogTalkPhoto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("talk_id",talk_id);
        queryWrapper.select("url");
        List<BlogTalkPhoto> talkPhotos = blogTalkPhotoMapper.selectList(queryWrapper);

        keys = talkPhotos.stream().map(v -> {
            return StringManipulation.subString(v.getUrl());
        }).collect(Collectors.toList());
        //删除图片
        Boolean deleteFile = qiniu.deleteFile(keys);

        queryWrapper.clear();
        queryWrapper.eq("talk_id",talk_id);
        int delete = blogTalkPhotoMapper.delete(queryWrapper);

        return delete > 0;
    }
}




