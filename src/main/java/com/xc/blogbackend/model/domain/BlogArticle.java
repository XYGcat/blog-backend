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
 * 
 * @TableName blog_article
 */
@TableName(value ="blog_article")
@Data
public class BlogArticle implements Serializable {

    @TableField(exist = false)
    private String categoryName;    // 文章所属分类名

    @TableField(exist = false)
    private Map<String, Object> tagList; // 文章标签列表

    @TableField(exist = false)
    private List<String> tagNameList;   // 标签名列表

    public void setTagNameList(List<String> tagNameList){
        this.tagNameList = tagNameList;
    }

    /**
     * 
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

    /**
     * 
     */
    @TableField(value = "createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * 
     */
    @TableField(value = "updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BlogArticle other = (BlogArticle) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getArticle_title() == null ? other.getArticle_title() == null : this.getArticle_title().equals(other.getArticle_title()))
            && (this.getAuthor_id() == null ? other.getAuthor_id() == null : this.getAuthor_id().equals(other.getAuthor_id()))
            && (this.getCategory_id() == null ? other.getCategory_id() == null : this.getCategory_id().equals(other.getCategory_id()))
            && (this.getArticle_content() == null ? other.getArticle_content() == null : this.getArticle_content().equals(other.getArticle_content()))
            && (this.getArticle_cover() == null ? other.getArticle_cover() == null : this.getArticle_cover().equals(other.getArticle_cover()))
            && (this.getIs_top() == null ? other.getIs_top() == null : this.getIs_top().equals(other.getIs_top()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getOrigin_url() == null ? other.getOrigin_url() == null : this.getOrigin_url().equals(other.getOrigin_url()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getView_times() == null ? other.getView_times() == null : this.getView_times().equals(other.getView_times()))
            && (this.getArticle_description() == null ? other.getArticle_description() == null : this.getArticle_description().equals(other.getArticle_description()))
            && (this.getThumbs_up_times() == null ? other.getThumbs_up_times() == null : this.getThumbs_up_times().equals(other.getThumbs_up_times()))
            && (this.getReading_duration() == null ? other.getReading_duration() == null : this.getReading_duration().equals(other.getReading_duration()))
            && (this.getArticle_order() == null ? other.getArticle_order() == null : this.getArticle_order().equals(other.getArticle_order()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getArticle_title() == null) ? 0 : getArticle_title().hashCode());
        result = prime * result + ((getAuthor_id() == null) ? 0 : getAuthor_id().hashCode());
        result = prime * result + ((getCategory_id() == null) ? 0 : getCategory_id().hashCode());
        result = prime * result + ((getArticle_content() == null) ? 0 : getArticle_content().hashCode());
        result = prime * result + ((getArticle_cover() == null) ? 0 : getArticle_cover().hashCode());
        result = prime * result + ((getIs_top() == null) ? 0 : getIs_top().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getOrigin_url() == null) ? 0 : getOrigin_url().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getView_times() == null) ? 0 : getView_times().hashCode());
        result = prime * result + ((getArticle_description() == null) ? 0 : getArticle_description().hashCode());
        result = prime * result + ((getThumbs_up_times() == null) ? 0 : getThumbs_up_times().hashCode());
        result = prime * result + ((getReading_duration() == null) ? 0 : getReading_duration().hashCode());
        result = prime * result + ((getArticle_order() == null) ? 0 : getArticle_order().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", article_title=").append(article_title);
        sb.append(", author_id=").append(author_id);
        sb.append(", category_id=").append(category_id);
        sb.append(", article_content=").append(article_content);
        sb.append(", article_cover=").append(article_cover);
        sb.append(", is_top=").append(is_top);
        sb.append(", status=").append(status);
        sb.append(", type=").append(type);
        sb.append(", origin_url=").append(origin_url);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", view_times=").append(view_times);
        sb.append(", article_description=").append(article_description);
        sb.append(", thumbs_up_times=").append(thumbs_up_times);
        sb.append(", reading_duration=").append(reading_duration);
        sb.append(", article_order=").append(article_order);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

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
            String article_description
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
    }
}