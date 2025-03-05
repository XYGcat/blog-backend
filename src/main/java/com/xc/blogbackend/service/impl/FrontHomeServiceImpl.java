package com.xc.blogbackend.service.impl;

import com.xc.blogbackend.common.RollApiResponse;
import com.xc.blogbackend.constant.RollApi;
import com.xc.blogbackend.model.domain.vo.DailySentenceVo;
import com.xc.blogbackend.model.domain.vo.ImageVo;
import com.xc.blogbackend.model.domain.vo.WeatherVo;
import com.xc.blogbackend.service.FrontHomeService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FrontHomeServiceImpl implements FrontHomeService {
    @Override
    public DailySentenceVo getDailySentence() {
        WebClient webClient = WebClient.create();

        String url = MessageFormat.format(RollApi.ROLL_API_DAILY_WORD, RollApi.APP_ID, RollApi.APP_SECRET);

        RollApiResponse<List<Map<String,String>>> response = webClient
                .get()
                .uri(url)
                .retrieve()                                 // 提取响应
                .bodyToMono(new ParameterizedTypeReference<RollApiResponse<List<Map<String, String>>>>() {})
                .block();

        DailySentenceVo dailySentenceVo = new DailySentenceVo();

        if (response != null && response.getCode() == 1) {
            String text = response.getData().get(0).get("content");
            dailySentenceVo.setText(text);
            return dailySentenceVo;
        }
        return null;
    }

    @Override
    public WeatherVo getWeatherCurrent(String city) {
        WebClient webClient = WebClient.create();

        String url = MessageFormat.format(RollApi.ROLL_API_WEATHER_CURRENT,city, RollApi.APP_ID, RollApi.APP_SECRET);

        RollApiResponse<WeatherVo> response = webClient
                .get()
                .uri(url)
                .retrieve()                                 // 提取响应
                .bodyToMono(new ParameterizedTypeReference<RollApiResponse<WeatherVo>>() {})
                .block();                                   // 阻塞等待结果

        if (response != null && response.getCode() == 1) {
            return response.getData();
        }else {
            return null;
        }
    }

    @Override
    public WeatherVo getWeatherForecast(String city) {
        WebClient webClient = WebClient.create();

        String url = MessageFormat.format(RollApi.ROLL_API_WEATHER_FORECAST,city, RollApi.APP_ID, RollApi.APP_SECRET);

        RollApiResponse<WeatherVo> response = webClient
                .get()
                .uri(url)
                .retrieve()                                 // 提取响应
                .bodyToMono(new ParameterizedTypeReference<RollApiResponse<WeatherVo>>() {})
                .block();                                   // 阻塞等待结果

        if (response != null && response.getCode() == 1) {
            return response.getData();
        }else {
            return null;
        }
    }

    @Override
    public String getGirlImg() {
        WebClient webClient = WebClient.create();

        String url = MessageFormat.format(RollApi.ROLL_API_GIRL_IMG, RollApi.APP_ID, RollApi.APP_SECRET);

        RollApiResponse<List<Map<String,String>>> response = webClient
                .get()
                .uri(url)
                .retrieve()                                 // 提取响应
                .bodyToMono(new ParameterizedTypeReference<RollApiResponse<List<Map<String,String>>>>() {})
                .block();                                   // 阻塞等待结果

        if (response != null && response.getCode() == 1) {
            return response.getData().get(0).get("imageUrl");
        }else {
            return null;
        }
    }

    @Override
    public List<ImageVo> getGirlImgList(int page) {
        WebClient webClient = WebClient.create();

        String url = MessageFormat.format(RollApi.ROLL_API_GIRL_IMG_LIST,page, RollApi.APP_ID, RollApi.APP_SECRET);

        RollApiResponse<List<ImageVo>> response = webClient
                .get()
                .uri(url)
                .retrieve()                                 // 提取响应
                .bodyToMono(new ParameterizedTypeReference<RollApiResponse<List<ImageVo>>>() {})
                .block();                                   // 阻塞等待结果

        if (response != null && response.getCode() == 1) {
            return response.getData();
        }else {
            return Collections.emptyList();
        }
    }
}
