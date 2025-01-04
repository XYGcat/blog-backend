package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.vo.DailySentenceVo;
import com.xc.blogbackend.model.domain.vo.ImageVo;
import com.xc.blogbackend.model.domain.vo.WeatherVo;
import com.xc.blogbackend.service.FrontHomeService;
import com.xc.blogbackend.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

/**
 * 前台_首页 Controller
 */
@Api(tags = "前台_首页接口")
@RestController
@RequestMapping("/frontHome")
public class FrontHomeController {

    @Resource
    private FrontHomeService frontHomeService;

    /**
     * 获取每日一句
     * @return
     */
    @ApiOperation(value = "获取每日一句")
    @GetMapping("/getDailySentence")
    public BaseResponse<DailySentenceVo> getDailySentence() {
        DailySentenceVo dailySentence = frontHomeService.getDailySentence();
        return ResultUtils.success(dailySentence,"获取每日一句成功");
    }

    /**
     * 获取特定城市今日天气信息
     * @return
     */
    @ApiOperation(value = "获取今日天气信息")
    @GetMapping("/getWeatherCurrent")
    public BaseResponse<WeatherVo> getWeatherCurrent(HttpServletRequest request) {
        String city = IpUtils.getCityInfo(request);
        WeatherVo weatherCurrent = frontHomeService.getWeatherCurrent(city);
        return ResultUtils.success(weatherCurrent,"获取天气成功");
    }

    /**
     * 获取特定城市今天及未来天气信息
     * @return
     */
    @ApiOperation(value = "获取今天及未来天气信息")
    @GetMapping("/getWeatherForecast")
    public BaseResponse<WeatherVo> getWeatherForecast(HttpServletRequest request) {
        String city = IpUtils.getCityInfo(request);
        WeatherVo weatherForecast = frontHomeService.getWeatherForecast(city);
        return ResultUtils.success(weatherForecast,"获取天气成功");
    }

    /**
     * 获取随机美女图
     * @return
     */
    @ApiOperation(value = "获取随机美女图")
    @GetMapping("/getGirlImg")
    public BaseResponse<String> getGirl() {
        String girl = frontHomeService.getGirlImg();
        return ResultUtils.success(girl,"获取图片成功");
    }

    @ApiOperation(value = "获取美女图列表")
    @GetMapping("/getGirlImgList")
    public BaseResponse<List<ImageVo>> getGirlList(@RequestParam(required = false) Integer page) {
        // 随机数
        int nextInt = new Random().nextInt(50);
        List<ImageVo> girlImgList = frontHomeService.getGirlImgList(nextInt + 1);
        return ResultUtils.success(girlImgList,"获取图片列表成功");
    }
}
