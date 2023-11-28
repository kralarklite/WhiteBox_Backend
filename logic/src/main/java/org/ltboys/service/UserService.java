package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.ltboys.dto.ro.LoginRo;
import org.ltboys.dto.ro.RegisterRo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * 用户注册
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject register(RegisterRo ro) throws Exception;

    /**
     * 用户登录
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject login(LoginRo ro) throws Exception;

    /**
     * 个人主页
     * @param token
     * @return
     * @throws Exception
     */
    JSONObject homepage(String token) throws Exception;

    /**
     * 查询个人文章
     * @param token
     * @return
     * @throws Exception
     */
    JSONObject myArticles(String token) throws Exception;

    /**
     * 查询个人评论
     * @param token
     * @return
     * @throws Exception
     */
    JSONObject myComments(String token) throws Exception;
}
