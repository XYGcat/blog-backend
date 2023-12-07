package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.xc.blogbackend.mapper.BlogHeaderMapper;
import com.xc.blogbackend.model.domain.BlogHeader;
import com.xc.blogbackend.service.BlogHeaderService;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_header】的数据库操作Service实现
* @createDate 2023-11-23 16:14:16
*/
@Service
@Slf4j
public class BlogHeaderServiceImpl extends ServiceImpl<BlogHeaderMapper, BlogHeader>
    implements BlogHeaderService{

    @Resource
    private BlogHeaderMapper blogHeaderMapper;

    @Resource
    private Qiniu qiniu;

    @Override
    public List<BlogHeader> getAllHeader() {
        QueryWrapper<BlogHeader> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "route_name", "bg_url");
        List<BlogHeader> blogHeaders = blogHeaderMapper.selectList(queryWrapper);

        for (BlogHeader v : blogHeaders){
            try {
                String url = qiniu.downloadUrl(v.getBg_url());
                v.setBg_url(url);
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        return blogHeaders;
    }

    @Override
    public Boolean addOrUpdateHeader(BlogHeader blogHeader) {
        Integer id = blogHeader.getId();
        if (id != null){
            //删除七牛云旧图片
            BlogHeader selectById = blogHeaderMapper.selectById(id);
            String subString = StringManipulation.subString(selectById.getBg_url());
            qiniu.deleteFile(subString);

            int i = blogHeaderMapper.updateById(blogHeader);
            return i > 0;
        }else {
            int insert = blogHeaderMapper.insert(blogHeader);
            return insert > 0;
        }
    }

    @Override
    public BlogHeader getOneByPath(String route_name) {
        QueryWrapper<BlogHeader> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("route_name",route_name);
        BlogHeader blogHeader = blogHeaderMapper.selectOne(queryWrapper);
        return blogHeader;
    }

    @Override
    public Boolean deleteHeader(Integer id) {
        int deleteById = blogHeaderMapper.deleteById(id);
        return deleteById > 0;
    }
}




