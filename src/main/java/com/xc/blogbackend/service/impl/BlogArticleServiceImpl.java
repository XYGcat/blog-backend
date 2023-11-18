package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.mapper.BlogArticleMapper;
import com.xc.blogbackend.model.domain.ArticleDTO;
import com.xc.blogbackend.model.domain.BlogArticle;
import com.xc.blogbackend.model.domain.request.ArticleRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogArticleService;
import com.xc.blogbackend.service.BlogArticleTagService;
import com.xc.blogbackend.service.BlogCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
* @author XC
* @description 针对表【blog_article】的数据库操作Service实现
* @createDate 2023-11-16 01:29:08
*/
@Service
@Slf4j
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle>
    implements BlogArticleService{

    @Resource
    private BlogArticleMapper blogArticleMapper;

    @Resource
    private BlogArticleTagService blogArticleTagService;

    @Resource
    private BlogCategoryService blogCategoryService;

    @Override
    public long getArticleCount() {
        // 创建 QueryWrapper 对象，用于构建查询条件
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);

        // 使用 MyBatis-Plus 提供的 count 方法获取符合条件的文章数量
        return blogArticleMapper.selectCount(queryWrapper);
    }

    @Override
    public PageInfoResult<BlogArticle> getArticleList(@RequestBody ArticleRequest articleRequest) {
        // 从参数中提取所需的信息
        String article_title = articleRequest.getArticle_title();
        String create_time = articleRequest.getCreate_time();
        Integer status = articleRequest.getStatus();
        Integer is_top = articleRequest.getIs_top();
        int current = articleRequest.getCurrent();
        int size = articleRequest.getSize();
        Integer tag_id = articleRequest.getTag_id();
        Integer category_id = articleRequest.getCategory_id();

        // 分页参数处理
        int offset = (current - 1) * size;
        int limit = size;

        // 构建查询条件
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        // 如果文章标题不为空，使用like模糊查询
        if (article_title != null && !article_title.isEmpty()) {
            queryWrapper.like("article_title", "%" + article_title + "%");
        }
        // 如果创建时间不为空，使用between范围查询
        if (create_time != null) {
            // 假设 createdAt 是数据库字段名，需要替换成实际的字段名
            queryWrapper.between("createdAt", create_time, LocalDateTime.now());
        }
        // 如果是否置顶不为空，使用eq精确查询
        if (is_top != null) {
            queryWrapper.eq("is_top", is_top);
        }
        // 如果状态不为空，使用eq精确查询
        if (status != null) {
            queryWrapper.eq("status", status);
        } else {    // 如果状态为空，使用in范围查询，只查询状态为1或2的文章
            queryWrapper.in("status", Arrays.asList(1, 2)); // 1, 2 是状态值的例子，根据实际情况修改
        }
        // 如果分类id不为空，使用eq精确查询
        if (category_id != null) {
            queryWrapper.eq("category_id", category_id);
        }
        // 如果标签id不为空，根据标签id查文章
        if (tag_id != null) {
            List<Integer> articleIdList = blogArticleTagService.getArticleIdListByTagId(tag_id);
            // 如果文章id列表不为空，使用in范围查询
            if (articleIdList != null && !articleIdList.isEmpty()) {
                queryWrapper.in("id", articleIdList);
            }
        }
        // 排除文章内容和原始链接属性，使用select方法传入属性名的数组
        queryWrapper.select("id","article_title","author_id","category_id","article_cover",
                "is_top","status","type","createdAt", "updatedAt","view_times","article_description",
                "thumbs_up_times","reading_duration","article_order");
        // 按照创建时间降序排序，使用orderByDesc方法传入属性名
        queryWrapper.orderByDesc("createdAt");

        // 创建Page对象，设置当前页和分页大小
        Page<BlogArticle> page = new Page<>(offset, limit);
        // 获取文章列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogArticle> articlePage = blogArticleMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogArticle> rows = articlePage.getRecords();
        // 获取文章总数
        Long count = articlePage.getTotal();

//        数据清理：假设需要保留的字段是 id、title 和 createdAt
//        ArticleDTO 是自己定义的一个类，用于存储需要的字段
//        List<ArticleDTO> cleanedRows = rows.stream().map(article -> {
//            ArticleDTO articleDTO = new ArticleDTO();
//            articleDTO.setId(article.getId());
//            articleDTO.setTitle(article.getTitle());
//            articleDTO.setCreatedAt(article.getCreatedAt());
//            // 如果 ArticleDTO 中有其他需要的字段，也可以在这里设置
//            return articleDTO;
//        }).collect(Collectors.toList());


        // 创建一个ExecutorService对象，根据你的需要选择合适的线程池大小
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 创建一个List<Future>对象，用于存储每个异步任务的返回值
        List<Future<ArticleDTO>> promiseList = new ArrayList<>();

        // 遍历rows数组中的每个元素
        for (BlogArticle v : rows) {
            // 创建一个Callable对象，定义异步任务的逻辑
            Callable<ArticleDTO> task = () -> {
                // 调用其他方法或访问数据库，获取需要的数据
                String categoryName = blogCategoryService.getCategoryNameById(v.getCategory_id());
                Map<String, Object> tagList = blogArticleTagService.getTagListByArticleId(v.getId());
                // 创建一个对象，存储数据
                ArticleDTO articleDTO = new ArticleDTO();
                articleDTO.setCategoryName(categoryName);
                articleDTO.setTagList(tagList);
                // 返回对象
                return articleDTO;
            };
            // 将Callable对象提交给线程池执行，并将返回的Future对象添加到List对象中
            Future<ArticleDTO> future = executorService.submit(task);
            promiseList.add(future);
        }

        // 遍历List对象中的每个Future对象
        for (int i = 0; i < promiseList.size(); i++) {
            try {
                // 使用get方法获取每个异步任务的结果，并对结果进行处理
                ArticleDTO res = promiseList.get(i).get();
                rows.get(i).setCategoryName(res.getCategoryName());
                rows.get(i).setTagNameList(res.getTagNameList());
            } catch (InterruptedException | ExecutionException e) {
                // 处理可能抛出的异常
                e.printStackTrace();
            }
        }

        // 关闭线程池
        executorService.shutdown();

        //添加返回值
        PageInfoResult<BlogArticle> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setSize(size);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setTotal(count);
        pageInfoResult.setList(rows);

        return pageInfoResult;
    }

    @Override
    public Boolean getArticleInfoByTitle(String id, String article_title) {
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        queryWrapper.eq("article_title",article_title);
        BlogArticle article = blogArticleMapper.selectOne(queryWrapper);
        if (article != null) {
            if (id != null){
                return !article.getId().equals(id);
            }else {
                return true;
            }
        }else {
            return false;
        }
    }


}




