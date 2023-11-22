package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.model.domain.BlogPhotoAlbum;
import com.xc.blogbackend.mapper.BlogPhotoAlbumMapper;
import com.xc.blogbackend.service.BlogPhotoAlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @author XC
* @description 针对表【blog_photo_album】的数据库操作Service实现
* @createDate 2023-11-22 16:10:53
*/
@Service
@Slf4j
public class BlogPhotoAlbumServiceImpl extends ServiceImpl<BlogPhotoAlbumMapper, BlogPhotoAlbum>
    implements BlogPhotoAlbumService{

}




