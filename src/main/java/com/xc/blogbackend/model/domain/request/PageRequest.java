package com.xc.blogbackend.model.domain.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.xc.blogbackend.utils.CustomTimeDeserializer;
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
    @JsonDeserialize(using = CustomTimeDeserializer.class)
    private List<String> time = new ArrayList<>();
}
