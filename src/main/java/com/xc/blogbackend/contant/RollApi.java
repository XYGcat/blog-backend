package com.xc.blogbackend.contant;

/**
 * Roll Api接口
 */
public class RollApi {

    public static final String APP_ID = "ppfxldiqgdejxunp";

    public static final String APP_SECRET = "MjdRMXkxbWJ6YXJPWWdBTE04Sk51QT09";

    /**
     * 天气接口
     *
     * https://www.mxnzp.com/doc/detail?id=7
     */
    public static final String ROLL_API_WEATHER_CURRENT = "https://www.mxnzp.com/api/weather/current/{0}?app_id={1}&app_secret={2}";

    public static final String ROLL_API_WEATHER_FORECAST = "https://www.mxnzp.com/api/weather/forecast/{0}?app_id={1}&app_secret={2}";


    /**
     * 随机获取福利图片
     */
    public static final String ROLL_API_GIRL_IMG = "https://www.mxnzp.com/api/image/girl/list/random?app_id={0}&app_secret={1}";

    public static final String ROLL_API_GIRL_IMG_LIST = "https://www.mxnzp.com/api/image/girl/list?page={0}&app_id={1}&app_secret={2}";


    /**
     * 每日一句
     */
    public static final String ROLL_API_DAILY_WORD = "https://www.mxnzp.com/api/daily_word/recommend?app_id={0}&app_secret={1}";
}
