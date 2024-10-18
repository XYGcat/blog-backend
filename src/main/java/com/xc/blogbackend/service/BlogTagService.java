package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogTag;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

import java.util.List;

/**
* @author XC
* @description 针对表【bg_tag】的数据库操作Service
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

    /**
     * 获取标签总数
     *
     * @return
     */
    Long getTagCount();

    /**
     * 分页查找标签
     *
     * @param current
     * @param size
     * @param tag_name
     * @return
     */
    PageInfoResult<BlogTag> getTalkList(Integer current,Integer size,String tag_name);

    /**
     * 修改标签
     *
     * @param id
     * @param tag_name
     * @return
     */
    Boolean updateTag(Integer id,String tag_name);

    /**
     * 删除标签
     *
     * @param idList
     * @return
     */
    Boolean deleteTags(List<Integer> idList);
}
