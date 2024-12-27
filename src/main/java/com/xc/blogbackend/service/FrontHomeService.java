package com.xc.blogbackend.service;

import com.xc.blogbackend.model.domain.vo.WeatherVo;

public interface FrontHomeService {

    /**
     * 获取每日一句
     * @return
     */
    String getDailyWord();

    /**
     * 获取今日天气信息
     * @param city
     * @return
     */
    WeatherVo getWeatherCurrent(String city);

    /**
     * 获取特定城市今天及未来天气信息
     *
     * @param city
     * @return
     */
    WeatherVo getWeatherForecast(String city);

    /**
     * 随机获取girl图片
     * @return
     */
    String getGirlImg();

    /**
     * 获取girl图片列表
     * @return
     */
    void getGirlImgList(int page);
}
