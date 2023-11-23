package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogLinks;
import com.xc.blogbackend.model.domain.request.PageRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

/**
* @author XC
* @description 针对表【blog_links】的数据库操作Service
* @createDate 2023-11-23 20:12:24
*/
public interface BlogLinksService extends IService<BlogLinks> {

    /**
     * 分页获取友链
     *
     * @param pageRequest
     * @return
     */
    PageInfoResult<BlogLinks> getLinksList(PageRequest pageRequest);

}
