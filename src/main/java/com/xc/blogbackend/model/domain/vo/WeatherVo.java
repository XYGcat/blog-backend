package com.xc.blogbackend.model.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 天气信息
 */
@Data
public class WeatherVo {

    /**
     * 地点
     */
    private String address;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 温度
     */
    private String temp;

    /**
     * 天气状况
     */
    private String weather;

    /**
     * 风向
     */
    private String windDirection;

    /**
     * 风力
     */
    private String windPower;

    /**
     * 湿度
     */
    private String humidity;

    /**
     * 发布时间
     */
    private String reportTime;

    /**
     * 天气预报列表
     */
    private List<Forecast> forecasts;

    @Data
    public static class Forecast {
        /**
         * 日期
         */
        private String date;                 // 日期，如 "2018-11-27"

        /**
         * 星期几
         */
        private String dayOfWeek;           // 星期几，如 "2"

        /**
         * 白天天气
         */
        private String dayWeather;          // 白天天气，如 "阵雨"

        /**
         * 夜间天气
         */
        private String nightWeather;        // 夜间天气，如 "小雨"

        /**
         * 白天温度
         */
        private String dayTemp;             // 白天温度，如 "22℃"

        /**
         * 夜间温度
         */
        private String nightTemp;           // 夜间温度，如 "17℃"

        /**
         * 白天风向
         */
        private String dayWindDirection;    // 白天风向，如 "无风向"

        /**
         * 夜间风向
         */
        private String nightWindDirection;  // 夜间风向，如 "无风向"

        /**
         * 白天风力
         */
        private String dayWindPower;        // 白天风力，如 "≤3级"

        /**
         * 夜间风力
         */
        private String nightWindPower;      // 夜间风力，如 "≤3级"
    }
}
