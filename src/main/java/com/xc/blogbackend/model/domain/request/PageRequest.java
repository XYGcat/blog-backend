package com.xc.blogbackend.model.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据请求体
 *
 * @author 星尘
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -3228210362535411628L;

    private Integer current;    // 分页页码
    private Integer size;       // 分页大小
    private String message;     // 消息
    private List<String> time = new ArrayList<>();
    private String site_name;   // 网站名称
    private Integer status;      // 状态
    private String tag;          // 标签
    private Integer user_id;     // 用户id
}
