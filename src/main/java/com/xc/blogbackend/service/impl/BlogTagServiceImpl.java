package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogTagMapper;
import com.xc.blogbackend.model.domain.BlogTag;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【blog_tag】的数据库操作Service实现
* @createDate 2023-11-16 11:55:15
*/
@Service
@Slf4j
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag>
    implements BlogTagService{

    @Resource
    private BlogTagMapper blogTagMapper;

    @Override
    public List<BlogTag> getTagDictionary() {
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","tag_name", "createdAt", "updatedAt");
        return blogTagMapper.selectList(queryWrapper);
    }

    @Override
    public BlogTag getOneTag(String tag_name) {
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        if (tag_name != null) {
            queryWrapper.eq("tag_name",tag_name);
        }
        BlogTag blogTag = blogTagMapper.selectOne(queryWrapper);
        return blogTag;
    }

    @Override
    public BlogTag createTag(String tag_name) {
        BlogTag blogTag = new BlogTag();
        blogTag.setTag_name(tag_name);
        blogTagMapper.insert(blogTag);

        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        Integer id = blogTag.getId();
        BlogTag tag = blogTagMapper.selectById(id);
        return tag;
    }

    @Override
    public Long getTagCount() {
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        Long count = blogTagMapper.selectCount(queryWrapper);

        return count;
    }

    @Override
    public PageInfoResult<BlogTag> getTalkList(Integer current, Integer size, String tag_name) {

        // 构建查询条件
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<>();
        // 如果标签名不为空，使用like模糊查询
        if (tag_name != null && !tag_name.isEmpty()) {
            queryWrapper.like("tag_name", "%" + tag_name + "%");
        }

        // 创建Page对象，设置当前页和分页大小
        Page<BlogTag> page = new Page<>(current,size);
        // 获取标签列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogTag> tagPage = blogTagMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogTag> rows = tagPage.getRecords();
        // 获取标签总数
        long count = tagPage.getTotal();

        PageInfoResult<BlogTag> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setSize(size);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }

    @Override
    public Boolean updateTag(Integer id, String tag_name) {
        BlogTag blogTag = new BlogTag();
        blogTag.setTag_name(tag_name);
        UpdateWrapper<BlogTag> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        int update = blogTagMapper.update(blogTag, updateWrapper);
        return update > 0;
    }

    @Override
    public Boolean deleteTags(List<Integer> idList) {
        int batchIds = blogTagMapper.deleteBatchIds(idList);
        return batchIds > 0;
    }
}




