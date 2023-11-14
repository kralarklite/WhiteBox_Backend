package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.ltboys.dto.ro.RegisterRo;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    JSONObject register(RegisterRo ro) throws Exception;
}
