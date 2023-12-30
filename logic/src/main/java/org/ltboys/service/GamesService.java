package org.ltboys.service;

import com.alibaba.fastjson.JSONObject;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.dto.ro.QueryGamesRo;
import org.springframework.stereotype.Service;

/**
 * @author kralarklite
 */
@Service
public interface GamesService {

    /**
     * 查询单个游戏信息及其tag信息
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject viewGame(IdRo ro) throws Exception;

    /**
     * 根据条件查询游戏列表（分页）
     * @param ro
     * @return
     * @throws Exception
     */
    JSONObject queryGames(QueryGamesRo ro) throws Exception;

    /**
     * 获取tag列表
     * @return
     * @throws Exception
     */
    JSONObject getTags() throws Exception;
}
