package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogTag;

import java.util.List;

/**
* @author XC
* @description 针对表【blog_tag】的数据库操作Service
* @createDate 2023-11-16 11:55:15
*/
public interface BlogTagService extends IService<BlogTag> {

    /**
     * 获取标签字典
     *
     * @return
     */
    List<BlogTag> getTagDictionary();

    /**
     * 根据(id或者)标签名称获取标签信息
     *
     * @param tag_name
     * @return
     */
    BlogTag getOneTag(String tag_name);

    /**
     * 新增标签
     *
     * @param tag_name
     * @return
     */
    BlogTag createTag(String tag_name);
}
