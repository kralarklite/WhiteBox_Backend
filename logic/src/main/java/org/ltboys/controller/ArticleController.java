package org.ltboys.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.action.ActionResult;
import org.ltboys.dto.ro.AddArticleRo;
import org.ltboys.dto.ro.AddCommentRo;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.dto.ro.QueryArticlesRo;
import org.ltboys.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author kralarklite
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/information")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 浏览所有文章
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/queryarticles")
    public ActionResult queryArticles(@RequestBody @Validated QueryArticlesRo ro) throws Exception {
        try {
            JSONObject vo = articleService.queryArticles(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询文章失败");
        }
    }
    /**
     * 查看文章
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/viewarticle")
    public ActionResult viewArticle(@RequestBody @Validated IdRo ro) throws Exception {
        try {
            JSONObject vo = articleService.viewArticle(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询文章失败");
        }
    }

    /**
     * 查看文章评论
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/querycomments")
    public ActionResult queryComments(@RequestBody @Validated IdRo ro) throws Exception {
        try {
            JSONObject vo = articleService.queryComments(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询文章失败");
        }
    }

    /**
     * 用户上传文章
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/addarticle")
    public ActionResult addArticle(@RequestHeader("token") @Validated String token,@RequestBody @Validated AddArticleRo ro) throws Exception {
        try {
            JSONObject vo = articleService.addArticle(token, ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询文章失败");
        }
    }

    /**
     * 用户上传评论
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/addcomment")
    public ActionResult addComment(@RequestHeader("token") @Validated String token,@RequestBody @Validated AddCommentRo ro) throws Exception {
        try {
            JSONObject vo = articleService.addComment(token, ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("查询文章失败");
        }
    }

}
