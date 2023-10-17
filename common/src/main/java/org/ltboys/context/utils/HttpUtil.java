package org.ltboys.context.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * http通信工具类
 */
@Slf4j
@Service
public class HttpUtil {

    /**
     * post方法
     * @param url 请求地址
     * @param data 请求报文
     * @throws Exception 通信异常
     * @return
     */
    public JSONObject post(String url, String data, Map<String, String> headers) throws Exception{
        // (1) 创建POST请求对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // (2) 设置请求参数
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }
        httpPost.setHeader("Content-Type", "application/json");

        // (3) 设置请求报文
        StringEntity stringEntity = new StringEntity(data);
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = null;
        response = httpClient.execute(httpPost);
        // (4) 读取响应报文
        if (response.getStatusLine().getStatusCode() == 200) {
            // (4.1) 请求成功时，读取报文并返回
            String content = EntityUtils.toString(response.getEntity(), "utf-8");
            return JSONObject.parseObject(content);
        } else {
            // (4.2) 请求失败时，抛出异常
            String content = EntityUtils.toString(response.getEntity());
            log.error("HTTP POST 请求异常：" + content);
            throw new RuntimeException("HTTP POST 请求失败");
        }

    }
}
