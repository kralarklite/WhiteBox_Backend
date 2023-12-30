package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @author kralarklite
 */
@Service
public interface PictureService {

    JSONObject savePicture(String str) throws Exception;
}
