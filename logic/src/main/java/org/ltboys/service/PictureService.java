package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface PictureService {

    JSONObject savePicture(String str) throws Exception;
}
