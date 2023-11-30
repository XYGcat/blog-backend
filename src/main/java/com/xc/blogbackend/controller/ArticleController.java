package com.xc.blogbackend.controller;

import com.qiniu.common.QiniuException;
import com.xc.blogbackend.common.BaseResponse;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.common.ResultUtils;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.model.domain.BlogArticle;
import com.xc.blogbackend.model.domain.BlogArticleTag;
import com.xc.blogbackend.model.domain.BlogCategory;
import com.xc.blogbackend.model.domain.BlogTag;
import com.xc.blogbackend.model.domain.request.AddArticleRequest;
import com.xc.blogbackend.model.domain.request.ArticleRequest;
import com.xc.blogbackend.model.domain.request.TitleExistRequest;
import com.xc.blogbackend.model.domain.request.UpdateArticleRequest;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.service.BlogArticleService;
import com.xc.blogbackend.service.BlogArticleTagService;
import com.xc.blogbackend.service.BlogCategoryService;
import com.xc.blogbackend.service.BlogTagService;
import com.xc.blogbackend.utils.PaddingUtils;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @Resource
    private Qiniu qiniu;

    /**
     * 根据id获取文章信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getArticleById/{id}")
    public BaseResponse<BlogArticle> getArticleById(@PathVariable Integer id){
        BlogArticle articleById = blogArticleService.getArticleById(id);
        return ResultUtils.success(articleById,"查询文章详情成功");
    }

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
     * 根据标题获取文章是否已经存在
     *
     * @param titleExistRequest
     * @return
     */
    @PostMapping("/titleExist")
    public BaseResponse<Boolean> getArticleInfoByTitle(@RequestBody TitleExistRequest titleExistRequest){
        Integer id = titleExistRequest.getId();
        String article_title = titleExistRequest.getArticle_title();
        Boolean articleInfoByTitle = blogArticleService.getArticleInfoByTitle(id, article_title);
        return ResultUtils.success(articleInfoByTitle,"文章查询结果");
    }

    /**
     * 公开或隐藏文章
     *
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/isPublic/{id}/{status}")
    public BaseResponse<Boolean> toggleArticlePublic(@PathVariable Integer id,@PathVariable Integer status){
        String message;
        Boolean aBoolean = blogArticleService.toggleArticlePublic(id, status);
        if (status == 1) {
            message = "隐藏文章";
        }else {
            message = "公开文章";
        }

        return ResultUtils.success(aBoolean,message + "成功");
    }

    /**
     * 恢复文章
     *
     * @param id
     * @return
     */
    @PutMapping("/revert/{id}")
    public BaseResponse<Boolean> revertArticle(@PathVariable Integer id){
        Boolean aBoolean = blogArticleService.revertArticle(id);
        return ResultUtils.success(aBoolean,"恢复文章成功");
    }

    /**
     * 通过id删除文章  传文章状态 1、2会假删除 3会真删除
     * 3状态下直接删除 其他状态回退
     *
     * @param id
     * @param status
     * @return
     */
    @DeleteMapping("/delete/{id}/{status}")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> deleteArticle(@PathVariable Integer id,@PathVariable Integer status) throws QiniuException {
        if (status == 3) {
            String oldCover = blogArticleService.getArticleCoverById(id);
            if (oldCover != null){
                String subString = StringManipulation.subString(oldCover);
                Boolean aBoolean = qiniu.deleteFile(subString);
            }else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }

        }
        Boolean aBoolean = blogArticleService.deleteArticle(id, status);
        return ResultUtils.success(aBoolean,"删除文章成功");
    }

    /**
     * 编辑更新文章
     *
     * @param request
     * @return
     */
    @PutMapping("/update")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> updateArticle(@RequestBody UpdateArticleRequest request){
        UpdateArticleRequest.ArticleDate requestArticle = request.getArticle();
        List<BlogTag> tagList = requestArticle.getTagList();
        BlogCategory category = requestArticle.getCategory();
        BlogArticle articleRest = PaddingUtils.mapToBlogArticle(request);
        Integer article_id = articleRest.getId();
        String article_title = articleRest.getArticle_title();

        Boolean byTitle = blogArticleService.getArticleInfoByTitle(article_id, article_title);
        if (byTitle){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"已存在相同的文章标题");
        }

        String oldCover = blogArticleService.getArticleCoverById(article_id);
        // 服务器删除图片
        if (oldCover != null && !oldCover.equals(articleRest.getArticle_cover())) {
            String imgKey = StringManipulation.subString(oldCover);
            qiniu.deleteFile(imgKey);
        }

        //// TODO: 2023-11-27 修改逻辑，如果标签没变，不修改，如果标签改变，重新生成
        // 先删除这个文章与标签之前的关联
        blogArticleTagService.deleteArticleTag(articleRest.getId());
        // 判断新的分类是新增的还是已经存在的 并且返回分类id
        Integer categoryOrReturn = createCategoryOrReturn(category.getId(),category.getCategory_name());
        articleRest.setCategory_id(categoryOrReturn);

        List<BlogArticleTag> newArticleTagList = createArticleTagByArticleId(articleRest.getId(), tagList);

        Boolean res = blogArticleService.updateArticle(articleRest);

        return ResultUtils.success(res,"修改文章成功");
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
            AddArticleRequest.ArticleDate finalArticle = addArticleRequest.getFinalArticle();

            String article_title = finalArticle.getArticle_title();
            Integer articleId = finalArticle.getId();
            Boolean byTitle = blogArticleService.getArticleInfoByTitle(articleId, article_title);
            if (byTitle){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"已存在相同的文章标题");
            }

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
            Integer categoryOrReturn = createCategoryOrReturn(id, category_name);
            articleRest.setCategory_id(categoryOrReturn);

            // 先创建文章 拿到文章的id
            BlogArticle newArticle = blogArticleService.createArticle(articleRest);

            // tag和标签进行关联
            Integer article_id = newArticle.getId();
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
    public Integer createCategoryOrReturn(Integer id,String category_name){
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
