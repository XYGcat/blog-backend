package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogMessageMapper;
import com.xc.blogbackend.model.domain.BlogMessage;
import com.xc.blogbackend.model.domain.BlogUser;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogCommentService;
import com.xc.blogbackend.service.BlogMessageService;
import com.xc.blogbackend.service.BlogUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
* @author XC
* @description 针对表【blog_message】的数据库操作Service实现
* @createDate 2023-11-23 16:35:28
*/
@Service
@Slf4j
public class BlogMessageServiceImpl extends ServiceImpl<BlogMessageMapper, BlogMessage>
    implements BlogMessageService{

    @Resource
    private BlogMessageMapper blogMessageMapper;

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private BlogCommentService blogCommentService;

    private final QueryWrapper<BlogMessage> queryWrapper = new QueryWrapper<>();

    @Override
    public PageInfoResult<BlogMessage> getMessageList(Integer current, Integer size, String message, List<String> time) {
        // 分页参数处理
        int offset = (current - 1) * size;
        int limit = size;

        queryWrapper.clear();
        if (message != null && !message.isEmpty()) {
            queryWrapper.like("message", "%" + message + "%");
        }
        if (time != null && time.size() == 2 && time.get(0) != null && time.get(1) != null) {
            queryWrapper.between("createdAt", time.get(0), time.get(1));
        }
        queryWrapper.orderByDesc("createdAt");
        // 创建Page对象，设置当前页和分页大小
        Page<BlogMessage> page = new Page<>(offset, limit);
        // 获取说说列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogMessage> messagePage = blogMessageMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogMessage> rows = messagePage.getRecords();
        // 获取说说总数
        long count = messagePage.getTotal();

        // 根据用户user_id获取用户当前的昵称和头像
        // 创建一个ExecutorService对象，根据你的需要选择合适的线程池大小
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 创建一个List<Future>对象，用于存储每个异步任务的返回值
        List<Future<BlogUser>> promiseList = new ArrayList<>();

        // 遍历rows数组中的每个元素   异步实现
        for(BlogMessage row : rows){
            // 创建一个Callable对象，定义异步任务的逻辑
            Callable<BlogUser> task = () -> {
                if (row.getUser_id() != null) {
                    // 调用其他方法或访问数据库，获取需要的数据
                    BlogUser oneUserInfo = blogUserService.getOneUserInfo(row.getUser_id());
                    return oneUserInfo;
                }else {
                    BlogUser oneUserInfo = new BlogUser();
                    oneUserInfo.setNick_name(row.getNick_name());
                    oneUserInfo.setAvatar("");
                    return oneUserInfo;
                }
            };
            // 将Callable对象提交给线程池执行，并将返回的Future对象添加到List对象中
            Future<BlogUser> future = executorService.submit(task);
            promiseList.add(future);
        }

        //同步实现
        int index = 0;
        for (Future<BlogUser> future : promiseList) {
            try {
                BlogUser v = future.get();
                if (v != null) {
                    rows.get(index).setNick_name(v.getNick_name());
                    rows.get(index).setAvatar(v.getAvatar());
                }
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
            index ++;
        }

        // 获取每一条的评论条数
        // 创建一个List<Future>对象，用于存储每个异步任务的返回值
        List<Future<Long>> promiseCommentList = new ArrayList<>();

        // 遍历rows数组中的每个元素   异步实现
        for(BlogMessage row : rows){
            // 创建一个Callable对象，定义异步任务的逻辑
            Callable<Long> task = () -> {
                // 调用其他方法或访问数据库，获取需要的数据
                Long commentTotal = blogCommentService.getCommentTotal(row.getId(), 3);
                return commentTotal;
            };
            // 将Callable对象提交给线程池执行，并将返回的Future对象添加到List对象中
            Future<Long> future = executorService.submit(task);
            promiseCommentList.add(future);
        }
        //同步实现
        int x = 0;
        for (Future<Long> future : promiseCommentList) {
            try {
                Long r = future.get();
                rows.get(index).setComment_total(r);
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
            x ++;
        }

        // 关闭线程池
        executorService.shutdown();

        //添加返回值
        PageInfoResult<BlogMessage> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }
}




