package com.xc.blogbackend.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.xc.blogbackend.model.domain.IPAddressDomain;
import org.lionsoul.ip2region.xdb.Searcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * ip解析工具类
 *
 * @author 星尘
 */
public class IpUtils {

    private static final String ZERO="0";

    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        // 尝试从请求头中获取X-Real-IP，该请求头通常由一些代理服务器设置
        String ipAddress = request.getHeader("X-Real-IP");

        // 如果X-Real-IP不存在或为空或为"unknown"，尝试获取X-Forwarded-For请求头
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Forwarded-For");
        }

        // 如果X-Forwarded-For不存在或为空或为"unknown"，尝试获取Proxy-Client-IP请求头
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        // 如果Proxy-Client-IP不存在或为空或为"unknown"，尝试获取WL-Proxy-Client-IP请求头
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        // 如果WL-Proxy-Client-IP不存在或为空或为"unknown"，尝试获取RemoteAddr
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }
        if (ipAddress.split(",").length > 1) {
            ipAddress = ipAddress.split(",")[0];
        }

        return ipAddress;
    }

    /**
     * 方法一
     * 根据iP获取归属地信息 ip2region
     *
     * @param ip
     * @return
     */
    public static String getLocation(String ip) {
        IPAddressDomain location = new IPAddressDomain();
        location.setIp(ip);
        try (InputStream inputStream = IpUtils.class.getResourceAsStream("/ip2region.xdb");) {
            byte[] bytes = IoUtil.readBytes(inputStream);
            Searcher searcher = Searcher.newWithBuffer(bytes);
            String region = searcher.search(ip);
            if (StrUtil.isAllNotBlank(region)) {
                // xdb返回格式 国家|区域|省份|城市|ISP，
                // 只有中国的数据绝大部分精确到了城市，其他国家部分数据只能定位到国家，后前的选项全部是0
                String[] result = region.split("\\|");
                location.setCountry(ZERO.equals(result[0])?StrUtil.EMPTY:result[0]);
                location.setProvince(ZERO.equals(result[2])?StrUtil.EMPTY:result[2]);
                location.setCity(ZERO.equals(result[3])?StrUtil.EMPTY:result[3]);
                location.setIsp(ZERO.equals(result[4])?StrUtil.EMPTY:result[4]);
            }
            searcher.close();
        } catch (Exception e) {
            return location.getCity();
        }
        String modifiedString = null;
        // 如果字符串以“市”结尾，就去掉最后一个字符（"市"）
        if (location.getCity().endsWith("市")) {
            modifiedString = location.getCity().substring(0, location.getCity().length() - 1);
        } else {
            modifiedString = location.getCity(); // 输出结果为原始字符串，因为不以“市”结尾
        }
        return modifiedString;
    }

    /**
     * 方法二
     * 根据iP获取归属地信息 ip2region
     *
     * @param ip
     * @return
     * @throws IOException
     */
//    public static String getCityInfo(String ip) throws IOException {
//        // 1、创建 searcher 对象
//        String dbPath = "F:\\Code\\blog\\blog-backend\\src\\main\\resources\\ip2region.xdb";
//        Searcher searcher = null;
//        try {
//            searcher = Searcher.newWithFileOnly(dbPath);
//        } catch (IOException e) {
//            System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
//            return null;
//        }
//        // 2、查询
//        try {
//            long sTime = System.nanoTime();
//            String region = searcher.search(ip);
//            long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
//
//            System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
//        } catch (Exception e) {
//            System.out.printf("failed to search(%s): %s\n", ip, e);
//        }
//        // 3、关闭资源
//        searcher.close();
//        return region;
//    }
}
