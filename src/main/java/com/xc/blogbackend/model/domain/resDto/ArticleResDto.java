package com.xc.blogbackend.model.domain.resDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ArticleResDto implements Serializable {

    private static final long serialVersionUID = 9198137538494223484L;

    /**
     *  文章id
     */
    private Long id;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章作者
     */
    private Long authorId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 文章内容中的图片链接
     */
    private String mdImgList;

    /**
     * 是否置顶 1 置顶 2 取消置顶
     */
    private Integer isTop;

    /**
     * 文章状态  1 公开 2 私密 3 草稿箱
     */
    private Integer status;

    /**
     * 文章类型 1 原创 2 转载 3 翻译
     */
    private Integer type;

    /**
     * 原文链接 是转载或翻译的情况下提供
     */
    private String originUrl;

    /**
     * 文章标签id列表
     */
    private List<Long> tagIdList;

    /**
     * 文章作者名
     */
    private String authorName;

    /**
     * 文章所属分类名
     */
    private String categoryName;

    /**
     * 文章标签列表
     */
    private Map<String, Object> tagList;

    /**
     * 文章标签名列表
     */
    private List<String> tagNameList;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime createdAt;

    /**
     * 文章访问次数
     */
    private Integer viewTimes;

    /**
     * 描述信息
     */
    private String articleDescription;

    /**
     * 文章点赞次数
     */
    private Integer thumbsUpTimes;

    /**
     * 文章阅读时长
     */
    private Double readingDuration;

    /**
     * 排序 1 最大 往后越小 用于置顶文章的排序
     */
    private Integer articleOrder;
}
