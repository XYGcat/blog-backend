package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.xc.blogbackend.mapper.BlogLinksMapper;
import com.xc.blogbackend.model.domain.BlogLinks;
import com.xc.blogbackend.model.domain.request.PageRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogLinksService;
import com.xc.blogbackend.utils.Qiniu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author XC
* @description 针对表【blog_links】的数据库操作Service实现
* @createDate 2023-11-23 20:12:24
*/
@Service
@Slf4j
public class BlogLinksServiceImpl extends ServiceImpl<BlogLinksMapper, BlogLinks>
    implements BlogLinksService{

    @Resource
    private BlogLinksMapper blogLinksMapper;

    @Resource
    private Qiniu qiniu;

    @Override
    public PageInfoResult<BlogLinks> getLinksList(PageRequest pageRequest) {
        Integer current = pageRequest.getCurrent();
        Integer size = pageRequest.getSize();
        String site_name = pageRequest.getSite_name();
        Integer status = pageRequest.getStatus();
        List<String> time = pageRequest.getTime();

        // 分页参数处理
        int offset = (current - 1) * size;
        int limit = size;

        QueryWrapper<BlogLinks> queryWrapper = new QueryWrapper<>();
        if (site_name != null && !site_name.isEmpty()) {
            queryWrapper.like("site_name", "%" + site_name + "%");
        }
        if (status != null) {
            queryWrapper.eq("status",status);
        }
        if (time != null && time.size() == 2 && time.get(0) != null && time.get(1) != null) {
            queryWrapper.between("createdAt", time.get(0), time.get(1));
        }
        queryWrapper.orderByAsc("createdAt");
        // 创建Page对象，设置当前页和分页大小
        Page<BlogLinks> page = new Page<>(offset, limit);
        // 获取说说列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogLinks> messagePage = blogLinksMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogLinks> rows = messagePage.getRecords();
        // 获取说说总数
        long count = messagePage.getTotal();

        for (BlogLinks v : rows){
            try {
                String downloadUrl = qiniu.downloadUrl(v.getSite_avatar());
                v.setSite_avatar(downloadUrl);
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        PageInfoResult<BlogLinks> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }

    @Override
    public Boolean addOrUpdateLinks(Map<String, Object> request) {
        Integer id = (Integer) request.get("id");
        String site_name = (String) request.get("site_name");
        String site_desc = (String) request.get("site_desc");
        String site_avatar = (String) request.get("site_avatar");
        String url = (String) request.get("url");
        int res;

        if (id != null) {
            BlogLinks blogLinks = new BlogLinks();
            blogLinks.setSite_name(site_name);
            blogLinks.setSite_desc(site_desc);
            blogLinks.setSite_avatar(site_avatar);
            blogLinks.setUrl(url);
            UpdateWrapper<BlogLinks> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            res = blogLinksMapper.update(blogLinks, updateWrapper);
        }else {
            BlogLinks blogLinks = new BlogLinks();
            blogLinks.setSite_name(site_name);
            blogLinks.setSite_desc(site_desc);
            blogLinks.setSite_avatar(site_avatar);
            blogLinks.setUrl(url);
            blogLinks.setStatus(1);
            res = blogLinksMapper.insert(blogLinks);
        }

        return res > 0;
    }
}




