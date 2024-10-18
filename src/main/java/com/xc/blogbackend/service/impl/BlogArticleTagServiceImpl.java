package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogArticleTagMapper;
import com.xc.blogbackend.mapper.BlogTagMapper;
import com.xc.blogbackend.model.domain.BlogArticleTag;
import com.xc.blogbackend.model.domain.BlogTag;
import com.xc.blogbackend.service.BlogArticleTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author XC
* @description 针对表【bg_article_tag】的数据库操作Service实现
* @createDate 2023-11-16 16:08:15
*/
@Service
@Slf4j
public class BlogArticleTagServiceImpl extends ServiceImpl<BlogArticleTagMapper, BlogArticleTag>
    implements BlogArticleTagService{

    @Resource
    private BlogArticleTagMapper blogArticleTagMapper;

    @Resource
    private BlogTagMapper blogTagMapper;

    @Override
    public List<Integer> getArticleIdListByTagId(int tag_id) {
        QueryWrapper<BlogArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("article_id");
        queryWrapper.eq("tag_id",tag_id);
        List<BlogArticleTag> articleTags = blogArticleTagMapper.selectList(queryWrapper);

        // 创建一个 Set 用于存储唯一的文章 ID
        Set<Integer> articleIdList = new HashSet<>();

        // 遍历结果列表，将文章 ID 添加到集合中
        articleTags.forEach(v -> {
            // 检查集合中是否已存在该文章 ID，若不存在则添加
            if (!articleIdList.contains(v.getArticle_id())) {
                articleIdList.add(v.getArticle_id());
            }
        });

        // 如果文章 ID 集合非空，将其转换为 List 后返回；否则返回空
        return articleIdList.isEmpty() ? null : articleIdList.stream().collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getTagListByArticleId(Integer article_id) {
        // 查询关联的标签 ID 列表
        QueryWrapper<BlogArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",article_id);
        queryWrapper.select("tag_id");
        List<BlogArticleTag> articleTags = blogArticleTagMapper.selectList(queryWrapper);

        List<Integer> tagIdList = articleTags.stream().map(BlogArticleTag::getTag_id).collect(Collectors.toList());

        // 根据标签 ID 列表获取标签信息
        List<BlogTag> tagList = blogTagMapper.selectBatchIds(tagIdList);

        // 提取标签名列表
        List<String> tagNameList = tagList.stream().map(BlogTag::getTag_name).collect(Collectors.toList());

        // 构建返回对象
        Map<String, Object> result = new HashMap<>();
        result.put("tagList", tagList);
        result.put("tagIdList", tagIdList);
        result.put("tagNameList", tagNameList);

        return result;
    }

    @Override
    public List<BlogArticleTag> createArticleTags(List<BlogArticleTag> articleTagList) {
        ArrayList<BlogArticleTag> articleTag = new ArrayList<>();
        //todo  在数据库xml中编写批量插入语句
        for (BlogArticleTag blogArticleTag : articleTagList){
            blogArticleTagMapper.insert(blogArticleTag);
            articleTag.add(blogArticleTag);
        }

        return articleTag != null ? articleTag : null;
    }

    @Override
    public Integer deleteArticleTag(Integer article_id) {
        QueryWrapper<BlogArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",article_id);
        int deleteById = blogArticleTagMapper.delete(queryWrapper);
        return deleteById;
    }
}




