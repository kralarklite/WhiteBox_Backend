package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.ltboys.dto.ro.AddArticleRo;
import org.ltboys.dto.ro.AddCommentRo;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.dto.ro.QueryArticlesRo;
import org.springframework.stereotype.Service;

@Service
public interface ArticleService {

    /**
     * 浏览所有文章
     * @param ro
     * @return
     */
    JSONObject queryArticles(QueryArticlesRo ro);

    /**
     * 查看文章
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject viewArticle(IdRo ro) throws Exception;

    /**
     * 查看文章评论
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject queryComments(IdRo ro) throws Exception;

    /**
     * 用户上传文章
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject addArticle(AddArticleRo ro) throws Exception;

    /**
     * 用户上传评论
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject addComment(AddCommentRo ro) throws Exception;
}
