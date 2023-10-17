package org.ltboys.context.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 环信IM工具类
 */
@Slf4j
@Service
public class EasemobUtil {

    /**
     * 环信用户注册模型类
     */
    @Data
    public static class EasemobUserEntity {
        /**
         * 环信用户名
         */
        private String username;
        /**
         * 环信登录密码
         */
        private String password;
        /**
         * 环信用户昵称
         */
        private String nickname;
    }

    @Value("${easemob.baseUrl}")
    private String baseUrl;

    @Value("${easemob.orgId}")
    private String orgId;

    @Value("${easemob.appName}")
    private String appName;

    @Value("${easemob.accessToken}")
    private String accessToken;

    @Autowired
    private HttpUtil httpUtil;

    /**
     * 环信注册用户
     * @param userInfo 用户信息
     * @return
     * @throws Exception
     */
    public JSONObject registerUser(EasemobUserEntity userInfo) throws Exception{
        String url = baseUrl + "/" + orgId + "/" + appName + "/users";
        log.info("================环信IM用户注册服务=============");
        log.info(url + "|" + JSONObject.toJSONString(userInfo));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        return httpUtil.post(url, JSONObject.toJSONString(userInfo), headers);
    }
}
