package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.ltboys.dto.ro.QueryGamesRo;
import org.springframework.stereotype.Service;

@Service
public interface GamesService {

    JSONObject viewGame(int id) throws Exception;

    JSONObject queryGames(QueryGamesRo ro) throws Exception;
}
