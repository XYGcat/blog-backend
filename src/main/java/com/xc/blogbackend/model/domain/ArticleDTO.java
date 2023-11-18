package com.xc.blogbackend.model.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 用于传输文章相关信息的数据传输对象
 *
 */
@Data
public class ArticleDTO {

    private String categoryName; // 文章所属分类名
    private Map<String, Object> tagList; // 文章标签列表

    //private List<String> tagNameList; // 标签名列表

    public List<String> getTagNameList(){
        List<String> tagNameList = (List<String>) tagList.get("tagNameList");
        return tagNameList;
    }
}
