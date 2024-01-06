package org.ltboys.context.utils.AccessInvoker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.HttpURLConnection;
import java.net.URL;

import org.ltboys.context.utils.AesHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;

public class AccessInvoker {
	@Value("${sms.sendSite}")
	private String sendSite;

	@Value("${sms.cKey}")
	private String cKey;

	@Value("${sms.method}")
	private String method;

	@Value("${sms.cKey16}")
	private String cKey16;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String cKey= "sD9Gg7c3U7qBnByvLRkv052D";
		String cKey16 = "2SRhkP97d4AcPdA1";
		// 连接的应用信息
        Date date = new Date();
        InputModel input = new InputModel();
        input.setSendsite("ZJSys");
        input.setMethod("SMSService");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        input.setReqtime(df.format(date));
        input.setId(df.format(date));  // 这个ID保证系统内唯一，这里作为样例直接取了时间戳

		// 发送的订单数据
        JSONObject subInput = new JSONObject();
        subInput.put("send_site", "zjsw_web");
		subInput.put("tel_no", "15995833412");
		subInput.put("model_id", "1");
		subInput.put("model_param", "0190|");

        try{
			// 将请求内容转换为json字符串
        	String rqJson = JSON.toJSONString(subInput);
        	System.out.println(rqJson);
            // 加密加签
        	String strNeedSign = input.getSendsite() + input.getId() + input.getReqtime() + input.getMethod() + cKey;
            // 读取RSA密钥PKCS8格式
//        	String priKeyStr = "";
//        	FileInputStream fin = new FileInputStream("./pkcs8_private_key20210916.pem");
//        	InputStreamReader reader = new InputStreamReader(fin);
//        	BufferedReader br = new BufferedReader(reader);
//        	String line;
//        	while ((line= br.readLine())!=null) {
//        		// 注意去掉前后缀
//        		if(!line.contains("PRIVATE KEY-----")) {
//					priKeyStr += line;
//				}
//			}
//        	br.close();
//        	reader.close();
//        	fin.close();
        	
        	//获取签名
//        	RsaHelper rsaHelper = new RsaHelper();
//        	rsaHelper.setPriKeyStr(priKeyStr);
//        	String sign = rsaHelper.createSignWithPriKeyBySHA256(strNeedSign);
			String sign = DigestUtils.md5Hex(strNeedSign);
        	//加密报文
        	AesHelper aesHelper = new AesHelper();
        	aesHelper.setAesKey(cKey16);
        	String rqEncoded = aesHelper.encodeWithAES(rqJson);
        	//设置参数
        	input.setRq(rqEncoded);
        	input.setSignature(sign);
        	String inputData = JSON.toJSONString(input);
        	//让fiddler抓取便于调试
            //Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",8888));
        	//下面就是post请求了没有特别的，这里只做最简单的处理，请根据项目实际情况来写
        	URL url = new URL("https://app.szh.abchina.com/WcfServiceAccess/service1.svc/Access");
        	//HttpURLConnection conn = (HttpURLConnection)url.openConnection(proxy);
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
        		System.out.println("调用结果" + response.toString());
//        		OutputModel output = JSON.parseObject(response.toString(), OutputModel.class);
//        		if("0000".equals(output.getRetCode()))
//        		{
//        			String rString = output.getRs();
//        			rString = aesHelper.decodeWithAES(rString);
//        			System.out.println("调用成功接口返回" + rString);
//        		}
//        		else {
//        			System.out.println("调用失败" + output.getRetMsg());
//				}
        		in.close();
        	}
        	else{
        		System.out.println("调用失败" + conn.getResponseCode() + response.toString());
        	}
        	
        }
        catch(Exception ex){
        	ex.printStackTrace();
        }
        
        
        
	}

}
