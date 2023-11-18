package com.xc.blogbackend.model.domain.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页请求数据返回体
 *
 * @author 星尘
 */
@Data
public class StatisticResult implements Serializable{
    private static final long serialVersionUID = -865599604642185427L;

    private long articleCount;
    private long tagCount;
    private long categoryCount;
    private long userCount;
}
