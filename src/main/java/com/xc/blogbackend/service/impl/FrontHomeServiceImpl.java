package com.xc.blogbackend.service.impl;

import com.xc.blogbackend.client.WebClientService;
import com.xc.blogbackend.common.RollApiResponse;
import com.xc.blogbackend.constant.UtilsApi;
import com.xc.blogbackend.model.domain.vo.DailySentenceVo;
import com.xc.blogbackend.model.domain.vo.ImageVo;
import com.xc.blogbackend.model.domain.vo.WeatherVo;
import com.xc.blogbackend.service.FrontHomeService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FrontHomeServiceImpl implements FrontHomeService {

    private final WebClientService webClientService;

    public FrontHomeServiceImpl(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    @Override
    public DailySentenceVo getDailySentence() {

        String url = UtilsApi.YY_API_DAILY_WORD;

        Map<String, String> response = webClientService.get(
                url,
                new ParameterizedTypeReference<Map<String, String>>() {});

        if (response != null) {
            DailySentenceVo dailySentenceVo = new DailySentenceVo();
            dailySentenceVo.setHitokoto(response.get("hitokoto"));
            dailySentenceVo.setFrom(response.get("from"));
            dailySentenceVo.setFromWho(response.get("from_who"));
            return dailySentenceVo;
        }
        return null;
    }

//    @Override
//    public DailySentenceVo getDailySentence() {
//
//        String url = MessageFormat.format(
//                UtilsApi.ROLL_API_DAILY_WORD,
//                UtilsApi.ROLL_APP_ID,
//                UtilsApi.ROLL_APP_SECRET);
//
//        RollApiResponse<List<Map<String,String>>> response = webClientService.get(
//                url,
//                new ParameterizedTypeReference<RollApiResponse<List<Map<String, String>>>>() {});
//
//        if (response != null && response.getCode() == 1) {
//            DailySentenceVo dailySentenceVo = new DailySentenceVo();
//            dailySentenceVo.setText(response.getData().get(0).get("content"));
//            return dailySentenceVo;
//        }
//        return null;
//    }

    @Override
    public WeatherVo getWeatherCurrent(String city) {
        String url = MessageFormat.format(
                UtilsApi.ROLL_API_WEATHER_CURRENT,
                city,
                UtilsApi.ROLL_APP_ID,
                UtilsApi.ROLL_APP_SECRET
        );

        RollApiResponse<WeatherVo> response = webClientService.get(
                url,
                new ParameterizedTypeReference<RollApiResponse<WeatherVo>>() {});

        return response != null && response.getCode() == 1 ? response.getData() : null;
    }

    @Override
    public WeatherVo getWeatherForecast(String city) {
        String url = MessageFormat.format(
                UtilsApi.ROLL_API_WEATHER_FORECAST,
                city,
                UtilsApi.ROLL_APP_ID,
                UtilsApi.ROLL_APP_SECRET
        );

        RollApiResponse<WeatherVo> response = webClientService.get(
                url,
                new ParameterizedTypeReference<RollApiResponse<WeatherVo>>() {}
        );

        return response != null && response.getCode() == 1 ? response.getData() : null;
    }

    @Override
    public ImageVo getGirlImg() {
        String url = UtilsApi.LZ_API_GIRL_IMG_LIST_URL;

        RollApiResponse<String> response = webClientService.get(
                url,
                new ParameterizedTypeReference<RollApiResponse<String>>() {}
        );
        if (ObjectUtils.isNotEmpty(response) && response.getCode() == 200) {
            ImageVo imageVo = new ImageVo();
            imageVo.setImageUrl(response.getData());
            return imageVo;
        }
        return null;
    }

    @Override
    public List<ImageVo> getGirlImgList(int page) {
        String url = MessageFormat.format(
                UtilsApi.ROLL_API_GIRL_IMG_LIST,
                page,
                UtilsApi.ROLL_APP_ID,
                UtilsApi.ROLL_APP_SECRET
        );

        RollApiResponse<List<ImageVo>> response = webClientService.get(
                url,
                new ParameterizedTypeReference<RollApiResponse<List<ImageVo>>>() {}
        );

        return response != null && response.getCode() == 1 ? response.getData() : Collections.emptyList();
    }
}
