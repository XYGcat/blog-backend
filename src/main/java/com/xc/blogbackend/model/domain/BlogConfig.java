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
 * @TableName bg_config
 */
@TableName(value ="bg_config")
@Data
public class BlogConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 博客名称
     */
    @TableField(value = "bg_name")
    private String bg_name;

    /**
     * 博客头像
     */
    @TableField(value = "bg_avatar")
    private String bg_avatar;

    /**
     * 博客头像背景图
     */
    @TableField(value = "avatar_bg")
    private String avatar_bg;

    /**
     * 个人签名
     */
    @TableField(value = "personal_say")
    private String personal_say;

    /**
     * 博客公告
     */
    @TableField(value = "bg_notice")
    private String bg_notice;

    /**
     * qq链接
     */
    @TableField(value = "qq_link")
    private String qq_link;

    /**
     * 微信链接
     */
    @TableField(value = "we_chat_link")
    private String we_chat_link;

    /**
     * github链接
     */
    @TableField(value = "github_link")
    private String github_link;

    /**
     * git_ee链接
     */
    @TableField(value = "git_ee_link")
    private String git_ee_link;

    /**
     * bilibili链接
     */
    @TableField(value = "bilibili_link")
    private String bilibili_link;

    /**
     * 博客被访问的次数
     */
    @TableField(value = "view_time")
    private Long view_time;

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
        BlogConfig other = (BlogConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBg_name() == null ? other.getBg_name() == null : this.getBg_name().equals(other.getBg_name()))
            && (this.getBg_avatar() == null ? other.getBg_avatar() == null : this.getBg_avatar().equals(other.getBg_avatar()))
            && (this.getAvatar_bg() == null ? other.getAvatar_bg() == null : this.getAvatar_bg().equals(other.getAvatar_bg()))
            && (this.getPersonal_say() == null ? other.getPersonal_say() == null : this.getPersonal_say().equals(other.getPersonal_say()))
            && (this.getBg_notice() == null ? other.getBg_notice() == null : this.getBg_notice().equals(other.getBg_notice()))
            && (this.getQq_link() == null ? other.getQq_link() == null : this.getQq_link().equals(other.getQq_link()))
            && (this.getWe_chat_link() == null ? other.getWe_chat_link() == null : this.getWe_chat_link().equals(other.getWe_chat_link()))
            && (this.getGithub_link() == null ? other.getGithub_link() == null : this.getGithub_link().equals(other.getGithub_link()))
            && (this.getGit_ee_link() == null ? other.getGit_ee_link() == null : this.getGit_ee_link().equals(other.getGit_ee_link()))
            && (this.getBilibili_link() == null ? other.getBilibili_link() == null : this.getBilibili_link().equals(other.getBilibili_link()))
            && (this.getView_time() == null ? other.getView_time() == null : this.getView_time().equals(other.getView_time()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBg_name() == null) ? 0 : getBg_name().hashCode());
        result = prime * result + ((getBg_avatar() == null) ? 0 : getBg_avatar().hashCode());
        result = prime * result + ((getAvatar_bg() == null) ? 0 : getAvatar_bg().hashCode());
        result = prime * result + ((getPersonal_say() == null) ? 0 : getPersonal_say().hashCode());
        result = prime * result + ((getBg_notice() == null) ? 0 : getBg_notice().hashCode());
        result = prime * result + ((getQq_link() == null) ? 0 : getQq_link().hashCode());
        result = prime * result + ((getWe_chat_link() == null) ? 0 : getWe_chat_link().hashCode());
        result = prime * result + ((getGithub_link() == null) ? 0 : getGithub_link().hashCode());
        result = prime * result + ((getGit_ee_link() == null) ? 0 : getGit_ee_link().hashCode());
        result = prime * result + ((getBilibili_link() == null) ? 0 : getBilibili_link().hashCode());
        result = prime * result + ((getView_time() == null) ? 0 : getView_time().hashCode());
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
        sb.append(", bg_name=").append(bg_name);
        sb.append(", bg_avatar=").append(bg_avatar);
        sb.append(", avatar_bg=").append(avatar_bg);
        sb.append(", personal_say=").append(personal_say);
        sb.append(", bg_notice=").append(bg_notice);
        sb.append(", qq_link=").append(qq_link);
        sb.append(", we_chat_link=").append(we_chat_link);
        sb.append(", github_link=").append(github_link);
        sb.append(", git_ee_link=").append(git_ee_link);
        sb.append(", bilibili_link=").append(bilibili_link);
        sb.append(", view_time=").append(view_time);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}