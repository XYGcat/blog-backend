package com.xc.blogbackend.constant;

/**
 * Roll Api接口
 */
public class UtilsApi {

    public static final String ROLL_APP_ID = "rvxhyvouifjmwjof";

    public static final String ROLL_APP_SECRET = "D2MDYZhkqXOzOeZxHKOZdzKpKKsYNI2Y";

    /**
     * 天气接口
     *
     * https://www.mxnzp.com/doc/detail?id=7
     */
    public static final String ROLL_API_WEATHER_CURRENT = "https://www.mxnzp.com/api/weather/current/{0}?app_id={1}&app_secret={2}";

    public static final String ROLL_API_WEATHER_FORECAST = "https://www.mxnzp.com/api/weather/forecast/{0}?app_id={1}&app_secret={2}";


    /**
     * 随机获取福利图片(横屏）
     */
    public static final String ROLL_API_GIRL_IMG = "https://www.mxnzp.com/api/image/girl/list/random?app_id={0}&app_secret={1}";

    /**
     * 获取福利图片列表(竖屏）
     *
     * https://xxapi.cn/doc/wapmeinvpic
     */
    public static final String LZ_API_GIRL_IMG_LIST_URL = "https://v2.xxapi.cn/api/wapmeinvpic?return=josn | 302";

    /**
     * 获取福利图片列表(横屏）
     * page: 当前页数
     */
    public static final String ROLL_API_GIRL_IMG_LIST = "https://www.mxnzp.com/api/image/girl/list?page={0}&app_id={1}&app_secret={2}";

    /**
     * 每日一句(roll)
     */
    public static final String ROLL_API_DAILY_WORD = "https://www.mxnzp.com/api/daily_word/recommend?app_id={0}&app_secret={1}";

    /**
     * 每日一句(一言)
     */
    public static final String YY_API_DAILY_WORD = "https://v1.hitokoto.cn?c=a&c=e&c=f&c=h&c=j";
}
