package org.ltboys.context.utils;

import org.ltboys.context.utils.AccessInvoker.InputModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
@Slf4j
@Service
public class SmsUtil {

    @Value("${sms.smsSendSite}")
    private String smsSendSite;

    @Value("${sms.sendSite}")
    private String sendSite;

    @Value("${sms.cKey}")
    private String cKey;

    @Value("${sms.cKey16}")
    private String cKey16;

    @Value("${sms.method}")
    private String method;

    @Value("${sms.apiUrl}")
    private String apiUrl;

    public Boolean send(String phone, String msg) {
        Date date = new Date();
        InputModel input = new InputModel();
        input.setSendsite(sendSite);
        input.setMethod(method);

        Integer randomMessageId = 1 + (int) (Math.random() * (999999999));
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        input.setReqtime(df.format(date));
        input.setId(df.format(date));  // 这个ID保证系统内唯一，这里作为样例直接取了时间戳

        // 发送的订单数据
        JSONObject subInput = new JSONObject();
        subInput.put("send_site", smsSendSite);
        subInput.put("tel_no", phone);
        subInput.put("model_id", "1");
        subInput.put("model_param", msg + "|");
        try {
            String rqJson = JSON.toJSONString(subInput);
            log.info("短信服务发送报文：", subInput.toJSONString());
            // 加密加签
            String strNeedSign = input.getSendsite() + input.getId() + input.getReqtime() + input.getMethod() + cKey;
            String sign = DigestUtils.md5Hex(strNeedSign);
            //加密报文
            AesHelper aesHelper = new AesHelper();
            aesHelper.setAesKey(cKey16);
            String rqEncoded = aesHelper.encodeWithAES(rqJson);
            //设置参数
            input.setRq(rqEncoded);
            input.setSignature(sign);
            String inputData = JSON.toJSONString(input);
            //下面就是post请求了没有特别的，这里只做最简单的处理，请根据项目实际情况来写
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");

            byte[] inputDataBin = inputData.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();

            os.write(inputDataBin, 0, inputDataBin.length);
            os.flush();
            os.close();

            StringBuffer response = new StringBuffer();

            if(200 == conn.getResponseCode()){
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String line =null;
                while((line=in.readLine())!=null){
                    response.append(line);
                }
                log.info("调用结果" + response.toString());
                in.close();
                return true;
            }
            else{
                log.error("调用失败" + conn.getResponseCode() + response.toString());
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
