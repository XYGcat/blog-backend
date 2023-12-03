package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogConfig;

/**
* @author XC
* @description 针对表【blog_config】的数据库操作Service
* @createDate 2023-11-23 14:04:12
*/
public interface BlogConfigService extends IService<BlogConfig> {

    /**
     * 获取网站设置
     *
     * @return
     */
    BlogConfig getConfig();

    /**
     * 增加网站访问次数
     *
     * @return
     */
    String addView();
}
