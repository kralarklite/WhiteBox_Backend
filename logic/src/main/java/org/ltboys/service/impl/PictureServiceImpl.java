package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.dto.ro.PictureRo;
import org.ltboys.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * @author kralarklite
 */
@Slf4j
@Service
public class PictureServiceImpl  implements PictureService {
    @Override
    public JSONObject savePicture(MultipartFile ro) throws Exception {
        JSONObject retJson = new JSONObject();
        //System.out.println(ro.getContentType());
        //System.out.println(ro.getSize());
        //System.out.println(Arrays.toString(ro.getBytes()));
        retJson.put("url","https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.alicdn.com%2Fbao%2Fuploaded%2Fi3%2F653957414%2FO1CN01Ls8fjX24dextfTpse_%21%210-item_pic.jpg&refer=http%3A%2F%2Fimg.alicdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1701331298&t=985b0ee776d0a6394d969e6d6cdb030f");
        return retJson;
    }
}
