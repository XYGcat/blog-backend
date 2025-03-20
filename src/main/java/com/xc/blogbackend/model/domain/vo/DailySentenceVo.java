package com.xc.blogbackend.model.domain.vo;

import lombok.Data;

/**
 * 每日一句 Vo
 *
 * @author xc
 */
@Data
public class DailySentenceVo {

    /**
     * 句子
     */
    private String hitokoto;

    /**
     * 出处
     */
    private String from;

    /**
     * 作者
     */
    private String fromWho;

    /**
     * 标签
     */
    private String tag;

    /**
     * 日期
     */
    private String date;
}
