package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.ltboys.dto.ro.QueryGamesRo;
import org.ltboys.mysql.entity.GamesEntity;
import org.ltboys.mysql.entity.TagMapEntity;
import org.ltboys.mysql.mapper.GamesMapper;
import org.ltboys.mysql.mapper.TagMapMapper;
import org.ltboys.mysql.mapper.TagMapper;
import org.ltboys.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class GamesServiceImpl  implements GamesService {

    @Autowired
    private GamesMapper gamesMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagMapMapper tagMapMapper;

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

    @Override
    public JSONObject queryGames(QueryGamesRo ro) throws Exception {
        JSONObject retJson = new JSONObject();

        QueryWrapper<GamesEntity> gamesEntityQueryWrapper = new QueryWrapper<>();
        QueryWrapper<TagMapEntity> tagMapEntityQueryWrapper = new QueryWrapper<>();

        if (!Objects.equals(ro.getName(), "")){
            gamesEntityQueryWrapper.eq("name",ro.getName());
        }

        if (!Objects.equals(ro.getReleaseTime(),"")){
            String start = ro.getReleaseTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = sdf.parse(start);
            gamesEntityQueryWrapper.ge("release_time",startTime);
        }

        List<Integer> tagList = ro.getTagList();
        if (tagList.size()!=0){
            List<Integer> gameIdList = new ArrayList<>();
            for (Integer tagId : tagList){
                String tag_sql = "LEFT JOIN of_shop_members t1 ON (t1.id = t.id) ";
                tagMapEntityQueryWrapper.eq("","");
            }
        }

        int limit = ro.getLimit();
        int page = ro.getPage();
        int offset = limit * (page - 1);
        String limitsql = "LIMIT " + limit + " OFFSET " + offset;
        gamesEntityQueryWrapper.last(limitsql);

        gamesEntityQueryWrapper.orderByDesc("id");

        List<GamesEntity> gamesEntityList = gamesMapper.selectList(gamesEntityQueryWrapper);
        if (gamesEntityList.size()>=1){
            retJson.put("GameList",gamesEntityList);
        }

        return retJson;
    }
}
