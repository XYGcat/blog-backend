package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.xc.blogbackend.mapper.BlogMessageMapper;
import com.xc.blogbackend.model.domain.BlogMessage;
import com.xc.blogbackend.model.domain.BlogUser;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogCommentService;
import com.xc.blogbackend.service.BlogLikeService;
import com.xc.blogbackend.service.BlogMessageService;
import com.xc.blogbackend.service.BlogUserService;
import com.xc.blogbackend.utils.Qiniu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    @Resource
    private BlogLikeService blogLikeService;

    @Resource
    private Qiniu qiniu;

    @Override
    public PageInfoResult<BlogMessage> getMessageList
            (Integer current, Integer size, String message, List<String> time,String tag,Integer user_id) {
        // 分页参数处理
        int offset = (current - 1) * size;
        int limit = size;

        QueryWrapper<BlogMessage> queryWrapper = new QueryWrapper<>();
        if (tag != null && !tag.isEmpty()) {
            queryWrapper.eq("tag", tag);
        }
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

        // 异步根据用户user_id获取用户当前的昵称和头像
        List<CompletableFuture<BlogUser>> promiseList = new ArrayList<>();
        // 遍历每行数据
        for (BlogMessage row : rows) {
            if (row.getUser_id() != null) {
                // 如果用户ID存在，创建异步任务获取用户信息
                CompletableFuture<BlogUser> res = CompletableFuture.supplyAsync(()
                        -> blogUserService.getOneUserInfo(row.getUser_id()));
                promiseList.add(res);
            } else {
                // 如果用户ID不存在，设置默认值
                BlogUser oneUserInfo = new BlogUser();
                oneUserInfo.setNick_name(row.getNick_name());
                oneUserInfo.setAvatar("");
                CompletableFuture<BlogUser> futureUserInfo = CompletableFuture.completedFuture(oneUserInfo);
                promiseList.add(futureUserInfo);
            }
        }
        // 等待所有异步任务完成
        CompletableFuture<Void> allUsers = CompletableFuture.allOf(promiseList.toArray(new CompletableFuture[0]));
        //方法一：使用 thenAccept 方法来处理完成后的操作，可以更加灵活地执行其他操作或链式调用其他方法
        //ignored:一个标识符
        allUsers.thenAccept(ignored -> {
            for (int i = 0; i < rows.size(); i++) {
                BlogUser blogUser = promiseList.get(i).join();
                //检查 CompletableFuture 是否以异常完成
                if (blogUser != null) {
                    rows.get(i).setNick_name(blogUser.getNick_name());
                    rows.get(i).setAvatar(blogUser.getAvatar());
                }
            }
        }).join(); // 等待用户信息异步任务完成

        //方法二：直接使用 join() 方法，等待异步任务完成后再进行处理。
        //如果只是简单地等待异步任务完成并在完成后进行处理，第二种方式可能更直接简洁.
//        allUsers.join();
//        // 将获取到的用户信息应用到行数据中
//        for (int i = 0; i < promiseList.size(); i++) {
//            BlogUser blogUser = promiseList.get(i).join();
//            //检查 CompletableFuture 是否以异常完成
//            if (blogUser != null) {
//                rows.get(i).setNick_name(blogUser.getNick_name());
//                rows.get(i).setAvatar(blogUser.getAvatar());
//            }
//        }


//        // 根据用户user_id获取用户当前的昵称和头像
//        // 创建一个ExecutorService对象，根据你的需要选择合适的线程池大小
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//
//        // 创建一个List<Future>对象，用于存储每个异步任务的返回值
//        List<Future<BlogUser>> promiseList = new ArrayList<>();
//
//        // 遍历rows数组中的每个元素   异步实现
//        for(BlogMessage row : rows){
//            // 创建一个Callable对象，定义异步任务的逻辑
//            Callable<BlogUser> task = () -> {
//                if (row.getUser_id() != null) {
//                    // 调用其他方法或访问数据库，获取需要的数据
//                    BlogUser oneUserInfo = blogUserService.getOneUserInfo(row.getUser_id());
//                    return oneUserInfo;
//                }else {
//                    BlogUser oneUserInfo = new BlogUser();
//                    oneUserInfo.setNick_name(row.getNick_name());
//                    oneUserInfo.setAvatar("");
//                    return oneUserInfo;
//                }
//            };
//            // 将Callable对象提交给线程池执行，并将返回的Future对象添加到List对象中
//            Future<BlogUser> future = executorService.submit(task);
//            promiseList.add(future);
//        }
//
//        //同步实现
//        int index = 0;
//        for (Future<BlogUser> future : promiseList) {
//            try {
//                BlogUser v = future.get();
//                if (v != null) {
//                    rows.get(index).setNick_name(v.getNick_name());
//                    rows.get(index).setAvatar(v.getAvatar());
//                }
//            }catch (InterruptedException | ExecutionException e){
//                e.printStackTrace();
//            }
//            index ++;
//        }

        // 判断当前登录用户是否点赞了
        if (user_id != null) {
            // 异步获取用户点赞信息
            List<CompletableFuture<Boolean>> likeFutures = rows.stream()
                    .map(row -> CompletableFuture.supplyAsync(() -> blogLikeService.getIsLikeByIdAndType(row.getId(), 3, user_id)))
                    .collect(Collectors.toList());

            // 等待所有用户信息异步任务完成并处理结果
            CompletableFuture<Void> allLikes = CompletableFuture.allOf(likeFutures.toArray(new CompletableFuture[0]));
            allLikes.thenAccept(ignored -> {
                for (int i = 0; i < rows.size(); i++) {
                    try {
                        Boolean aBoolean = likeFutures.get(i).get();
                        rows.get(i).setIs_like(aBoolean);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).join(); // 等待用户信息异步任务完成
        }

        // 异步获取每一条的评论条数
        List<CompletableFuture<Long>> promiseCommentList = rows.stream()
                .map(row -> CompletableFuture.supplyAsync(() -> blogCommentService.getCommentTotal(row.getId(), 3)))
                .collect(Collectors.toList());
        // 等待所有异步任务完成并处理结果
        CompletableFuture<Void> allComments = CompletableFuture.allOf(promiseCommentList.toArray(new CompletableFuture[0]));
        allComments.thenAccept(ignored -> {
            for (int i = 0; i < rows.size(); i++) {
                Long r = promiseCommentList.get(i).join();
                rows.get(i).setComment_total(r);
            }
        }).join(); // 等待异步任务完成

//        // 创建一个List<Future>对象，用于存储每个异步任务的返回值
//        List<Future<Long>> promiseCommentList = new ArrayList<>();
//
//        // 遍历rows数组中的每个元素   异步实现
//        for(BlogMessage row : rows){
//            // 创建一个Callable对象，定义异步任务的逻辑
//            Callable<Long> task = () -> {
//                // 调用其他方法或访问数据库，获取需要的数据
//                Long commentTotal = blogCommentService.getCommentTotal(row.getId(), 3);
//                return commentTotal;
//            };
//            // 将Callable对象提交给线程池执行，并将返回的Future对象添加到List对象中
//            Future<Long> future = executorService.submit(task);
//            promiseCommentList.add(future);
//        }
//        //同步实现
//        int x = 0;
//        for (Future<Long> future : promiseCommentList) {
//            try {
//                Long r = future.get();
//                rows.get(index).setComment_total(r);
//            }catch (InterruptedException | ExecutionException e){
//                e.printStackTrace();
//            }
//            x ++;
//        }
//
//        // 关闭线程池
//        executorService.shutdown();

        //添加返回值
        //插入带下载七牛云图片凭证的url
        for(BlogMessage blogMessage : rows){
            String bg_url = blogMessage.getBg_url();
            try {
                blogMessage.setBg_url(qiniu.downloadUrl(bg_url));
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        PageInfoResult<BlogMessage> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }

    @Override
    public List<Map<String, Object>> getMessageTag() {
        List<BlogMessage> messages = blogMessageMapper.selectList(null);

        List<Map<String, Object>> arr = new ArrayList<>();
        if(messages != null && !messages.isEmpty()){
            for(BlogMessage v : messages){
                if(v.getTag() != null){
                    int index = -1;
                    for (int i = 0; i < arr.size(); i++) {
                        Map<String, Object> item = arr.get(i);
                        if (v.getTag().equals(item.get("tag"))) {
                            index = i;
                            break;
                        }
                    }
                    if (index == -1) {
                        Map<String, Object> newItem = new HashMap<>();
                        newItem.put("tag", v.getTag());
                        newItem.put("count", 1);
                        arr.add(newItem);
                    } else {
                        int count = (int) arr.get(index).get("count");
                        arr.get(index).put("count", count + 1);
                    }
                }
            }
        }
        //对arr数组中的元素进行排序，以便按照标签出现次数从高到低排序
        for (int i = 0; i < arr.size(); i++) {
            for (int j = i + 1; j < arr.size(); j++) {
                if ((int)arr.get(j).get("count") > (int)arr.get(i).get("count")) {
                    Map<String, Object> temp = arr.get(i);
                    arr.set(i, arr.get(j));
                    arr.set(j, temp);
                }
            }
        }
        //Math.min(10, arr.size()) 是为了确保获取的子列表长度最多为 10，即使原始列表 arr 的长度超过了 10。
        // 如果 arr 的长度小于 10，则返回整个列表作为子列表。
        return arr.subList(0, Math.min(10, arr.size()));
    }

    @Override
    public Boolean addMessage(BlogMessage blogMessage) {
        int insert = blogMessageMapper.insert(blogMessage);

        return insert > 0;
    }

    @Override
    public Boolean updateMessage(BlogMessage blogMessage) {
        int i = blogMessageMapper.updateById(blogMessage);
        return i > 0;
    }

    @Override
    public Integer deleteMessage(List<Integer> idList) {
        int i = blogMessageMapper.deleteBatchIds(idList);
        return i;
    }

    @Override
    public Boolean likeMessage(Integer id) {
        BlogMessage blogMessage = blogMessageMapper.selectById(id);
        if (blogMessage != null) {
            blogMessage.setLike_times(blogMessage.getLike_times() + 1);
            int i = blogMessageMapper.updateById(blogMessage);
            return i > 0;
        }else {
            return false;
        }
    }

    @Override
    public Boolean cancelLikeMessage(Integer id) {
        BlogMessage blogMessage = blogMessageMapper.selectById(id);
        if (blogMessage != null) {
            blogMessage.setLike_times(blogMessage.getLike_times() - 1);
            int i = blogMessageMapper.updateById(blogMessage);
            return i > 0;
        }else {
            return false;
        }
    }
}




