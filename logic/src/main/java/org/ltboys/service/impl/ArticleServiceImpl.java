package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.mysql.entity.ArticleEntity;
import org.ltboys.mysql.mapper.ArticleMapper;
import org.ltboys.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

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
        updatePara.setViews(String.valueOf(Integer.parseInt(articleMapper.selectOne(articleEntityQueryWrapper).getViews())+1));
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
}
