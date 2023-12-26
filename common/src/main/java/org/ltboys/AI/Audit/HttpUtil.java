/**
 * HttpUtil.java
 * com.prereadweb.utils
 * Copyright (c) 2019, 北京聚智未来科技有限公司版权所有.
 */
package org.ltboys.AI.Audit;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

    /**
     * http 工具类
     */
    public class HttpUtil {

        public static String doGet(String url, Map<String, String> param) {
            // 创建Httpclient对象
            CloseableHttpClient httpclient = HttpClients.createDefault();
            String resultString = "";
            CloseableHttpResponse response = null;
            try {
                // 创建uri
                URIBuilder builder = new URIBuilder(url);
                if (param != null) {
                    for (String key : param.keySet()) {
                        builder.addParameter(key, param.get(key));
                    }
                }
                URI uri = builder.build();

                // 创建http GET请求
                HttpGet httpGet = new HttpGet(uri);

                // 执行请求
                response = httpclient.execute(httpGet);
                // 判断返回状态是否为200
                if (response.getStatusLine().getStatusCode() == 200) {
                    resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return resultString;
        }

        public static String doGet(String url) {
            return doGet(url, null);
        }

        public static String doPost(String url, Map<String, String> param) {
            // 创建Httpclient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            String resultString = "";
            try {
                // 创建Http Post请求
                HttpPost httpPost = new HttpPost(url);
                // 创建参数列表
                if (param != null) {
                    List<NameValuePair> paramList = new ArrayList<>();
                    for (String key : param.keySet()) {
                        paramList.add(new BasicNameValuePair(key, param.get(key)));
                    }
                    // 模拟表单
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
                    httpPost.setEntity(entity);
                }
                // 执行http请求
                response = httpClient.execute(httpPost);
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return resultString;
        }

        public static String doPost(String url) {
            return doPost(url, null);
        }

        /**
         * 请求的参数类型为json
         * @param url
         * @param json
         * @return
         * {username:"",pass:""}
         */
        public static String doPostJson(String url, String json) {
            // 创建Httpclient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            String resultString = "";
            try {
                // 创建Http Post请求
                HttpPost httpPost = new HttpPost(url);
                // 创建请求内容
                StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
                // 执行http请求
                response = httpClient.execute(httpPost);
                resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return resultString;
        }
        public static String post(String requestUrl, String accessToken, String params)
                throws Exception {
            String contentType = "application/x-www-form-urlencoded";
            return HttpUtil.post(requestUrl, accessToken, contentType, params);
        }

        public static String post(String requestUrl, String accessToken, String contentType, String params)
                throws Exception {
            String encoding = "UTF-8";
            if (requestUrl.contains("nlp")) {
                encoding = "GBK";
            }
            return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
        }

        public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
                throws Exception {
            String url = requestUrl + "?access_token=" + accessToken;
            return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
        }

        public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
                throws Exception {
            URL url = new URL(generalUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 设置通用的请求属性
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // 得到请求的输出流对象
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(params.getBytes(encoding));
            out.flush();
            out.close();

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> headers = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : headers.keySet()) {
                System.err.println(key + "--->" + headers.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = null;
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), encoding));
            String result = "";
            String getLine;
            while ((getLine = in.readLine()) != null) {
                result += getLine;
            }
            in.close();
            System.err.println("result:" + result);
            return result;
        }
    }
