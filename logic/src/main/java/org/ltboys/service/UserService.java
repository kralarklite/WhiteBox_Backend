package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.ltboys.dto.ro.*;
import org.springframework.stereotype.Service;

/**
 * @author kralarklite
 */
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

    /**
     * 查询用户名称、头像、性别
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject brief(IdRo ro) throws Exception;

    /**
     * 更改用户信息
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject updateUser(String token, UpdateUserRo ro) throws Exception;

    /**
     * 用户添加收藏游戏
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject addCollect(String token, UserCollectRo ro) throws Exception;

    /**
     * 用户删除收藏游戏
     * @param token
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject deleteCollect(String token, UserCollectRo ro) throws Exception;

    /**
     * 查询个人收藏
     * @param token
     * @return
     * @throws Exception
     */
    JSONObject myCollects(String token) throws Exception;

    JSONObject rateGame(String token, RateGameRo ro) throws Exception;

    JSONObject viewGameUser(UserCollectRo ro) throws Exception;
}
