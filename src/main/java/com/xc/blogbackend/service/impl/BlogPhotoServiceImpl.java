package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.xc.blogbackend.mapper.BlogPhotoMapper;
import com.xc.blogbackend.model.domain.BlogPhoto;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogPhotoService;
import com.xc.blogbackend.utils.Qiniu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_photo】的数据库操作Service实现
* @createDate 2023-11-30 13:53:22
*/
@Service
@Slf4j
public class BlogPhotoServiceImpl extends ServiceImpl<BlogPhotoMapper, BlogPhoto>
    implements BlogPhotoService{

    @Resource
    private BlogPhotoMapper blogPhotoMapper;

    @Resource
    private Qiniu qiniu;

    @Override
    public Boolean deletePhotosByAlbumId(Integer album_id) {
        QueryWrapper<BlogPhoto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("album_id",album_id);
        int delete = blogPhotoMapper.delete(queryWrapper);
        return delete > 0;
    }

    @Override
    public PageInfoResult<BlogPhoto> getPhotosByAlbumId(Integer current, Integer size, Integer id, Integer status) {
        // 分页参数处理
        int offset = (current - 1) * size;
        int limit = size;
        //构建查询条件
        QueryWrapper<BlogPhoto> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("album_id",id);
        }
        if (status != null) {
            queryWrapper.eq("status",status);
        }
        // 创建Page对象，设置当前页和分页大小
        Page<BlogPhoto> page = new Page<>(offset, limit);
        // 获取说说列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogPhoto> photoPage = blogPhotoMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogPhoto> rows = photoPage.getRecords();
        // 获取说说总数
        long count = photoPage.getTotal();

        for (BlogPhoto blogPhoto : rows){
            String downloadUrl = null;
            try {
                downloadUrl = qiniu.downloadUrl(blogPhoto.getUrl());
                blogPhoto.setUrl(downloadUrl);
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        PageInfoResult<BlogPhoto> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setList(rows);
        pageInfoResult.setSize(size);
        pageInfoResult.setTotal(count);
        pageInfoResult.setCurrent(current);

        return pageInfoResult;
    }

    @Override
    public Boolean addPhotos(List<BlogPhoto> photoList) {
        boolean saveBatch = this.saveBatch(photoList);
        return saveBatch;
    }

    @Override
    public Boolean deletePhotos(List<Integer> idList, Integer type) {
        if (type == 1) {
            BlogPhoto blogPhoto = new BlogPhoto();
            blogPhoto.setStatus(2);
            UpdateWrapper<BlogPhoto> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id",idList);
            int update = blogPhotoMapper.update(blogPhoto, updateWrapper);
            return update > 0;
        }else {
            int batchIds = blogPhotoMapper.deleteBatchIds(idList);
            return batchIds > 0;
        }
    }

    @Override
    public Boolean revertPhotos(List<Integer> idList) {
        BlogPhoto blogPhoto = new BlogPhoto();
        blogPhoto.setStatus(1);
        UpdateWrapper<BlogPhoto> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id",idList);
        int update = blogPhotoMapper.update(blogPhoto, updateWrapper);
        return update > 0;
    }
}




