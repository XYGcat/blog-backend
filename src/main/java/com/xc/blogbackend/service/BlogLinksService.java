package com.xc.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.blogbackend.model.domain.BlogLinks;
import com.xc.blogbackend.model.domain.request.PageRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;

import java.util.List;
import java.util.Map;

/**
* @author XC
* @description 针对表【bg_links】的数据库操作Service
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

    /**
     * 新增/编辑友链
     *
     * @param request
     * @return
     */
    Boolean addOrUpdateLinks(Map<String,Object> request);

    /**
     * 审核友链
     *
     * @param idList
     * @return
     */
    Boolean approveLinks(List<Integer> idList);

    /**
     * 批量删除友链
     *
     * @param idList
     * @return
     */
    Boolean deleteLinks(List<Integer> idList);
}
