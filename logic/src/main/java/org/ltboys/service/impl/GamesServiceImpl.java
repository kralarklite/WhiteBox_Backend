package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.mysql.entity.GamesEntity;
import org.ltboys.mysql.mapper.GamesMapper;
import org.ltboys.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GamesServiceImpl  implements GamesService {

    @Autowired
    private GamesMapper gamesMapper;

    @Override
    public JSONObject viewGame(int id) throws Exception{
        JSONObject retJson = new JSONObject();

        QueryWrapper<GamesEntity> gamesEntityQueryWrapper = new QueryWrapper<>();
        gamesEntityQueryWrapper.eq("id",id);
        List<GamesEntity> gamesEntityList = gamesMapper.selectList(gamesEntityQueryWrapper);
        if (gamesEntityList.size()==1){
            retJson.put("GameList",gamesEntityList);
        }

        return retJson;
    }
}
