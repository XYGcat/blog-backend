package com.xc.blogbackend.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class IPAddressDomain implements Serializable {
    //@ApiModelProperty("ip地址")
    private String ip;

    //@ApiModelProperty("国家")
    private String country;

    //@ApiModelProperty("省")
    private String province;

    //@ApiModelProperty("城市")
    private String city;

    //@ApiModelProperty("服务商")
    private String isp;

}
