package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogNotifyMapper;
import com.xc.blogbackend.model.domain.BlogNotify;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author XC
* @description 针对表【bg_notify】的数据库操作Service实现
* @createDate 2023-12-01 17:26:20
*/
@Service
@Slf4j
public class BlogNotifyServiceImpl extends ServiceImpl<BlogNotifyMapper, BlogNotify>
    implements BlogNotifyService{

    @Resource
    private BlogNotifyMapper blogNotifyMapper;

    @Override
    public PageInfoResult<BlogNotify> getNotifyList(Integer current, Integer size, Integer user_id) {

        QueryWrapper<BlogNotify> queryWrapper = new QueryWrapper<>();    // 构建查询条件
        if (user_id != null) {
            queryWrapper.eq("user_id", user_id);
        }
        //按照 isView 升序和 createdAt 降序排列
        queryWrapper.orderByAsc("isView")
                    .orderByDesc("created_at");
        // 创建Page对象，设置当前页和分页大小
        Page<BlogNotify> page = new Page<>(current,size);
        // 获取通知列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogNotify> notifyPage = blogNotifyMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogNotify> rows = notifyPage.getRecords();
        // 获取通知总数
        long count = notifyPage.getTotal();

        PageInfoResult<BlogNotify> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setCurrent(current);
        pageInfoResult.setList(rows);
        pageInfoResult.setTotal(count);
        pageInfoResult.setSize(size);

        return pageInfoResult;
    }

    @Override
    public BlogNotify addNotify(Integer user_id, Integer type, Integer to_id, String message) {
        BlogNotify blogNotify = new BlogNotify();
        if (user_id != null) {
            blogNotify.setUser_id(user_id);
        }
        if (type != null) {
            blogNotify.setType(type);
        }
        if (to_id != null) {
            blogNotify.setTo_id(to_id);
        }
        if (message != null) {
            blogNotify.setMessage(message);
        }
        int insert = blogNotifyMapper.insert(blogNotify);

        return blogNotify;
    }

    @Override
    public Boolean updateNotify(Integer id) {
        BlogNotify blogNotify = new BlogNotify();
        blogNotify.setIsView(2);
        UpdateWrapper<BlogNotify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        int update = blogNotifyMapper.update(blogNotify, updateWrapper);
        return update > 0;
    }

    @Override
    public Boolean deleteNotifys(Integer id) {
        int deleteById = blogNotifyMapper.deleteById(id);
        return deleteById > 0;
    }
}




