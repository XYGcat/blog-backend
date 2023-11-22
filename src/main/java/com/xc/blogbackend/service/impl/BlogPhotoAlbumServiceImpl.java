package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogPhotoAlbumMapper;
import com.xc.blogbackend.model.domain.BlogPhotoAlbum;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogPhotoAlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_photo_album】的数据库操作Service实现
* @createDate 2023-11-22 16:10:53
*/
@Service
@Slf4j
public class BlogPhotoAlbumServiceImpl extends ServiceImpl<BlogPhotoAlbumMapper, BlogPhotoAlbum>
    implements BlogPhotoAlbumService{

    @Resource
    private BlogPhotoAlbumMapper blogPhotoAlbumMapper;

    private final QueryWrapper<BlogPhotoAlbum> queryWrapper = new QueryWrapper<>();

    @Override
    public PageInfoResult<BlogPhotoAlbum> getAlbumList(String album_name, Integer current, Integer size) {
        // 分页参数处理
        int offset = (current - 1) * size;
        int limit = size;
        //构建查询条件
        queryWrapper.clear();
        if (album_name != null && !album_name.isEmpty()) {
            queryWrapper.like("album_name", "%" + album_name + "%");
        }
        // 创建Page对象，设置当前页和分页大小
        Page<BlogPhotoAlbum> page = new Page<>(offset, limit);
        // 获取说说列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogPhotoAlbum> photoAlbumPage = blogPhotoAlbumMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogPhotoAlbum> rows = photoAlbumPage.getRecords();
        // 获取说说总数
        long count = photoAlbumPage.getTotal();

        //添加返回值
        PageInfoResult<BlogPhotoAlbum> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }
}




