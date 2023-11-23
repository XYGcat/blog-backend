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
 * @TableName blog_message
 */
@TableName(value ="blog_message")
@Data
public class BlogMessage implements Serializable {

    /**
     *评论总数
     */
    @TableField(exist = false)
    private Long comment_total;

    /**
     * 用户头像
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 留言内容
     */
    @TableField(value = "message")
    private String message;

    /**
     * 字体颜色
     */
    @TableField(value = "color")
    private String color;

    /**
     * 字体大小
     */
    @TableField(value = "font_size")
    private Integer font_size;

    /**
     * 背景颜色
     */
    @TableField(value = "bg_color")
    private String bg_color;

    /**
     * 背景图片
     */
    @TableField(value = "bg_url")
    private String bg_url;

    /**
     * 留言用户的id
     */
    @TableField(value = "user_id")
    private Integer user_id;

    /**
     * 点赞次数
     */
    @TableField(value = "like_times")
    private Integer like_times;

    /**
     * 创建时间
     */
    @TableField(value = "createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /**
     * 字体宽度
     */
    @TableField(value = "font_weight")
    private Integer font_weight;

    /**
     * 游客用户的昵称
     */
    @TableField(value = "nick_name")
    private String nick_name;

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
        BlogMessage other = (BlogMessage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTag() == null ? other.getTag() == null : this.getTag().equals(other.getTag()))
            && (this.getMessage() == null ? other.getMessage() == null : this.getMessage().equals(other.getMessage()))
            && (this.getColor() == null ? other.getColor() == null : this.getColor().equals(other.getColor()))
            && (this.getFont_size() == null ? other.getFont_size() == null : this.getFont_size().equals(other.getFont_size()))
            && (this.getBg_color() == null ? other.getBg_color() == null : this.getBg_color().equals(other.getBg_color()))
            && (this.getBg_url() == null ? other.getBg_url() == null : this.getBg_url().equals(other.getBg_url()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getLike_times() == null ? other.getLike_times() == null : this.getLike_times().equals(other.getLike_times()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getFont_weight() == null ? other.getFont_weight() == null : this.getFont_weight().equals(other.getFont_weight()))
            && (this.getNick_name() == null ? other.getNick_name() == null : this.getNick_name().equals(other.getNick_name()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTag() == null) ? 0 : getTag().hashCode());
        result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
        result = prime * result + ((getColor() == null) ? 0 : getColor().hashCode());
        result = prime * result + ((getFont_size() == null) ? 0 : getFont_size().hashCode());
        result = prime * result + ((getBg_color() == null) ? 0 : getBg_color().hashCode());
        result = prime * result + ((getBg_url() == null) ? 0 : getBg_url().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getLike_times() == null) ? 0 : getLike_times().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getFont_weight() == null) ? 0 : getFont_weight().hashCode());
        result = prime * result + ((getNick_name() == null) ? 0 : getNick_name().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tag=").append(tag);
        sb.append(", message=").append(message);
        sb.append(", color=").append(color);
        sb.append(", font_size=").append(font_size);
        sb.append(", bg_color=").append(bg_color);
        sb.append(", bg_url=").append(bg_url);
        sb.append(", user_id=").append(user_id);
        sb.append(", like_times=").append(like_times);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", font_weight=").append(font_weight);
        sb.append(", nick_name=").append(nick_name);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}