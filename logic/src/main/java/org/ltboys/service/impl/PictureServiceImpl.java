package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
//import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.ltboys.context.utils.FileUtil;
import org.ltboys.service.PictureService;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import sun.text.resources.FormatData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Arrays;

import static org.springframework.http.HttpRequest.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;



/**
 * @author kralarklite
 */
@Slf4j
@Service
public class PictureServiceImpl  implements PictureService {
//    @Override
//    public JSONObject savePicture(MultipartFile ro) throws Exception {
//        JSONObject retJson = new JSONObject();
////        System.out.println(ro.getContentType());
////        System.out.println(ro.getSize());
////        System.out.println(Arrays.toString(ro.getBytes()));
//        retJson.put("url", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fbao%2Fuploaded%2Fi3%2F653957414%2FO1CN01Ls8fjX24dextfTpse_%21%210-item_pic.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1701331298&t=985b0ee776d0a6394d969e6d6cdb030f");
//        return retJson;
//    }
//}
    @Override
    public JSONObject savePicture(MultipartFile ro) throws Exception{
        String apitoken = "rr7kN3H7YTL21ADvYI2NAyAtbKoROOLP";
        String url = "https://smms.app/api/v2/upload";

        try {
            JSONObject retJson = new JSONObject();

            InputStream inputStream = ro.getInputStream();
            FileOutputStream file = new FileOutputStream("../../../../../../../../saved.jpg");
            int j;
            while ((j = inputStream.read()) != -1){
                file.write(j);
            }
            inputStream.close();
            file.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add("Authorization", apitoken);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");



            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("smfile", new org.springframework.core.io.FileSystemResource("../../../../../../../../saved.jpg"));

            HttpEntity requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();

            // 使用泛型指定 Response 的类型为 String
            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, requestEntity, JSONObject.class);

//             解析响应
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                JSONObject responseJson = new JSONObject(responseEntity.getBody());
                String imageUrl = responseJson.getJSONObject("data").getString("url");
                retJson.put("url", imageUrl);
            } else {
                retJson.put("error", "Failed to upload the image");
            }
            //删除本地文件
            File img_file = new File("../../../../../../../../saved.jpg");
            if (img_file.isFile() && img_file.exists()){
                img_file.delete();
            }

            return retJson;

        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }


    }

}
