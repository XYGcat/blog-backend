package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogLinksMapper;
import com.xc.blogbackend.model.domain.BlogLinks;
import com.xc.blogbackend.model.domain.request.PageRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogLinksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    private final QueryWrapper<BlogLinks> queryWrapper = new QueryWrapper<>();

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

        queryWrapper.clear();
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

        PageInfoResult<BlogLinks> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }
}




