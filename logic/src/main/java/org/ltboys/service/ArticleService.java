package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.ltboys.dto.ro.IdRo;
import org.springframework.stereotype.Service;

@Service
public interface ArticleService {

    /**
     * 查看文章
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject viewArticle(IdRo ro) throws Exception;
}
