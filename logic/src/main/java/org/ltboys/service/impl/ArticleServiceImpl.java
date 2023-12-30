package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.AI.Audit.BaiduAuditUtils;
import org.ltboys.dto.ro.AddArticleRo;
import org.ltboys.dto.ro.AddCommentRo;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.dto.ro.QueryArticlesRo;
import org.ltboys.mysql.entity.ArticleEntity;
import org.ltboys.mysql.entity.CommentEntity;
import org.ltboys.mysql.mapper.ArticleMapper;
import org.ltboys.mysql.mapper.CommentMapper;
import org.ltboys.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author kralarklite
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;


    @Override
    public JSONObject queryArticles(QueryArticlesRo ro) {
        JSONObject retJson = new JSONObject();

        QueryWrapper<ArticleEntity> articleEntityQueryWrapper = new QueryWrapper<>();

        if (ro.getClassification()==1 || ro.getClassification()==2 || ro.getClassification()==3){
            articleEntityQueryWrapper.eq("classification",ro.getClassification());
        } else if (ro.getClassification()!=0) {
            retJson.put("retCode","9302");
            retJson.put("retMsg","文章分类不存在");
            return retJson;
        }

        List<ArticleEntity> articleEntityList = articleMapper.selectList(articleEntityQueryWrapper);
        retJson.put("articleEntityList",articleEntityList);
        return retJson;
    }


    @Override
    public JSONObject viewArticle(IdRo ro) throws Exception {
        JSONObject retJson = new JSONObject();

        QueryWrapper<ArticleEntity> articleEntityQueryWrapper = new QueryWrapper<>();
        articleEntityQueryWrapper.eq("article_id",ro.getId());

        if (!articleMapper.exists(articleEntityQueryWrapper)) {
            retJson.put("retCode","9301");
            retJson.put("retMsg","文章不存在");
            return retJson;
        }

        //文章访问量+1
        ArticleEntity updatePara = new ArticleEntity();
        updatePara.setViews(articleMapper.selectOne(articleEntityQueryWrapper).getViews()+1);
        int fact = articleMapper.update(updatePara,articleEntityQueryWrapper);
        if(fact != 1){
            retJson.put("retCode","9300");
            retJson.put("retMsg","error");
            return retJson;
        }

        ArticleEntity articleEntity = articleMapper.selectOne(articleEntityQueryWrapper);
        retJson.put("articleEntity",articleEntity);
        return retJson;
    }


    @Override
    public JSONObject queryComments(IdRo ro) throws Exception {
        JSONObject retJson = new JSONObject();
        QueryWrapper<CommentEntity> commentEntityQueryWrapper = new QueryWrapper<>();
        commentEntityQueryWrapper.eq("article_id",ro.getId());
        List<CommentEntity> commentEntityList = commentMapper.selectList(commentEntityQueryWrapper);
        retJson.put("commentEntityList",commentEntityList);
        return retJson;
    }

    @Override
    public JSONObject addArticle(AddArticleRo ro) throws Exception {
        JSONObject retJson = new JSONObject();

        //判断是否违规
        if (judgement(retJson, BaiduAuditUtils.TextCensor(ro.getTitle()))) {
            retJson.replace("retMsg","文章标题"+retJson.get("retMsg"));
            return retJson;
        }
        if (judgement(retJson, BaiduAuditUtils.TextCensor(ro.getContent()))) {
            retJson.replace("retMsg","文章内容"+retJson.get("retMsg"));
            return retJson;
        }

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setUserId(ro.getUserId());
        articleEntity.setTitle(ro.getTitle());
        articleEntity.setContent(ro.getContent());
        articleEntity.setPicture(ro.getPicture());
        articleEntity.setClassification(ro.getClassification());
        articleEntity.setGameId(ro.getGameId());
        articleEntity.setReleaseTime(new Date());

        int fact = articleMapper.insert(articleEntity);

        if (fact!=1){
            retJson.put("retCode","9300");
            retJson.put("retMsg","error");
            return retJson;
        }

        retJson.put("retCode","0000");
        retJson.put("retMsg","文章发布成功");
        return retJson;
    }


    @Override
    public JSONObject addComment(AddCommentRo ro) throws Exception {
        JSONObject retJson = new JSONObject();

        //判断是否违规
        List<Object> list = BaiduAuditUtils.TextCensor(ro.getCommentContent());
        if (judgement(retJson, list)) return retJson;

//        Boolean flag = BaiduAuditUtils.TextCensor(ro.getCommentContent());
//        if (!flag) {
//            retJson.put("retCode","9401");
//            retJson.put("retMsg","评论违规");
//            return retJson;
//        }

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUserId(ro.getUserId());
        commentEntity.setArticleId(ro.getArticleId());
        commentEntity.setCommentContent(ro.getCommentContent());
        commentEntity.setCommentDate(new Date());

        int fact = commentMapper.insert(commentEntity);

        if (fact!=1){
            retJson.put("retCode","9400");
            retJson.put("retMsg","评论失败");
            return retJson;
        }

        retJson.put("retCode","0000");
        retJson.put("retMsg","评论成功");
        return retJson;
    }


    //审核判断
    private static boolean judgement(JSONObject retJson, List<Object> list) {
        if (list.size()==0){
            retJson.put("retCode","9400");
            retJson.put("retMsg","评论失败");
            return true;
        } else if (!(Boolean)list.get(0) && list.size()==1) {
            retJson.put("retCode","9400");
            retJson.put("retMsg","评论失败");
            return true;
        } else if (!(Boolean)list.get(0) && list.size()==2) {
            retJson.put("retCode","9401");
            try {
                JSONObject jsonObject = (JSONObject) list.get(1);
                retJson.put("retMsg",jsonObject.getString("msg"));
                retJson.put("wordHitPositions",jsonObject.getJSONArray("hits").getJSONObject(0).getJSONArray("wordHitPositions"));
                return true;
            } catch (Exception e) {
                retJson.put("retMsg","评论违规");
                return true;
            }
        }
        return false;
    }

}
