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
        
	}

