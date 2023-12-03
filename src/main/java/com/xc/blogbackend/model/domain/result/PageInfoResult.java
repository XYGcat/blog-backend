package com.xc.blogbackend.model.domain.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 用于封装分页查询返回的结果信息
 *
 * @param <T>
 */
@Data
public class PageInfoResult<T> implements Serializable {
    private static final long serialVersionUID = -8719757753354188168L;

    private long total; // 总记录数
    private long size; // 每页记录数
    private long current; // 当前页码
    private List<T> list; // 当前页的数据列表
    private List<Map<String, Object>> finalList; //前台时间轴返回值
}
