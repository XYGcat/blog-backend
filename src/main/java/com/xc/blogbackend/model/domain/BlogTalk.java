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
 * @TableName blog_talk
 */
@TableName(value ="blog_talk")
@Data
public class BlogTalk implements Serializable {

    @TableField(exist = false)
    private String  avatar;

    @TableField(exist = false)
    private String nick_name;

    @TableField(exist = false)
    private List<String> talkImgListResponse;

    @TableField(exist = false)
    private List<Map<String,String>> talkImgList;   //添加说说时的图片数据列表请求体

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发布说说的用户id
     */
    @TableField(value = "user_id")
    private Integer user_id;

    /**
     * 说说内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 说说状态 1 公开 2 私密 3 回收站
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 是否置顶 1 置顶 2 不置顶
     */
    @TableField(value = "is_top")
    private Integer is_top;

    /**
     * 点赞次数
     */
    @TableField(value = "like_times")
    private Integer like_times;

    /**
     * 
     */
    @TableField(value = "createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")    //保证在返回 JSON 格式数据时，日期字段按照指定格式展示
    private Date createdAt;


    /**
     * 
     */
    @TableField(value = "updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

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
        BlogTalk other = (BlogTalk) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIs_top() == null ? other.getIs_top() == null : this.getIs_top().equals(other.getIs_top()))
            && (this.getLike_times() == null ? other.getLike_times() == null : this.getLike_times().equals(other.getLike_times()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIs_top() == null) ? 0 : getIs_top().hashCode());
        result = prime * result + ((getLike_times() == null) ? 0 : getLike_times().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", user_id=").append(user_id);
        sb.append(", content=").append(content);
        sb.append(", status=").append(status);
        sb.append(", is_top=").append(is_top);
        sb.append(", like_times=").append(like_times);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}