package com.xc.blogbackend.controller;

import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.model.domain.BlogArticle;
import com.xc.blogbackend.model.domain.BlogArticleTag;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.model.domain.BlogTag;
import com.xc.blogbackend.model.domain.request.AddArticleRequest;
import com.xc.blogbackend.model.domain.request.ArticleRequest;
import com.xc.blogbackend.model.domain.request.TitleExistRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogArticleService;
import com.xc.blogbackend.service.BlogArticleTagService;
import com.xc.blogbackend.service.BlogCategoryService;
import com.xc.blogbackend.service.BlogTagService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *文章接口
 *
 * @author 星尘
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private BlogArticleService blogArticleService;

    @Resource
    private BlogCategoryService blogCategoryService;

    @Resource
    private BlogTagService blogTagService;

    @Resource
    private BlogArticleTagService blogArticleTagService;

    /**
     * 条件分页获取文章
     *
     * @return
     */
    @PostMapping("/getArticleList")
    public BaseResponse<PageInfoResult<BlogArticle>> getArticleList(@RequestBody ArticleRequest articleRequest){
        PageInfoResult<BlogArticle> articleList = blogArticleService.getArticleList(articleRequest);

        return ResultUtils.success(articleList);
    }

    /**
     * 根据文章标题判断文章是否被注册过
     *
     * @param titleExistRequest
     * @return
     */
    @PostMapping("/titleExist")
    public BaseResponse<Boolean> getArticleInfoByTitle(@RequestBody TitleExistRequest titleExistRequest){
        String id = titleExistRequest.getId();
        String article_title = titleExistRequest.getArticle_title();
        Boolean articleInfoByTitle = blogArticleService.getArticleInfoByTitle(id, article_title);
        return ResultUtils.success(articleInfoByTitle);
    }

    /**
     * 新增文章
     *
     * @param addArticleRequest
     * @return
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<List<BlogArticleTag>> createArticle(@RequestBody AddArticleRequest addArticleRequest){
        try {
            AddArticleRequest.ArticleDtae finalArticle = addArticleRequest.getFinalArticle();
            List<BlogTag> tagList = finalArticle.getTagList();
            BlogCategory category = finalArticle.getCategory();
            BlogArticle articleRest = new BlogArticle();
            articleRest.setValues(finalArticle.getArticle_title(),
                                  finalArticle.getAuthor_id(),
                                  finalArticle.getArticle_content(),
                                  finalArticle.getArticle_cover(),
                                  finalArticle.getIs_top(),
                                  finalArticle.getArticle_order(),
                                  finalArticle.getStatus(),
                                  finalArticle.getType(),
                                  finalArticle.getOrigin_url(),
                                  finalArticle.getArticle_description());
            Integer id = category.getId();
            String category_name = category.getCategory_name();

            // 如果分类不存在，则先创建分类
//            ArticleCommon articleCommon = new ArticleCommon();
            String categoryOrReturn = createCategoryOrReturn(id, category_name);
            articleRest.setCategory_id(Integer.valueOf(categoryOrReturn));

            // 先创建文章 拿到文章的id
            BlogArticle newArticle = blogArticleService.createArticle(articleRest);

            // tag和标签进行关联
            String article_id = String.valueOf(newArticle.getId());
            List<BlogArticleTag> articleTagByArticleId =
                    createArticleTagByArticleId(article_id, tagList);

            return ResultUtils.success(articleTagByArticleId,"新增文章成功");
        } catch (Exception e) {
            throw new RuntimeException("新增文章失败");
        }
    }

    /**
     * 新增和编辑文章关于分类的公共方法
     *
     * @param id
     * @param category_name
     * @return
     */
    public String createCategoryOrReturn(Integer id,String category_name){
        Integer finalId;
        if (id != null) {
            finalId = id;
        } else {
//            BlogCategoryServiceImpl blogCategoryService = new BlogCategoryServiceImpl();
            BlogCategory oneCategory = blogCategoryService.getOneCategory(category_name);
            if (oneCategory != null) {
                finalId = oneCategory.getId();
            } else {
                BlogCategory createCategory = blogCategoryService.createCategory(category_name);
                finalId = createCategory.getId();
            }
        }
        return String.valueOf(finalId);
    }

    /**
     *进行添加文章分类与标签关联的公共方法
     *
     * @param article_id
     * @param tagList
     * @return
     */
    public List<BlogArticleTag> createArticleTagByArticleId(String article_id, List<BlogTag> tagList){
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
