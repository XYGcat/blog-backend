package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.xc.blogbackend.mapper.BlogCommentMapper;
import com.xc.blogbackend.model.domain.BlogComment;
import com.xc.blogbackend.model.domain.BlogUser;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogCommentService;
import com.xc.blogbackend.service.BlogLikeService;
import com.xc.blogbackend.service.BlogUserService;
import com.xc.blogbackend.utils.IpUtils;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
* @author XC
* @description 针对表【blog_comment】的数据库操作Service实现
* @createDate 2023-11-23 18:17:54
*/
@Service
@Slf4j
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment>
    implements BlogCommentService{

    @Resource
    private BlogCommentMapper blogCommentMapper;

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private Qiniu qiniu;

    @Resource
    private BlogLikeService blogLikeService;

    @Override
    public Long getCommentTotal(Integer for_id, Integer type) {
        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("for_id", for_id).eq("type", type);
        Long count = blogCommentMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public PageInfoResult<BlogComment> frontGetParentComment(Map<String, Object> request) {
        Integer current = (Integer) request.get("current");
        Integer size = (Integer) request.get("size");
        Integer type = (Integer) request.get("type");
        Integer for_id = (Integer) request.get("for_id");
        Integer user_id = (Integer) request.get("user_id");
        String order = (String) request.get("order");

        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        if (type != null){
            queryWrapper.eq("type",type);
        }
        if (for_id != null){
            queryWrapper.eq("for_id",for_id);
        }
        // 模拟添加 { parent_id: null } 条件
        queryWrapper.isNull("parent_id");
        if(order == "new"){
            queryWrapper.orderByDesc("createdAt");
        }else {
            queryWrapper.orderByDesc("thumbs_up");
        }
        Page<BlogComment> page = new Page<>(current,size);
        Page<BlogComment> commentPage = blogCommentMapper.selectPage(page, queryWrapper);
        List<BlogComment> rows = commentPage.getRecords();
        long count = commentPage.getTotal();

        //获取地理位置并返回
        for (BlogComment v : rows){
            String location = IpUtils.getLocation(v.getIp());
            v.setIpAddress(location);
        }

        // 根据用户id获取用户当前的昵称和头像
        List<CompletableFuture<BlogUser>> promiseList = new ArrayList<>();
        for (BlogComment row : rows){
            if (row.getFrom_id() != null) {
                CompletableFuture<BlogUser> res = CompletableFuture.supplyAsync(
                        () -> blogUserService.getOneUserInfo(row.getFrom_id()));
                promiseList.add(res);
            }
        }
        // 等待所有异步任务完成
        CompletableFuture<Void> allUsers = CompletableFuture.allOf(promiseList.toArray(new CompletableFuture[0]));
        //方法一：使用 thenAccept 方法来处理完成后的操作，可以更加灵活地执行其他操作或链式调用其他方法
        allUsers.thenAccept(ignored -> {        //ignored:一个标识符
            for (int i = 0; i < rows.size(); i++) {
                BlogUser blogUser = promiseList.get(i).join();
                //检查 CompletableFuture 是否以异常完成
                if (blogUser != null) {
                    rows.get(i).setFrom_avatar(blogUser.getAvatar());
                    rows.get(i).setFrom_name(blogUser.getNick_name());
                }
            }
        }).join(); // 等待用户信息异步任务完成

        // 判断当前登录用户是否点赞了
        if (user_id != null) {
            // 异步获取用户点赞信息
            List<CompletableFuture<Boolean>> promiseLikeList = rows.stream()
                    .map(row -> CompletableFuture.supplyAsync(() -> blogLikeService.getIsLikeByIdAndType(row.getId(), 4, user_id)))
                    .collect(Collectors.toList());

            // 等待所有用户信息异步任务完成并处理结果
            CompletableFuture<Void> allLikes = CompletableFuture.allOf(promiseLikeList.toArray(new CompletableFuture[0]));
            allLikes.thenAccept(ignored -> {
                for (int i = 0; i < rows.size(); i++) {
                    try {
                        Boolean aBoolean = promiseLikeList.get(i).get();
                        rows.get(i).setIs_like(aBoolean);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).join(); // 等待用户信息异步任务完成
        }

        for (BlogComment row : rows){
            String from_avatar = row.getFrom_avatar();
//            String to_avatar = row.getTo_avatar();
            try {
                row.setFrom_avatar(qiniu.downloadUrl(from_avatar));
//                row.setTo_avatar(qiniu.downloadUrl(to_avatar));
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        PageInfoResult<BlogComment> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }

    @Override
    public PageInfoResult<BlogComment> frontGetChildrenComment(Map<String,Object> request) {
        Integer current = (Integer) request.get("current");
        Integer size = (Integer) request.get("size");
        Integer type = (Integer) request.get("type");
        Integer for_id = (Integer) request.get("for_id");
        Integer user_id = (Integer) request.get("user_id");
        Integer parent_id = (Integer) request.get("parent_id");

        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        if (type != null){
            queryWrapper.eq("type",type);
        }
        if (for_id != null){
            queryWrapper.eq("for_id",for_id);
        }
        if (parent_id != null){
            queryWrapper.eq("parent_id",parent_id);
        }
        queryWrapper.orderByAsc("createdAt");
        Page<BlogComment> page = new Page<>(current,size);
        Page<BlogComment> commentPage = blogCommentMapper.selectPage(page, queryWrapper);
        List<BlogComment> rows = commentPage.getRecords();
        long count = commentPage.getTotal();

        //获取地理位置
        for (BlogComment v : rows){
            String location = IpUtils.getLocation(v.getIp());
            v.setIpAddress(location);
        }

        // 根据用户id获取用户当前的昵称和头像
        List<CompletableFuture<BlogUser>> promiseList = new ArrayList<>();
        for (BlogComment row : rows){
            if (row.getFrom_id() != null) {
                CompletableFuture<BlogUser> res = CompletableFuture.supplyAsync(
                        () -> blogUserService.getOneUserInfo(row.getFrom_id()));
                promiseList.add(res);
            }
        }
        // 等待所有异步任务完成
        CompletableFuture<Void> fromAllUsers = CompletableFuture.allOf(promiseList.toArray(new CompletableFuture[0]));
        //方法一：使用 thenAccept 方法来处理完成后的操作，可以更加灵活地执行其他操作或链式调用其他方法
        fromAllUsers.thenAccept(ignored -> {        //ignored:一个标识符
            for (int i = 0; i < rows.size(); i++) {
                BlogUser blogUser = promiseList.get(i).join();
                //检查 CompletableFuture 是否以异常完成
                if (blogUser != null) {
                    rows.get(i).setFrom_avatar(blogUser.getAvatar());
                    rows.get(i).setFrom_name(blogUser.getNick_name());
                }
            }
        }).join(); // 等待用户信息异步任务完成

        // 根据用户id获取用户当前的昵称和头像
        List<CompletableFuture<BlogUser>> promiseList2 = new ArrayList<>();
        for (BlogComment row : rows){
            if (row.getTo_id() != null) {
                CompletableFuture<BlogUser> res = CompletableFuture.supplyAsync(
                        () -> blogUserService.getOneUserInfo(row.getTo_id()));
                promiseList2.add(res);
            }
        }
        // 等待所有异步任务完成
        CompletableFuture<Void> toAllUsers = CompletableFuture.allOf(promiseList2.toArray(new CompletableFuture[0]));
        //方法一：使用 thenAccept 方法来处理完成后的操作，可以更加灵活地执行其他操作或链式调用其他方法
        toAllUsers.thenAccept(ignored -> {        //ignored:一个标识符
            for (int i = 0; i < rows.size(); i++) {
                BlogUser blogUser = promiseList.get(i).join();
                //检查 CompletableFuture 是否以异常完成
                if (blogUser != null) {
                    rows.get(i).setTo_avatar(blogUser.getAvatar());
                    rows.get(i).setTo_name(blogUser.getNick_name());
                }
            }
        }).join(); // 等待用户信息异步任务完成

        // 判断当前登录用户是否点赞了
        if (user_id != null) {
            // 异步获取用户点赞信息
            List<CompletableFuture<Boolean>> promiseLikeList = rows.stream()
                    .map(row -> CompletableFuture.supplyAsync(() -> blogLikeService.getIsLikeByIdAndType(row.getId(), 4, user_id)))
                    .collect(Collectors.toList());

            // 等待所有用户信息异步任务完成并处理结果
            CompletableFuture<Void> allLikes = CompletableFuture.allOf(promiseLikeList.toArray(new CompletableFuture[0]));
            allLikes.thenAccept(ignored -> {
                for (int i = 0; i < rows.size(); i++) {
                    try {
                        Boolean aBoolean = promiseLikeList.get(i).get();
                        rows.get(i).setIs_like(aBoolean);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).join(); // 等待用户信息异步任务完成
        }

        for (BlogComment row : rows){
            String from_avatar = row.getFrom_avatar();
            String to_avatar = row.getTo_avatar();
            try {
                row.setFrom_avatar(qiniu.downloadUrl(from_avatar));
                row.setTo_avatar(qiniu.downloadUrl(to_avatar));
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        PageInfoResult<BlogComment> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }

    @Override
    public PageInfoResult<BlogComment> backGetCommentList(Map<String, Object> request) {
        Integer current = (Integer) request.get("current");
        Integer size = (Integer) request.get("size");
        String content = (String) request.get("content");
        String to_name = (String) request.get("to_name");
        String from_name = (String) request.get("from_name");
        List<String> time = (List<String>) request.get("time");

        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        if (content != null && !content.isEmpty()){
            queryWrapper.like("content","%" + content + "%");
        }
        if (to_name != null && !to_name.isEmpty()){
            queryWrapper.like("to_name","%" + to_name + "%");
        }
        if (from_name != null && !from_name.isEmpty()){
            queryWrapper.like("from_name","%" + from_name + "%");
        }
        if (time != null && time.size() == 2 && time.get(0) != null && time.get(1) != null) {
            queryWrapper.between("createdAt", time.get(0), time.get(1));
        }
        queryWrapper.orderByDesc("createdAt");
        Page<BlogComment> page = new Page<>(current,size);
        Page<BlogComment> commentPage = blogCommentMapper.selectPage(page, queryWrapper);
        List<BlogComment> rows = commentPage.getRecords();
        long count = commentPage.getTotal();

        //获取地理位置
        for (BlogComment v : rows){
            String location = IpUtils.getLocation(v.getIp());
            v.setIpAddress(location);
        }

        // 根据用户from_id获取用户当前的昵称和头像
        List<CompletableFuture<BlogUser>> promiseList = new ArrayList<>();
        for (BlogComment row : rows){
            if (row.getFrom_id() != null) {
                CompletableFuture<BlogUser> res = CompletableFuture.supplyAsync(
                        () -> blogUserService.getOneUserInfo(row.getFrom_id()));
                promiseList.add(res);
            }
        }
        // 等待所有异步任务完成
        CompletableFuture<Void> fromAllUsers = CompletableFuture.allOf(promiseList.toArray(new CompletableFuture[0]));
        //方法一：使用 thenAccept 方法来处理完成后的操作，可以更加灵活地执行其他操作或链式调用其他方法
        fromAllUsers.thenAccept(ignored -> {        //ignored:一个标识符
            for (int i = 0; i < rows.size(); i++) {
                BlogUser blogUser = promiseList.get(i).join();
                //检查 CompletableFuture 是否以异常完成
                if (blogUser != null) {
                    rows.get(i).setFrom_avatar(blogUser.getAvatar());
                    rows.get(i).setFrom_name(blogUser.getNick_name());
                }
            }
        }).join(); // 等待用户信息异步任务完成

        // 根据用户id获取用户当前的昵称和头像
        List<CompletableFuture<BlogUser>> promiseList2 = new ArrayList<>();
        for (BlogComment row : rows){
            if (row.getTo_id() != null) {
                CompletableFuture<BlogUser> res = CompletableFuture.supplyAsync(
                        () -> blogUserService.getOneUserInfo(row.getTo_id()));
                promiseList2.add(res);
            }
        }
        // 等待所有异步任务完成
        CompletableFuture<Void> toAllUsers = CompletableFuture.allOf(promiseList2.toArray(new CompletableFuture[0]));
        //方法一：使用 thenAccept 方法来处理完成后的操作，可以更加灵活地执行其他操作或链式调用其他方法
        toAllUsers.thenAccept(ignored -> {        //ignored:一个标识符
            for (int i = 0; i < rows.size(); i++) {
                BlogUser blogUser = promiseList.get(i).join();
                //检查 CompletableFuture 是否以异常完成
                if (blogUser != null) {
                    rows.get(i).setTo_avatar(blogUser.getAvatar());
                    rows.get(i).setTo_name(blogUser.getNick_name());
                }
            }
        }).join(); // 等待用户信息异步任务完成

        for (BlogComment row : rows){
            String from_avatar = row.getFrom_avatar();
            String to_avatar = row.getTo_avatar();
            try {
                row.setFrom_avatar(qiniu.downloadUrl(from_avatar));
                if (to_avatar != null) {
                    row.setTo_avatar(qiniu.downloadUrl(to_avatar));
                }
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        PageInfoResult<BlogComment> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }

    @Override
    public BlogComment createComment(BlogComment blogComment, String ip) {
        blogComment.setIp(ip);
        int insert = blogCommentMapper.insert(blogComment);
        return blogComment;
    }

    @Override
    public BlogComment applyComment(BlogComment blogComment, String ip) {
        blogComment.setIp(ip);
        //截取头像图片链接
        String to_avatar = blogComment.getTo_avatar();
        String baseUrl = StringManipulation.extractBaseUrl(to_avatar);
        blogComment.setTo_avatar(baseUrl);

        int insert = blogCommentMapper.insert(blogComment);
        return blogComment;
    }

    @Override
    public Boolean thumbUpComment(Integer id) {
        BlogComment blogComment = blogCommentMapper.selectById(id);
        if (blogComment != null) {
            Integer thumbs_up = blogComment.getThumbs_up();
            blogComment.setThumbs_up(thumbs_up + 1);
            int i = blogCommentMapper.updateById(blogComment);
            return i > 0;
        }
        return false;
    }

    @Override
    public Boolean cancelThumbUp(Integer id) {
        BlogComment blogComment = blogCommentMapper.selectById(id);
        if (blogComment != null) {
            Integer thumbs_up = blogComment.getThumbs_up();
            blogComment.setThumbs_up(thumbs_up - 1);
            int i = blogCommentMapper.updateById(blogComment);
            return i > 0;
        }
        return false;
    }

    @Override
    public Boolean deleteComment(Integer id, Integer parent_id) {
        // 如果有父级评论 就只删除这一条
        if (parent_id > 0){
            int deleteById = blogCommentMapper.deleteById(id);
            return deleteById > 0;
        }
        // 如果没有父级评论 就删除这条评论 以及子级评论
        else {
            int deleteById = blogCommentMapper.deleteById(id);
            QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id",id);
            int delete = blogCommentMapper.delete(queryWrapper);
            return deleteById > 0;
        }
    }
}




