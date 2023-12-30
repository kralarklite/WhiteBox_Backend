package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.ltboys.dto.ro.PictureRo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kralarklite
 */
@Service
public interface PictureService {

    JSONObject savePicture(MultipartFile ro) throws Exception;
}
