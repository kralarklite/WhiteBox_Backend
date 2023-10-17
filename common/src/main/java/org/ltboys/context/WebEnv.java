package org.ltboys.context;

import java.util.HashMap;

public class WebEnv {

    private static final ThreadLocal<HashMap<String, Object>> ENV_MAP = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new HashMap<String, Object>();
        }
    };

    /**
     * 查询授权登录凭证
     * @return
     */
    public static String getOuthToken() {
        return ((String) ENV_MAP.get().get("outhToken"));
    }

    /**
     * 保存授权登录凭证
     * @param outhToken
     */
    public static void setOuthToken(String outhToken) {
        ENV_MAP.get().put("outhToken", outhToken);
    }
}
