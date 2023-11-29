package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.mapper.BlogTalkMapper;
import com.xc.blogbackend.model.domain.BlogTalk;
import com.xc.blogbackend.model.domain.BlogTalkPhoto;
import com.xc.blogbackend.model.domain.BlogUser;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogTalkPhotoService;
import com.xc.blogbackend.service.BlogTalkService;
import com.xc.blogbackend.service.BlogUserService;
import com.xc.blogbackend.utils.Qiniu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
* @author XC
* @description 针对表【blog_talk】的数据库操作Service实现
* @createDate 2023-11-20 14:28:11
*/
@Service
@Slf4j
public class BlogTalkServiceImpl extends ServiceImpl<BlogTalkMapper, BlogTalk>
    implements BlogTalkService{

    @Resource
    private BlogTalkMapper blogTalkMapper;

    @Resource
    private BlogTalkPhotoService blogTalkPhotoService;

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private Qiniu qiniu;

    @Override
    //异步方法实现
    public PageInfoResult<BlogTalk> getTalkList(Integer current, Integer size, Integer status) {
        // 分页参数处理
        int offset = (current - 1) * size;
        int limit = size;

        QueryWrapper<BlogTalk> queryWrapper = new QueryWrapper<>();    // 构建查询条件
        // 如果说说状态不为空，使用eq精确查询
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        //按照 is_top 升序和 createdAt 降序排列
        queryWrapper.orderByAsc("is_top").orderByDesc("createdAt");
        // 创建Page对象，设置当前页和分页大小
        Page<BlogTalk> page = new Page<>(offset, limit);
        // 获取说说列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogTalk> articlePage = blogTalkMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogTalk> rows = articlePage.getRecords();
        // 获取说说总数
        long count = articlePage.getTotal();

        // 异步获取图片列表
        List<CompletableFuture<List<BlogTalkPhoto>>> photoFutures = rows.stream()
                .map(v -> CompletableFuture.supplyAsync(() -> blogTalkPhotoService.getPhotoByTalkId(v.getId())))
                .collect(Collectors.toList());

        // 等待所有图片异步任务完成并处理结果
        CompletableFuture<Void> allPhotos = CompletableFuture.allOf(photoFutures.toArray(new CompletableFuture[0]));
        allPhotos.join(); // 等待图片异步任务完成
        // 处理异步任务结果
        IntStream.range(0, rows.size()).forEach(i -> {
            List<BlogTalkPhoto> photos = photoFutures.get(i).join();
            if (!photos.isEmpty()) {
                List<String> talkImgListResponse = photos.stream()
                        .map(photo -> {
                            try {
                                return qiniu.downloadUrl(photo.getUrl());
                            } catch (QiniuException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
                rows.get(i).setTalkImgListResponse(talkImgListResponse);
            }
        });

        // 异步获取用户信息
        List<CompletableFuture<BlogUser>> userFutures = rows.stream()
                .map(row -> CompletableFuture.supplyAsync(() -> blogUserService.getOneUserInfo(row.getUser_id())))
                .collect(Collectors.toList());

        // 等待所有用户信息异步任务完成并处理结果
        CompletableFuture<Void> allUsers = CompletableFuture.allOf(userFutures.toArray(new CompletableFuture[0]));
        allUsers.thenAccept(ignored -> {
            for (int i = 0; i < rows.size(); i++) {
                BlogUser user = userFutures.get(i).join();
                if (user != null) {
                    rows.get(i).setNick_name(user.getNick_name());
                    rows.get(i).setAvatar(user.getAvatar());
                }
            }
        }).join(); // 等待用户信息异步任务完成

        // 构造返回值 PageInfoResult
        PageInfoResult<BlogTalk> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }


      //同步实现方法
//    public PageInfoResult<BlogTalk> getTalkList(Integer current, Integer size, Integer status) {
//        // 分页参数处理
//        int offset = (current - 1) * size;
//        int limit = size;
//
//        queryWrapper.clear();
//        // 如果说说状态不为空，使用eq精确查询
//        if (status != null) {
//            queryWrapper.eq("status", status);
//        }
//        //按照 is_top 升序和 createdAt 降序排列
//        queryWrapper.orderByAsc("is_top").orderByDesc("createdAt");
//        // 创建Page对象，设置当前页和分页大小
//        Page<BlogTalk> page = new Page<>(offset, limit);
//        // 获取说说列表，使用page方法传入Page对象和QueryWrapper对象
//        Page<BlogTalk> articlePage = blogTalkMapper.selectPage(page, queryWrapper);
//        // 获取分页数据
//        List<BlogTalk> rows = articlePage.getRecords();
//        // 获取说说总数
//        long count = articlePage.getTotal();
//
////        //将获取到的 photoByTalkId 对象列表添加到 promiseList 中，形成一个列表的列表结构
////        List<List<BlogTalkPhoto>> promiseList = new ArrayList<>();
//
//        // 创建一个ExecutorService对象，根据你的需要选择合适的线程池大小
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//
//        // 创建一个List<Future>对象，用于存储每个异步任务的返回值
//        List<Future<List<BlogTalkPhoto>>> promiseList = new ArrayList<>();
//
//        // 遍历rows数组中的每个元素   异步实现
//        for(BlogTalk v : rows){
//            // 创建一个Callable对象，定义异步任务的逻辑
//            Callable<List<BlogTalkPhoto>> task = () -> {
//                // 调用其他方法或访问数据库，获取需要的数据
//                List<BlogTalkPhoto> photoByTalkId = blogTalkPhotoService.getPhotoByTalkId(v.getId());
////          promiseList.add(photoByTalkId);
//                return photoByTalkId;
//            };
//            // 将Callable对象提交给线程池执行，并将返回的Future对象添加到List对象中
//            Future<List<BlogTalkPhoto>> future = executorService.submit(task);
//            promiseList.add(future);
//        }
//
//        //同步实现
//        for (Future<List<BlogTalkPhoto>> future : promiseList) {
//            try {
//                List<BlogTalkPhoto> v = future.get();
//                if (!v.isEmpty()) {
//                    for (BlogTalkPhoto photo : v) {
//                        int index = findRowIndex(rows, photo.getTalk_id());
//                        if (index != -1) {
//                            List<String> talkImgListResponse = v.stream()
//                                                                .map(BlogTalkPhoto -> {
//                                                                    try {
//                                                                        return qiniu.downloadUrl(BlogTalkPhoto.getUrl());
//                                                                    } catch (QiniuException e) {
//                                                                        throw new RuntimeException(e);
//                                                                    }
//                                                                })
//                                                                .collect(Collectors.toList());
//                            rows.get(index).setTalkImgListResponse(talkImgListResponse);
//                        }
//                    }
//                }
//            }catch (InterruptedException | ExecutionException e){
//                e.printStackTrace();
//            }
//        }
//
//        // 创建一个List<Future>对象，用于存储每个异步任务的返回值
//        List<Future<BlogUser>> userList = new ArrayList<>();
//
//        // 遍历rows数组中的每个元素   异步实现
//        for(BlogTalk row : rows){
//            // 创建一个Callable对象，定义异步任务的逻辑
//            Callable<BlogUser> task = () -> {
//                // 调用其他方法或访问数据库，获取需要的数据
//                BlogUser oneUserInfo = blogUserService.getOneUserInfo(row.getUser_id());
//                return oneUserInfo;
//            };
//            // 将Callable对象提交给线程池执行，并将返回的Future对象添加到List对象中
//            Future<BlogUser> future = executorService.submit(task);
//            userList.add(future);
//        }
//
//        //同步实现
//        int index = 0;
//        for (Future<BlogUser> future : userList) {
//            try {
//                BlogUser r = future.get();
//                if (r != null) {
//                    rows.get(index).setNick_name(r.getNick_name());
//                    rows.get(index).setAvatar(r.getAvatar());
//                }
//            }catch (InterruptedException | ExecutionException e){
//                e.printStackTrace();
//            }
//            index ++;
//        }
//
//        // 关闭线程池
//        executorService.shutdown();
//
//        //添加返回值
//        PageInfoResult<BlogTalk> pageInfoResult = new PageInfoResult<>();
//        pageInfoResult.setSize(size);
//        pageInfoResult.setCurrent(current);
//        pageInfoResult.setTotal(count);
//        pageInfoResult.setList(rows);
//
//        return pageInfoResult;
//    }

    @Override
    public BlogTalk publishTalk(BlogTalk blogTalk) {
        List<Map<String, String>> talkImgList = blogTalk.getTalkImgList();
        blogTalkMapper.insert(blogTalk);

        if (blogTalk.getId() != null) {
            List<BlogTalkPhoto> imgList = talkImgList.stream().map(img -> {
                BlogTalkPhoto transformedImg = new BlogTalkPhoto();
                transformedImg.setTalk_id(blogTalk.getId());
                String imgurl = img.get("imgurl");
                transformedImg.setUrl(img.get("imgurl"));
                return transformedImg;
            }).collect(Collectors.toList());

            blogTalkPhotoService.publishTalkPhoto(imgList);
        }
        return blogTalk;
    }

    @Override
    public BlogTalk getTalkById(Integer id) {
        BlogTalk blogTalk = blogTalkMapper.selectById(id);
        if (blogTalk != null) {
            List<BlogTalkPhoto> photos = blogTalkPhotoService.getPhotoByTalkId(id);
            List<String> talkImgList = photos.stream().map(BlogTalkPhoto::getUrl).collect(Collectors.toList());
            blogTalk.setTalkImgListResponse(talkImgList);
            return blogTalk;
        }
        return null;
    }

    @Override
    public Boolean updateTalk(BlogTalk blogTalk) {
        Integer id = blogTalk.getId();
        List<Map<String, String>> talkImgList = blogTalk.getTalkImgList();
        List<BlogTalkPhoto> imgList = new ArrayList<>();

        int updateById = blogTalkMapper.updateById(blogTalk);
        if (updateById > 0) {
            // 先删除图片关联
            Boolean aBoolean = blogTalkPhotoService.deleteTalkPhoto(id);
            if (aBoolean){
                imgList = talkImgList.stream().map(img -> {
                    BlogTalkPhoto blogTalkPhoto = new BlogTalkPhoto();
                    blogTalkPhoto.setTalk_id(id);
                    blogTalkPhoto.setUrl(img.get("url"));
                    return blogTalkPhoto;
                }).collect(Collectors.toList());
            }
            // 添加图片关联
            List<BlogTalkPhoto> blogTalkPhotos = blogTalkPhotoService.publishTalkPhoto(imgList);

            return updateById > 0;
        }else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public Boolean toggleTop(Integer id, Integer is_top) {
        BlogTalk blogTalk = new BlogTalk();
        blogTalk.setIs_top(is_top);
        UpdateWrapper<BlogTalk> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        int update = blogTalkMapper.update(blogTalk, updateWrapper);
        return update > 0;
    }

    @Override
    public Boolean togglePublic(Integer id, Integer status) {
        BlogTalk blogTalk = new BlogTalk();
        blogTalk.setStatus(status);
        UpdateWrapper<BlogTalk> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        int update = blogTalkMapper.update(blogTalk, updateWrapper);
        return update > 0;
    }

    @Override
    public Boolean deleteTalkById(Integer id, Integer status) {
        int res;
        if (status == 1 || status == 2) {
            BlogTalk blogTalk = new BlogTalk();
            blogTalk.setStatus(3);
            UpdateWrapper<BlogTalk> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            res = blogTalkMapper.update(blogTalk, updateWrapper);
        }else {
            res = blogTalkMapper.deleteById(id);
            Boolean aBoolean = blogTalkPhotoService.deleteTalkPhoto(id);
        }
        return res > 0;
    }

    @Override
    public Boolean revertTalk(Integer id) {
        BlogTalk blogTalk = new BlogTalk();
        blogTalk.setStatus(1);
        UpdateWrapper<BlogTalk> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        int res = blogTalkMapper.update(blogTalk, updateWrapper);
        return res > 0;
    }
}




