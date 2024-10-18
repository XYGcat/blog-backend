package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章表
 * 
 * @TableName bg_article
 */
@TableName(value ="bg_article")
@Data
public class BlogArticle implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *  文章id 不能为空
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章标题 不能为空
     */
    @TableField(value = "article_title")
    private String article_title;

    /**
     * 文章作者 不能为空
     */
    @TableField(value = "author_id")
    private Integer author_id;

    /**
     * 分类id 不能为空
     */
    @TableField(value = "category_id")
    private Integer category_id;

    /**
     * 文章内容
     */
    @TableField(value = "article_content")
    private String article_content;

    /**
     * 文章内容中的图片链接
     */
    @TableField(value = "mdImgList")
    private String mdImgList;

    /**
     * 文章缩略图
     */
    @TableField(value = "article_cover")
    private String article_cover;

    /**
     * 是否置顶 1 置顶 2 取消置顶
     */
    @TableField(value = "is_top")
    private Integer is_top;

    /**
     * 文章状态  1 公开 2 私密 3 草稿箱
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 文章类型 1 原创 2 转载 3 翻译
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 原文链接 是转载或翻译的情况下提供
     */
    @TableField(value = "origin_url")
    private String origin_url;

    @TableField(exist = false)
    private List<Integer> tagIdList;

    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String categoryName;    // 文章所属分类名

    @TableField(exist = false)
    private Map<String, Object> tagList; // 文章标签列表

    @TableField(exist = false)
    private List<String> tagNameList;   // 标签名列表

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updatedAt;

    /**
     * 文章访问次数
     */
    @TableField(value = "view_times")
    private Integer view_times;

    /**
     * 描述信息 不能为空
     */
    @TableField(value = "article_description")
    private String article_description;

    /**
     * 文章点赞次数
     */
    @TableField(value = "thumbs_up_times")
    private Integer thumbs_up_times;

    /**
     * 文章阅读时长
     */
    @TableField(value = "reading_duration")
    private Double reading_duration;

    /**
     * 排序 1 最大 往后越小 用于置顶文章的排序
     */
    @TableField(value = "article_order")
    private Integer article_order;

    /**
     * 自定义方法，一次性插入多个值
     *
     * @param article_title
     * @param author_id
     * @param article_content
     * @param article_cover
     * @param is_top
     * @param article_order
     * @param status
     * @param type
     * @param origin_url
     * @param article_description
     */
    public void setValues(
            String article_title,
            Integer author_id,
            String article_content,
            String article_cover,
            Integer is_top,
            Integer article_order,
            Integer status,
            Integer type,
            String origin_url,
            String article_description,
            String mdImgList
    ) {
        this.article_title = article_title;
        this.author_id = author_id;
        this.article_content = article_content;
        this.article_cover = article_cover;
        this.is_top = is_top;
        this.article_order = article_order;
        this.status = status;
        this.type = type;
        this.origin_url = origin_url;
        this.article_description = article_description;
        this.mdImgList = mdImgList;
    }
}