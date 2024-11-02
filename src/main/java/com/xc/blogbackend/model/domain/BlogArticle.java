package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
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
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文章标题 不能为空
     */
    @TableField(value = "article_title")
    private String articleTitle;

    /**
     * 文章作者 不能为空
     */
    @TableField(value = "author_id")
    private Long authorId;

    /**
     * 分类id 不能为空
     */
    @TableField(value = "category_id")
    private Long categoryId;

    /**
     * 文章内容
     */
    @TableField(value = "article_content")
    private String articleContent;

    /**
     * 文章内容中的图片链接
     */
    @TableField(value = "md_img_list")
    private String mdImgList;

    /**
     * 文章缩略图
     */
    @TableField(value = "article_cover")
    private String articleCover;

    /**
     * 是否置顶 1 置顶 2 取消置顶
     */
    @TableField(value = "is_top")
    private Integer isTop;

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
    private String originUrl;

    @TableField(exist = false)
    private List<Long> tagIdList;

    @TableField(exist = false)
    private String authorName;

    @TableField(exist = false)
    private String categoryName;    // 文章所属分类名

    @TableField(exist = false)
    private Map<String, Object> tagList; // 文章标签列表

    @TableField(exist = false)
    private List<String> tagNameList;   // 标签名列表

    /**
     * 创建时间，插入时自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime createdAt;

    /**
     * 更新时间，插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime updatedAt;

    /**
     * 文章访问次数
     */
    @TableField(value = "view_times")
    private Integer viewTimes;

    /**
     * 描述信息 不能为空
     */
    @TableField(value = "article_description")
    private String articleDescription;

    /**
     * 文章点赞次数
     */
    @TableField(value = "thumbs_up_times")
    private Integer thumbsUpTimes;

    /**
     * 文章阅读时长
     */
    @TableField(value = "reading_duration")
    private Double readingDuration;

    /**
     * 排序 1 最大 往后越小 用于置顶文章的排序
     */
    @TableField(value = "article_order")
    private Integer articleOrder;

    /**
     * 自定义方法，一次性插入多个值
     *
     * @param articleTitle
     * @param authorId
     * @param articleContent
     * @param articleCover
     * @param isTop
     * @param articleOrder
     * @param status
     * @param type
     * @param originUrl
     * @param articleDescription
     */
    public void setValues(
            String articleTitle,
            Long authorId,
            String articleContent,
            String articleCover,
            Integer isTop,
            Integer articleOrder,
            Integer status,
            Integer type,
            String originUrl,
            String articleDescription,
            String mdImgList
    ) {
        this.articleTitle = articleTitle;
        this.authorId = authorId;
        this.articleContent = articleContent;
        this.articleCover = articleCover;
        this.isTop = isTop;
        this.articleOrder = articleOrder;
        this.status = status;
        this.type = type;
        this.originUrl = originUrl;
        this.articleDescription = articleDescription;
        this.mdImgList = mdImgList;
    }
}