package com.xc.blogbackend.model.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * ip地址信息
 */
@Data
public class IPAddressEntity implements Serializable {
    private static final long serialVersionUID = 345976253755963787L;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 服务商
     */
    private String isp;
}
