package org.ltboys.context.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 通过Gateway发送报文的工具类
 */
@Slf4j
@Service
public class GatewayUtil {

    @Autowired
    HttpUtil httpUtil;
    private Character[] arr = new Character[] {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 65, 66, 67, 68, 69, 70, 71, 72, 73,
            74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102,
            103, 104, 105, 106, 107, 108, 109, 110, 111,112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
    public JSONObject post(String url, String data) throws Exception{
        String randomKey = getRandomKey();
        String tmp = new StringBuffer(randomKey).reverse().toString();
        AesHelper helper = new AesHelper();
        helper.setAesKey(tmp);
        String body = helper.encodeWithAES(data);
        JSONObject request = new JSONObject();
        request.put("body", body);
        Map<String, String> header = new HashMap<>();
        header.put("signuture", randomKey);
        log.info("GatewayUtil:发送的报文为" + request.toJSONString() + "；发送地址为：" + url +"；签名：" + randomKey);
        return httpUtil.post(url, request.toJSONString(), header);
    }

    private String getRandomKey() {
        Arrays.sort(arr, new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return  Math.random() > 0.5? -1 : 1;
            }
        });

        StringBuffer sb = new StringBuffer();

        for(Character c: arr) {
            sb.append(c);
        }
        int randomStart = (int) Math.random() * 50;
        String randomKey = sb.toString().substring(randomStart, randomStart + 12) + getTimeStamps();
        return randomKey;
    }

    private String getTimeStamps() {
        String curr = String.valueOf(System.currentTimeMillis());
        return curr.substring(curr.length() - 4);
    }
}
