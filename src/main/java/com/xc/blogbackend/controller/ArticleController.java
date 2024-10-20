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
import com.xc.blogbackend.model.domain.result.ArticleListByContent;
import com.xc.blogbackend.model.domain.result.PageInfoResult;
import com.xc.blogbackend.model.domain.result.RecommendResult;
import com.xc.blogbackend.service.BlogArticleService;
import com.xc.blogbackend.service.BlogArticleTagService;
import com.xc.blogbackend.service.BlogCategoryService;
import com.xc.blogbackend.service.BlogTagService;
import com.xc.blogbackend.utils.ImageLinkComparator;
import com.xc.blogbackend.utils.PaddingUtils;
import com.xc.blogbackend.utils.Qiniu;
import com.xc.blogbackend.utils.StringManipulation;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.*;

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
    @ApiOperation(value = "根据id获取文章信息")
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
    @ApiOperation(value = "根据条件分页获取文章")
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
    @ApiOperation(value = "根据标题获取文章是否已经存在")
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
    @ApiOperation(value = "公开或隐藏文章")
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
    @ApiOperation(value = "恢复文章")
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
    @ApiOperation(value = "根据id删除文章")
    @DeleteMapping("/delete/{id}/{status}")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> deleteArticle(@PathVariable Integer id,@PathVariable Integer status) throws QiniuException {
        if (status == 3) {
            // 删除七牛云文章封面图片
            String oldCover = blogArticleService.getArticleCoverById(id);
            if (oldCover != null){
                String subString = StringManipulation.subString(oldCover);
                qiniu.deleteFile(subString);
            }else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            // 删除七牛云文章内容中的图片
            String mdImg = blogArticleService.getMdImgList(id);
            if (mdImg != null){
                // 将字符串转换成字符串数组
                String[] linksArray = mdImg.substring(1, mdImg.length() - 1).split(", "); // 去除首尾的方括号并按逗号和空格分割
                List<String> mdImgList = Arrays.asList(linksArray);
                // 截取图片链接的key
                for (int i = 0; i < mdImgList.size(); i++) {
                    String v = mdImgList.get(i);
                    String subString = StringManipulation.subString(v); // 假设这个方法用于截取字符串的操作
                    mdImgList.set(i, subString); // 更新原始数组中的元素
                }
                // 删除七牛云图片
                qiniu.deleteFile(mdImgList);
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
    @ApiOperation(value = "编辑更新文章")
    @PutMapping("/update")
    @Transactional(rollbackFor = Exception.class)  //Spring 的事务管理，如果发生异常，会自动回滚事务
    public BaseResponse<Boolean> updateArticle(@RequestBody UpdateArticleRequest request){
        UpdateArticleRequest.ArticleDate requestArticle = request.getArticle();
        List<BlogTag> tagList = requestArticle.getTagList();
        BlogCategory category = requestArticle.getCategory();

        BlogArticle articleRest = PaddingUtils.mapToBlogArticle(request);
        Integer article_id = articleRest.getId();
        String article_title = articleRest.getArticle_title();
        //根据传入文章id获取数据库中old文章信息
        BlogArticle oldArticle = blogArticleService.getArticle(article_id);

        // 监测文章内容中图片链接的变化，以此删除七牛云图片
        List<String> oldMdImgList = new ArrayList<>();
        List<String> newMdImgList = new ArrayList<>();
        String oldMdImg = oldArticle.getMdImgList();
        if (oldMdImg != null) {
            // 将字符串转换成字符串数组
            String[] linksArray = oldMdImg.substring(1, oldMdImg.length() - 1).split(", "); // 去除首尾的方括号并按逗号和空格分割
            oldMdImgList = Arrays.asList(linksArray);
            newMdImgList = requestArticle.getMdImgList();
            List<String> missingInOldList = ImageLinkComparator.findMissingElements(newMdImgList, oldMdImgList);
            // 截取图片链接的key
            for (int i = 0; i < missingInOldList.size(); i++) {
                String v = missingInOldList.get(i);
                String subString = StringManipulation.subString(v); // 这个方法用于截取字符串的操作
                missingInOldList.set(i, subString); // 更新原始数组中的元素
            }
            // 删除七牛云图片
            qiniu.deleteFile(missingInOldList);
        }

        // 如果封面图片改变，删除旧的七牛云文章封面图片
        String oldArticle_cover = oldArticle.getArticle_cover();
        if (oldArticle_cover != null && !oldArticle_cover.equals(articleRest.getArticle_cover())) {
            String imgKey = StringManipulation.subString(oldArticle_cover);
            qiniu.deleteFile(imgKey);
        }

        // 判断文章标题是否改变，改变则更新七牛云和数据库图片链接
        String oldArticle_title = oldArticle.getArticle_title();
        if (!oldArticle_title.equals(article_title)){
            // 更新数据库文章内容中图片链接
            String newArticle_content = articleRest.getArticle_content();
            String replaceContent = newArticle_content.replace("/article/" + oldArticle_title, "/article/" + article_title);
            articleRest.setArticle_content(replaceContent);
            // 更新数据库文章内容中图片列表链接
            String mdImgList = articleRest.getMdImgList();
            if (mdImgList != null){
                String replaceMdImgList = mdImgList.replace("/article/" + oldArticle_title, "/article/" + article_title);
                articleRest.setMdImgList(replaceMdImgList);
            }
            // 更新七牛云中文章内容图片链接
            if (oldMdImg != null){
                List<String> commonElements = ImageLinkComparator.findCommonElements(newMdImgList, oldMdImgList);
                for (String commonElement : commonElements) {
                    String newMdImg = commonElement.replace(oldArticle_title, article_title);
                    String newKey = StringManipulation.subString(newMdImg);
                    String oldKey = StringManipulation.subString(commonElement);
                    qiniu.renameImgKey(oldKey, newKey);
                }
            }
            // 更新数据库和七牛云文章封面图片链接
            if (Objects.equals(oldArticle_cover, articleRest.getArticle_cover())) {
                assert oldArticle_cover != null;
                String newArticle_cover = oldArticle_cover.replace(oldArticle_title, article_title);
                articleRest.setArticle_cover(newArticle_cover);
                String newKey = StringManipulation.subString(newArticle_cover);
                String oldKey = StringManipulation.subString(oldArticle_cover);
                qiniu.renameImgKey(oldKey,newKey);
            }
        }

        // 根据文章标题获取文章信息 校验是否可以新增或编辑文章
        Boolean byTitle = blogArticleService.getArticleInfoByTitle(article_id, article_title);
        if (byTitle){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"已存在相同的文章标题");
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
    @ApiOperation(value = "新增文章")
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
            String mdImgList = String.valueOf(finalArticle.getMdImgList());
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
                                  finalArticle.getArticle_description(),
                                  mdImgList);
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
     * 修改文章置顶状态
     *
     * @param id
     * @param is_top
     * @return
     */
    @ApiOperation(value = "修改文章置顶状态")
    @PutMapping("/updateTop/{id}/{is_top}")
    public BaseResponse<Boolean> updateTop(@PathVariable Integer id,@PathVariable Integer is_top){
        Boolean aBoolean = blogArticleService.updateTop(id, is_top);

        return ResultUtils.success(aBoolean,"修改文章置顶状态成功");
    }

    /**
     * 前台
     * 分页获取文章 按照置顶和发布时间倒序排序
     *
     * @param current 页码
     * @param size  每页数据量
     * @return
     */
    @ApiOperation(value = "前台 分页获取文章列表")
    @GetMapping("/blogHomeGetArticleList/{current}/{size}")
    public BaseResponse<PageInfoResult<BlogArticle>> blogHomeGetArticleList(@PathVariable Integer current,
                                                                            @PathVariable  Integer size){
        PageInfoResult<BlogArticle> pageInfoResult = blogArticleService.blogHomeGetArticleList(current, size);

        return ResultUtils.success(pageInfoResult,"获取文章列表成功");
    }

    /**
     * 根据文章获取上下一篇文章 和推荐文章
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据文章获取上下一篇文章 和推荐文章")
    @GetMapping("/getRecommendArticleById/{id}")
    public BaseResponse<RecommendResult> getRecommendArticleById(@PathVariable Integer id){
        if(id == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        RecommendResult recommendArticleById = blogArticleService.getRecommendArticleById(id);
        return ResultUtils.success(recommendArticleById,"获取推荐文章成功");
    }

    /**
     * 分页获取时间轴信息
     *
     * @param current
     * @param size
     * @return
     */
    @ApiOperation(value = "分页获取时间轴信息")
    @GetMapping("/blogTimelineGetArticleList/{current}/{size}")
    public BaseResponse<PageInfoResult<BlogArticle>> blogTimelineGetArticleList(@PathVariable Integer current, @PathVariable Integer size){
        PageInfoResult<BlogArticle> result = blogArticleService.blogTimelineGetArticleList(current, size);

        return ResultUtils.success(result,"获取文章时间轴列表成功");
    }

    /**
     * 前台
     * 分页获取该分类下文章的简略信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "前台 分页获取该分类下文章的简略信息")
    @PostMapping("/getArticleListByCategoryId")
    public BaseResponse<PageInfoResult<BlogArticle>> getArticleListByCategoryId(@RequestBody Map<String,Integer> request){
        Integer id = request.get("id");
        Integer current = request.get("current");
        Integer size = request.get("size");
        if (id == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        PageInfoResult<BlogArticle> byCategoryId = blogArticleService.getArticleListByCategoryId(current, size, id);

        return ResultUtils.success(byCategoryId,"根据分类获取文章列表成功");
    }

    /**
     * 前台
     * 分页获取该标签下文章的简略信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "前台 分页获取该标签下文章的简略信息")
    @PostMapping("/getArticleListByTagId")
    public BaseResponse<PageInfoResult<BlogArticle>> getArticleListByTagId(@RequestBody Map<String,Integer> request){
        Integer id = request.get("id");
        Integer current = request.get("current");
        Integer size = request.get("size");

        if(id == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        PageInfoResult<BlogArticle> articleListByTagId = blogArticleService.getArticleListByTagId(current, size, id);

        return ResultUtils.success(articleListByTagId,"根据分类获取文章列表成功");
    }

    /**
     * 前台
     * 获取热门文章
     *
     * @return
     */
    @ApiOperation(value = "前台 获取热门文章")
    @GetMapping("/getHotArticle")
    public BaseResponse<List<BlogArticle>> getHotArticle(){
        List<BlogArticle> hotArticle = blogArticleService.getHotArticle();
        return ResultUtils.success(hotArticle,"获取热门文章成功");
    }

    /**
     * 前台
     * 全局搜索文章
     *
     * @param content
     * @return
     */
    @ApiOperation(value = "前台 全局搜索文章")
    @GetMapping("/getArticleListByContent/{content}")
    public BaseResponse<List<ArticleListByContent>> getArticleListByContent(@PathVariable String content){
        List<ArticleListByContent> articleListByContent = blogArticleService.getArticleListByContent(content);
        return ResultUtils.success(articleListByContent,"按照内容搜索文章成功");
    }

    /**
     * 文章点赞
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "文章点赞")
    @PutMapping("/like/{id}")
    public BaseResponse<Boolean> articleLike(@PathVariable Integer id){
        Boolean aBoolean = blogArticleService.articleLike(id);
        return ResultUtils.success(aBoolean,"点赞成功");
    }

    /**
     * 文章取消点赞
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "取消文章点赞")
    @PutMapping("/cancelLike/{id}")
    public BaseResponse<Boolean> cancelArticleLike(@PathVariable Integer id){
        Boolean aBoolean = blogArticleService.cancelArticleLike(id);
        return ResultUtils.success(aBoolean,"取消点赞成功");
    }

    /**
     * 增加文章阅读时长 毫秒
     *
     * @param id
     * @param duration
     * @return
     */
    @ApiOperation(value = "增加文章阅读时长")
    @PutMapping("/addReadingDuration/{id}/{duration}")
    public BaseResponse<Boolean> addReadingDuration(@PathVariable Integer id,@PathVariable Integer duration){
        Boolean aBoolean = blogArticleService.addReadingDuration(id, duration);
        return ResultUtils.success(aBoolean,"增加阅读时长成功");
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
