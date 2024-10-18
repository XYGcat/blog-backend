package com.xc.blogbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName blog_comment
 */
@TableName(value ="blog_comment")
@Data
public class BlogComment implements Serializable {

    /**
     * 作者id
     */
    @TableField(exist = false)
    private Integer author_id;

    /**
     * 是否点赞
     */
    @TableField(exist = false)
    private Boolean is_like;

    /**
     *地理位置
     */
    @TableField(exist = false)
    private String ipAddress;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论父级id
     */
    @TableField(value = "parent_id")
    private Integer parent_id;

    /**
     * 评论的对象id 比如说说id、文章id等
     */
    @TableField(value = "for_id")
    private Integer for_id;

    /**
     * 评论类型 1 文章 2 说说 3 留言 ...
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 评论人id
     */
    @TableField(value = "from_id")
    private Integer from_id;

    /**
     * 评论人昵称
     */
    @TableField(value = "from_name")
    private String from_name;

    /**
     * 评论人头像
     */
    @TableField(value = "from_avatar")
    private String from_avatar;

    /**
     * 被回复的人id
     */
    @TableField(value = "to_id")
    private Integer to_id;

    /**
     * 被回复人的昵称
     */
    @TableField(value = "to_name")
    private String to_name;

    /**
     * 被回复人的头像
     */
    @TableField(value = "to_avatar")
    private String to_avatar;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 评论点赞数
     */
    @TableField(value = "thumbs_up")
    private Integer thumbs_up;

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
     * ip地址
     */
    @TableField(value = "ip")
    private String ip;

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
        BlogComment other = (BlogComment) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getParent_id() == null ? other.getParent_id() == null : this.getParent_id().equals(other.getParent_id()))
            && (this.getFor_id() == null ? other.getFor_id() == null : this.getFor_id().equals(other.getFor_id()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getFrom_id() == null ? other.getFrom_id() == null : this.getFrom_id().equals(other.getFrom_id()))
            && (this.getFrom_name() == null ? other.getFrom_name() == null : this.getFrom_name().equals(other.getFrom_name()))
            && (this.getFrom_avatar() == null ? other.getFrom_avatar() == null : this.getFrom_avatar().equals(other.getFrom_avatar()))
            && (this.getTo_id() == null ? other.getTo_id() == null : this.getTo_id().equals(other.getTo_id()))
            && (this.getTo_name() == null ? other.getTo_name() == null : this.getTo_name().equals(other.getTo_name()))
            && (this.getTo_avatar() == null ? other.getTo_avatar() == null : this.getTo_avatar().equals(other.getTo_avatar()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getThumbs_up() == null ? other.getThumbs_up() == null : this.getThumbs_up().equals(other.getThumbs_up()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParent_id() == null) ? 0 : getParent_id().hashCode());
        result = prime * result + ((getFor_id() == null) ? 0 : getFor_id().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getFrom_id() == null) ? 0 : getFrom_id().hashCode());
        result = prime * result + ((getFrom_name() == null) ? 0 : getFrom_name().hashCode());
        result = prime * result + ((getFrom_avatar() == null) ? 0 : getFrom_avatar().hashCode());
        result = prime * result + ((getTo_id() == null) ? 0 : getTo_id().hashCode());
        result = prime * result + ((getTo_name() == null) ? 0 : getTo_name().hashCode());
        result = prime * result + ((getTo_avatar() == null) ? 0 : getTo_avatar().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getThumbs_up() == null) ? 0 : getThumbs_up().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", parent_id=").append(parent_id);
        sb.append(", for_id=").append(for_id);
        sb.append(", type=").append(type);
        sb.append(", from_id=").append(from_id);
        sb.append(", from_name=").append(from_name);
        sb.append(", from_avatar=").append(from_avatar);
        sb.append(", to_id=").append(to_id);
        sb.append(", to_name=").append(to_name);
        sb.append(", to_avatar=").append(to_avatar);
        sb.append(", content=").append(content);
        sb.append(", thumbs_up=").append(thumbs_up);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", ip=").append(ip);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}