package com.xc.blogbackend.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.enums.CategoryEnum;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.mapper.BlogArticleMapper;
import com.xc.blogbackend.model.domain.*;
import com.xc.blogbackend.model.domain.request.ArticleRequest;
import com.xc.blogbackend.model.domain.request.UpdateArticleRequest;
import com.xc.blogbackend.model.domain.result.ArticleListByContent;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.model.domain.result.RecommendResult;
import com.xc.blogbackend.service.*;
import com.xc.blogbackend.utils.ImageLinkComparator;
import com.xc.blogbackend.utils.PaddingUtils;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
* @author XC
* @description 针对表【bg_article】的数据库操作Service实现
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

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private BlogTagService blogTagService;

    @Resource
    private Qiniu qiniu;

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
        List<String> create_time = articleRequest.getCreate_time();
        Integer status = articleRequest.getStatus();
        Integer is_top = articleRequest.getIs_top();
        int current = articleRequest.getCurrent();
        int size = articleRequest.getSize();
        Integer tag_id = articleRequest.getTag_id();
        Integer category_id = articleRequest.getCategory_id();

        // 构建查询条件
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        // 如果文章标题不为空，使用like模糊查询
        if (article_title != null && !article_title.isEmpty()) {
            queryWrapper.like("article_title", "%" + article_title + "%");
        }
        // 如果创建时间不为空，使用between范围查询
        if (create_time != null && create_time.size() == 2 && create_time.get(0) != null && create_time.get(1) != null) {
            queryWrapper.between("created_at", create_time.get(0), create_time.get(1));
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
                "is_top","status","type","created_at", "updated_at","view_times","article_description",
                "thumbs_up_times","reading_duration","article_order");
        // 按照创建时间降序排序，使用orderByDesc方法传入属性名
        queryWrapper.orderByDesc("created_at");

        // 创建Page对象，设置当前页和分页大小
        Page<BlogArticle> page = new Page<>(current, size);
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

        // 使用增强的 for 循环遍历 promiseList
        for (Future<ArticleDTO> future : promiseList) {
            try {
                ArticleDTO res = future.get();
                // 获取当前 future 的索引
                int index = promiseList.indexOf(future);
                if (index != -1) {
                    // 直接在循环中修改 rows 的数据
                    rows.get(index).setCategoryName(res.getCategoryName());
                    rows.get(index).setTagNameList(res.getTagNameList());
                    rows.get(index).setArticle_cover(qiniu.downloadUrl(rows.get(index).getArticle_cover()));
                }
            } catch (InterruptedException | ExecutionException | QiniuException e) {
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
    public Boolean getArticleInfoByTitle(Integer id, String article_title) {
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

    @Override
    public BlogArticle createArticle(BlogArticle article) {
        Integer insert = blogArticleMapper.insert(article);
        if (insert != null){
            return article;
        }else {
            return null;
        }
    }

    @Override
    public BlogArticle getArticleById(Integer article_id) {
        if (article_id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"为空");
        }
        BlogArticle blogArticle = blogArticleMapper.selectById(article_id);
        if (blogArticle != null) {
            // 对浏览次数属性进行自增
            blogArticle.setView_times(blogArticle.getView_times() + 1);
            // 保存更新后的文章对象到数据库
            blogArticleMapper.updateById(blogArticle);
        }
        // 获取标签列表
        Map<String, Object> listByArticleId = blogArticleTagService.getTagListByArticleId(article_id);
        List<Integer> tagIdList = (List<Integer>) listByArticleId.get("tagIdList");
        List<String> tagNameList = (List<String>) listByArticleId.get("tagNameList");
        // 获取分类名称
        String categoryNameById = blogCategoryService.getCategoryNameById(blogArticle.getCategory_id());
        // 获取文章作者昵称
        String authorNameById = blogUserService.getAuthorNameById(blogArticle.getAuthor_id());

        blogArticle.setTagIdList(tagIdList);
        blogArticle.setTagNameList(tagNameList);
        blogArticle.setAuthorName(authorNameById);
        blogArticle.setCategoryName(categoryNameById);

        return blogArticle;
    }

    @Override
    public String getMdImgList(Integer article_id) {
        BlogArticle blogArticle = blogArticleMapper.selectById(article_id);
        String mdImgList = blogArticle.getMdImgList();
        return mdImgList;
    }

    @Override
    public BlogArticle getArticle(Integer article_id) {
        BlogArticle blogArticle = blogArticleMapper.selectById(article_id);
        return blogArticle;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public Boolean updateArticle(UpdateArticleRequest request) {
        // 提取前端传入的数据
        UpdateArticleRequest.ArticleDate requestArticle = request.getArticle();
        List<BlogTag> tagList = requestArticle.getTagList();
        BlogCategory category = requestArticle.getCategory();

        // 转换为BlogArticle对象
        BlogArticle articleRest = PaddingUtils.mapToBlogArticle(request);
        Integer articleId = articleRest.getId();
        String articleTitle = articleRest.getArticle_title();

        // 获取数据库中的旧文章信息
        BlogArticle oldArticle = this.getArticle(articleId);
        String oldArticleTitle = oldArticle.getArticle_title();
        String oldArticleCover = oldArticle.getArticle_cover();

        // 如果封面图片改变，删除旧的七牛云文章封面图片
        if (oldArticleCover != null && !oldArticleCover.equals(articleRest.getArticle_cover())) {
            String imgKey = StringManipulation.subString(oldArticleCover);
            qiniu.deleteFile(imgKey);
        }

        // 监测文章内容中图片链接的变化，以此删除七牛云图片
        List<String> oldMdImgList = new ArrayList<>();
        List<String> newMdImgList = requestArticle.getMdImgList();
        String oldMdImg = oldArticle.getMdImgList();

        // 判断旧文章图片链接是否为空
        boolean isOldMdImgEmpty = ObjectUtil.isNotEmpty(oldMdImg);

        // 判断文章标题是否改变，改变则更新七牛云和数据库图片链接
        if (!oldArticleTitle.equals(articleTitle)){
            // 更新数据库文章内容中图片链接
            String newArticle_content = articleRest.getArticle_content();
            String replaceContent = newArticle_content.replace("/article/" + oldArticleTitle, "/article/" + articleTitle);
            articleRest.setArticle_content(replaceContent);
            // 更新数据库文章内容中图片列表链接
            String mdImgList = articleRest.getMdImgList();
            if (mdImgList != null){
                String replaceMdImgList = mdImgList.replace("/article/" + oldArticleTitle, "/article/" + articleTitle);
                articleRest.setMdImgList(replaceMdImgList);
            }
            // 更新七牛云中文章内容图片链接
            if (isOldMdImgEmpty){
                {
                    // 去除首尾的方括号并按逗号和空格分割
                    String[] linksArray = oldMdImg.substring(1, oldMdImg.length() - 1).split(", ");
                    oldMdImgList = Arrays.asList(linksArray);

                    // 找出在旧列表中但不在新列表中的元素
                    List<String> missingInOldList = ImageLinkComparator.findMissingElements(newMdImgList, oldMdImgList);
                    // 截取图片链接的key
                    for (int i = 0; i < missingInOldList.size(); i++) {
                        // 截取字符串
                        String subString = StringManipulation.subString(missingInOldList.get(i));
                        // 更新原始数组中的元素
                        missingInOldList.set(i, subString);
                    }
                    // 删除七牛云图片
                    qiniu.deleteFile(missingInOldList);
                }

                {
                    // 找出在旧列表和新列表中都存在的元素
                    List<String> commonElements = ImageLinkComparator.findCommonElements(newMdImgList, oldMdImgList);
                    for (String commonElement : commonElements) {
                        String newMdImg = commonElement.replace(oldArticleTitle, articleTitle);
                        String newKey = StringManipulation.subString(newMdImg);
                        String oldKey = StringManipulation.subString(commonElement);
                        qiniu.renameImgKey(oldKey, newKey);
                    }
                }
            }
            // 更新数据库和七牛云文章封面图片链接
            if (Objects.equals(oldArticleCover, articleRest.getArticle_cover())) {
                assert oldArticleCover != null;
                String newArticle_cover = oldArticleCover.replace(oldArticleTitle, articleTitle);
                articleRest.setArticle_cover(newArticle_cover);
                String newKey = StringManipulation.subString(newArticle_cover);
                String oldKey = StringManipulation.subString(oldArticleCover);
                qiniu.renameImgKey(oldKey,newKey);
            }
        }

        // 根据文章标题获取文章信息 校验是否可以新增或编辑文章
        Boolean byTitle = this.getArticleInfoByTitle(articleId, articleTitle);
        if (byTitle){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"已存在相同的文章标题");
        }

        // TODO: 2023-11-27 修改逻辑，如果标签没变，不修改，如果标签改变，重新生成
        // 先删除这个文章与标签之前的关联
        blogArticleTagService.deleteArticleTag(articleRest.getId());

        // 判断新的分类是新增的还是已经存在的 并且返回分类id
        Integer categoryOrReturn = createCategoryOrReturn(category.getId(),category.getCategoryName());
        articleRest.setCategory_id(categoryOrReturn);

        // 创建新的标签和文章关联
        createArticleTagByArticleId(articleRest.getId(), tagList);

        UpdateWrapper<BlogArticle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", articleRest.getId()); // 设置更新条件：ID 等于给定文章的 ID
        int res = blogArticleMapper.update(articleRest, updateWrapper);

        return res > 0;
    }

    @Override
    public String getArticleCoverById(Integer article_id) {
        BlogArticle blogArticle = blogArticleMapper.selectById(article_id);
        return blogArticle.getArticle_cover();
    }

    @Override
    public Boolean toggleArticlePublic(Integer id, Integer status) {
        if (status == 2) {
            status = 1;
        } else {
            status = 2;
        }
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setStatus(status);
        UpdateWrapper<BlogArticle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        int update = blogArticleMapper.update(blogArticle, updateWrapper);

        return update > 0;
    }

    @Override
    public Boolean revertArticle(Integer id) {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setStatus(1);
        UpdateWrapper<BlogArticle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        int update = blogArticleMapper.update(blogArticle, updateWrapper);
        return update >0;
    }

    @Override
    public Boolean updateTop(Integer id, Integer is_top) {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setIs_top(is_top);
        blogArticle.setId(id);
        int i = blogArticleMapper.updateById(blogArticle);
        return i > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public Boolean deleteArticle(Integer id, Integer status) {
        if (status != 3) {
            BlogArticle blogArticle = new BlogArticle();
            blogArticle.setStatus(3);
            UpdateWrapper<BlogArticle> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",id);
            int update = blogArticleMapper.update(blogArticle, updateWrapper);
            return update > 0;
        }else {
            int deleteById = blogArticleMapper.deleteById(id);
            if (deleteById > 0) {
                // 删除和标签的关联
                blogArticleTagService.deleteArticleTag(id);
            }
            return deleteById > 0;
        }
    }

    @Override
    public PageInfoResult<BlogArticle> blogHomeGetArticleList(Integer current, Integer size) {

        // 构建查询条件
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        // 设置排序规则
        queryWrapper.orderByAsc("is_top")
                .orderByAsc("article_order")
                .orderByDesc("created_at");
        // 设置属性过滤，排除 articleContent 和 originUrl 属性
        queryWrapper.select(BlogArticle.class, info -> !info.getColumn().equals("article_content")
                && !info.getColumn().equals("origin_url"));

        // 创建Page对象，设置当前页和分页大小
        Page<BlogArticle> page = new Page<>(current, size);
        // 获取文章列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogArticle> articlePage = blogArticleMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogArticle> rows = articlePage.getRecords();
        // 获取文章总数
        Long count = articlePage.getTotal();

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

        // 使用增强的 for 循环遍历 promiseList
        for (Future<ArticleDTO> future : promiseList) {
            try {
                ArticleDTO res = future.get();
                // 获取当前 future 的索引
                int index = promiseList.indexOf(future);
                if (index != -1) {
                    // 直接在循环中修改 rows 的数据
                    rows.get(index).setCategoryName(res.getCategoryName());
                    rows.get(index).setTagNameList(res.getTagNameList());
                    rows.get(index).setArticle_cover(qiniu.downloadUrl(rows.get(index).getArticle_cover()));
                }
            } catch (InterruptedException | ExecutionException | QiniuException e) {
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
    public RecommendResult getRecommendArticleById(Integer article_id) {
        // 上一篇文章id
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("id",article_id);
        queryWrapper.eq("status",1);
        queryWrapper.select("id","article_title","article_cover");
        queryWrapper.orderByDesc("id");
        queryWrapper.last("LIMIT 1"); // 添加 LIMIT 条件，只返回一条记录
        BlogArticle contextPrevious = blogArticleMapper.selectOne(queryWrapper);

        // 下一篇文章id
        queryWrapper.clear();
        queryWrapper.gt("id",article_id);
        queryWrapper.eq("status",1);
        queryWrapper.select("id","article_title","article_cover");
        queryWrapper.orderByAsc("id");
        queryWrapper.last("LIMIT 1"); // 添加 LIMIT 条件，只返回一条记录
        BlogArticle contentNext = blogArticleMapper.selectOne(queryWrapper);

        // 上下文不存在的话就取当前的
        if (contextPrevious == null) {
            queryWrapper.clear();
            queryWrapper.eq("id",article_id);
            queryWrapper.select("id","article_title","article_cover");
            contextPrevious = blogArticleMapper.selectOne(queryWrapper);
        }
        if (contentNext == null) {
            queryWrapper.clear();
            queryWrapper.eq("id",article_id);
            queryWrapper.select("id","article_title","article_cover");
            contentNext = blogArticleMapper.selectOne(queryWrapper);
        }
        Map<String, Object> tagListByArticleId = blogArticleTagService.getTagListByArticleId(article_id);
        List<Integer> tagIdList = (List<Integer>) tagListByArticleId.get("tagIdList");
        List<Integer> articleIdList = new ArrayList<>();
        for (Integer tag_id : tagIdList){
            List<Integer> listByTagId = blogArticleTagService.getArticleIdListByTagId(tag_id);
            if (listByTagId != null) {
                articleIdList.addAll(listByTagId);
            }
        }

        // 获取文章tagId在本文章中的 最多六篇推荐文章
        queryWrapper.clear();
        queryWrapper.in("id", articleIdList)
                    .eq("status", 1);
        // 设置返回结果中包含的属性
        queryWrapper.select("id", "article_title", "article_cover", "created_at");
        // 设置排序规则，按 createdAt 降序排列
        queryWrapper.orderByDesc("created_at");
        // 设置查询结果数量限制为 6 条记录
        queryWrapper.last("LIMIT 6");

        List<BlogArticle> recommend = blogArticleMapper.selectList(queryWrapper);

        RecommendResult recommendResult = new RecommendResult();
        recommendResult.setRecommend(recommend);
        recommendResult.setNext(contentNext);
        recommendResult.setPrevious(contextPrevious);

        return recommendResult;
    }

    @Override
    public PageInfoResult<BlogArticle> blogTimelineGetArticleList(Integer current, Integer size) {

        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();    // 构建查询条件
        queryWrapper.eq("status", 1);
        queryWrapper.select("id","article_title","article_cover","created_at as createdAt" );
        //按照 createdAt 降序排列
        queryWrapper.orderByDesc("created_at");
        // 创建Page对象，设置当前页和分页大小
        Page<BlogArticle> page = new Page<>(current, size);
        // 获取通知列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogArticle> articlePage = blogArticleMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogArticle> rows = articlePage.getRecords();
        // 获取通知总数
        long count = articlePage.getTotal();

        Map<String, List<BlogArticle>> resultList = new HashMap<>();
        for (BlogArticle v : rows){
            //添加七牛云下载图片凭证
            try {
                String url = qiniu.downloadUrl(v.getArticle_cover());
                v.setArticle_cover(url);
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
            Date createdAt = v.getCreatedAt();
            String year = "year_" + StringManipulation.getYearFromDate(createdAt);
            //如果resultList中已经有了year这个键，它将直接将v添加到对应的列表中；
            //如果没有这个键，它将会新建一个ArrayList，并将v加入其中.
            resultList.computeIfAbsent(year, k -> new ArrayList<>()).add(v);
        }
        // 整合数据
        List<Map<String, Object>> finalList = new ArrayList<>();
        for (String key : resultList.keySet()) {
            String year = key.replace("year_", "");

            Map<String, Object> obj = new HashMap<>();
            obj.put("year", year);
            obj.put("articleList", resultList.get(key));

            finalList.add(obj);
        }

        PageInfoResult<BlogArticle> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setCurrent(current);
        pageInfoResult.setFinalList(finalList);
        pageInfoResult.setTotal(count);
        pageInfoResult.setSize(size);
        return pageInfoResult;
    }

    @Override
    public PageInfoResult<BlogArticle> getArticleListByCategoryId(Integer current, Integer size, Integer category_id) {

        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();    // 构建查询条件
        queryWrapper.eq("status", 1);
        queryWrapper.eq("category_id",category_id);
        queryWrapper.select("id","article_title","article_cover","created_at");
        queryWrapper.orderByDesc("created_at");
        // 创建Page对象，设置当前页和分页大小
        Page<BlogArticle> page = new Page<>(current, size);
        // 获取通知列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogArticle> articlePage = blogArticleMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogArticle> rows = articlePage.getRecords();
        // 获取通知总数
        long count = articlePage.getTotal();

        for (BlogArticle blogArticle : rows){
            String article_cover = blogArticle.getArticle_cover();
            try {
                blogArticle.setArticle_cover(qiniu.downloadUrl(article_cover));
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        PageInfoResult<BlogArticle> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setCurrent(current);
        pageInfoResult.setList(rows);
        pageInfoResult.setTotal(count);
        pageInfoResult.setSize(size);
        return pageInfoResult;
    }

    @Override
    public PageInfoResult<BlogArticle> getArticleListByTagId(Integer current, Integer size, Integer tag_id) {
        List<Integer> tagIdList = blogArticleTagService.getArticleIdListByTagId(tag_id);

        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();    // 构建查询条件
        queryWrapper.eq("status", 1);
        queryWrapper.in("id",tagIdList);
        queryWrapper.select("id","article_title","article_cover","created_at");
        queryWrapper.orderByDesc("created_at");
        // 创建Page对象，设置当前页和分页大小
        Page<BlogArticle> page = new Page<>(current, size);
        // 获取通知列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogArticle> articlePage = blogArticleMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogArticle> rows = articlePage.getRecords();
        // 获取通知总数
        long count = articlePage.getTotal();

        for (BlogArticle blogArticle : rows){
            String article_cover = blogArticle.getArticle_cover();
            try {
                blogArticle.setArticle_cover(qiniu.downloadUrl(article_cover));
            } catch (QiniuException e) {
                throw new RuntimeException(e);
            }
        }

        PageInfoResult<BlogArticle> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setCurrent(current);
        pageInfoResult.setList(rows);
        pageInfoResult.setTotal(count);
        pageInfoResult.setSize(size);
        return pageInfoResult;
    }

    @Override
    public List<BlogArticle> getHotArticle() {
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        queryWrapper.select("id","article_title","view_times");
        queryWrapper.last("LIMIT 5");
        queryWrapper.orderByDesc("view_times");
        List<BlogArticle> blogArticles = blogArticleMapper.selectList(queryWrapper);
        return blogArticles;
    }

    @Override
    public List<ArticleListByContent> getArticleListByContent(String content) {
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",1);
        queryWrapper.like("article_content", "%" + content + "%");
        queryWrapper.select("id", "article_title", "article_content", "view_times");
        queryWrapper.last("LIMIT 8");
        queryWrapper.orderByDesc("view_times");
        List<BlogArticle> blogArticles = blogArticleMapper.selectList(queryWrapper);

        List<ArticleListByContent> result = new ArrayList<>();
        String extracted;
        if (blogArticles != null) {
            for (BlogArticle r : blogArticles){
                String article_content = r.getArticle_content();
                int index = article_content.indexOf(content);
                if (index != -1) { // 如果找到了指定内容
                    int previous = index;
                    int next = index + content.length() + 12;
                    // 截取文章内容，构建结果对象
                    if (next <= article_content.length() - previous) {
                        extracted = article_content.substring(previous, next);
                        // 处理截取的内容
                    } else {
                        extracted = article_content.substring(previous);
                        // 处理从 previous 到字符串结尾的内容
                    }
                    ArticleListByContent resultObject = new ArticleListByContent();
                    resultObject.setId(r.getId());
                    resultObject.setArticle_content(extracted);
                    resultObject.setArticle_title(r.getArticle_title());
                    result.add(resultObject);
                }
            }
        }
        return result;
    }

    @Override
    public Boolean articleLike(Integer id) {
        BlogArticle blogArticle = blogArticleMapper.selectById(id);
        if (blogArticle != null){
            Integer thumbs_up_times = blogArticle.getThumbs_up_times();
            blogArticle.setThumbs_up_times(thumbs_up_times + 1);
            int i = blogArticleMapper.updateById(blogArticle);
            return i > 0;
        }
        return false;
    }

    @Override
    public Boolean cancelArticleLike(Integer id) {
        BlogArticle blogArticle = blogArticleMapper.selectById(id);
        if (blogArticle != null){
            Integer thumbs_up_times = blogArticle.getThumbs_up_times();
            blogArticle.setThumbs_up_times(thumbs_up_times - 1);
            int i = blogArticleMapper.updateById(blogArticle);
            return i > 0;
        }
        return false;
    }

    @Override
    public Boolean addReadingDuration(Integer id, Integer duration) {
        BlogArticle blogArticle = blogArticleMapper.selectById(id);
        if (blogArticle != null){
            Double reading_duration = blogArticle.getReading_duration();
            blogArticle.setReading_duration(reading_duration + duration);
            int i = blogArticleMapper.updateById(blogArticle);
            return i > 0;
        }
        return false;
    }

    /**
     * 新增和编辑文章关于分类的公共方法
     *
     * @param id
     * @param categoryName
     * @return
     */
    public Integer createCategoryOrReturn(Integer id,String categoryName){
        Integer finalId;
        if (id != null) {
            finalId = id;
        } else {
//            BlogCategoryServiceImpl blogCategoryService = new BlogCategoryServiceImpl();
            BlogCategory oneCategory = blogCategoryService.getOneCategory(categoryName);
            if (oneCategory != null) {
                finalId = oneCategory.getId();
            } else {
                BlogCategory createCategory = blogCategoryService.createCategory(categoryName, CategoryEnum.ARTICLE.getCode());
                finalId = createCategory.getId();
            }
        }
        return finalId;
    }

    /**
     *进行添加文章分类与标签关联的公共方法
     *
     * @param article_id
     * @param tagList
     * @return
     */
    public List<BlogArticleTag> createArticleTagByArticleId(Integer article_id, List<BlogTag> tagList){
        List<BlogArticleTag> articleTags = null;

        //// TODO: 2023-11-19 实现异步操作
        // 先将新增的tag进行保存，拿到tag的id，再进行标签 文章关联
        BlogTag res = null;
        ArrayList<BlogTag> promiseList = new ArrayList<>();
//        BlogTagServiceImpl blogTagService = new BlogTagServiceImpl();
        for (BlogTag blogTag : tagList){
            if (blogTag.getId() == null) {
                BlogTag oneTag = blogTagService.getOneTag(blogTag.getTag_name());
                if (oneTag != null) {
                    res = oneTag;
                }else {
                    res = blogTagService.createTag(blogTag.getTag_name());
                }
            }
            promiseList.add(res);
        }

        // 组装添加了标签id后的标签列表
        for(BlogTag blogTag : promiseList){
            if (blogTag != null) {
                for (int index = 0;index < tagList.size();index ++){
                    if (tagList.get(index).getTag_name().equals(blogTag.getTag_name())){
                        tagList.get(index).setId(blogTag.getId());
                    }
                }
            }
        }

        // 文章id和标签id 关联
        if (article_id != null) {
            ArrayList<BlogArticleTag> articleTagList = new ArrayList<>();
            for (BlogTag blogTag : tagList){
                BlogArticleTag articleTag = new BlogArticleTag();
                articleTag.setArticle_id(Integer.valueOf(article_id));
                articleTag.setTag_id(blogTag.getId());
                articleTagList.add(articleTag);
            }
            // 批量新增文章标签关联
//            BlogArticleTagServiceImpl blogArticleTagService = new BlogArticleTagServiceImpl();
            articleTags = blogArticleTagService.createArticleTags(articleTagList);
        }

        return articleTags;
    }
}




