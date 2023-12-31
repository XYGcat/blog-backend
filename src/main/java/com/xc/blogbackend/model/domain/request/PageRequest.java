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

    private Integer current;
    private Integer size;
    private String message;
    private List<String> time = new ArrayList<>();
    private String site_name;
    private Integer status;
    private String tag;
    private Integer user_id;
}
