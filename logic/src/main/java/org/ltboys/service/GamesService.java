package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface GamesService {

    JSONObject viewGame(int id) throws Exception;
}
