package org.ltboys.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.action.ActionResult;
import org.ltboys.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kralarklite
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    /**
     * 把前端传来的图片存储并获得其url
     * @param ro
     * @return
     * @throws Exception
     */
    @PostMapping("/save")
    public ActionResult savePicture (@RequestParam("image") MultipartFile ro) throws Exception{

        try {
            JSONObject vo = pictureService.savePicture(ro);
            return ActionResult.success(vo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ActionResult.failure("失败");
        }
    }
}
