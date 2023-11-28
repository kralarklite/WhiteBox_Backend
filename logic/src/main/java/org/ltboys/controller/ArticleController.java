package org.ltboys.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.action.ActionResult;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/information")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

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

}
