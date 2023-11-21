package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.dto.ro.QueryGamesRo;
import org.ltboys.mysql.entity.GamesEntity;
import org.ltboys.mysql.entity.TagEntity;
import org.ltboys.mysql.entity.TagMapEntity;
import org.ltboys.mysql.mapper.GamesMapper;
import org.ltboys.mysql.mapper.TagMapMapper;
import org.ltboys.mysql.mapper.TagMapper;
import org.ltboys.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GamesServiceImpl implements GamesService {

    @Autowired
    private GamesMapper gamesMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagMapMapper tagMapMapper;

    @Override
    public JSONObject viewGame(IdRo ro) throws Exception{
        JSONObject retJson = new JSONObject();

        QueryWrapper<GamesEntity> gamesEntityQueryWrapper = new QueryWrapper<>();
        gamesEntityQueryWrapper
                .eq("id",ro.getId())
                .eq("flag",1);
        gamesEntityQueryWrapper
                .select("id","name","cover","`desc`","publisher","score","release_time");
        List<Map<String , Object>> viewGameList = gamesMapper.selectMaps(gamesEntityQueryWrapper);
        //List<GamesEntity> gamesEntityList = gamesMapper.selectList(gamesEntityQueryWrapper);

        if (viewGameList.size()!=1){
            retJson.put("retCode","9000");
            retJson.put("retMsg","game数据异常");
            return retJson;
        }

        QueryWrapper<TagMapEntity> tagMapEntityQueryWrapper = new QueryWrapper<>();
        tagMapEntityQueryWrapper
                .eq("game_id",ro.getId())
                .eq("flag",1);
        List<TagMapEntity> tagMapEntityList = tagMapMapper.selectList(tagMapEntityQueryWrapper);
        if (tagMapEntityList.size()!=0){
            //提取tagMapEntityList中tagId字段作为一个新列表
            List<Integer> tagIdList = tagMapEntityList.stream().map(TagMapEntity::getTagId).collect(Collectors.toList());
            //根据tagId列表批量查询
            List<TagEntity> tagEntityList = tagMapper.selectBatchIds(tagIdList);
            retJson.put("Tags",tagEntityList);
        } else {
            retJson.put("Tags",new ArrayList<>());
        }

        retJson.put("Game",viewGameList);

        return retJson;
    }

    @Override
    public JSONObject queryGames(QueryGamesRo ro) throws Exception {
        JSONObject retJson = new JSONObject();

        QueryWrapper<GamesEntity> gamesEntityQueryWrapper = new QueryWrapper<>();
        QueryWrapper<TagMapEntity> tagMapEntityQueryWrapper = new QueryWrapper<>();

        gamesEntityQueryWrapper.eq("flag",1);

        //根据游戏名查询
        if (!Objects.equals(ro.getName(), "")){
            gamesEntityQueryWrapper.like("name",ro.getName());
        }

        //筛选条件：release_time
        if (!Objects.equals(ro.getReleaseTime(),"")){
            String start = ro.getReleaseTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = sdf.parse(start);
            gamesEntityQueryWrapper.ge("release_time",startTime);
        }

        //根据tag查询
        Integer tagId = ro.getTagId();
        if (!Objects.equals(tagId, 0)){
            tagMapEntityQueryWrapper
                    .eq("tag_id",tagId)
                    .eq("flag",1);
            if (tagMapMapper.exists(tagMapEntityQueryWrapper)){
                List<Integer> gameIdList = tagMapMapper.selectList(tagMapEntityQueryWrapper).stream().map(TagMapEntity::getGameId).collect(Collectors.toList());
                gamesEntityQueryWrapper.in("id",gameIdList);
            } else {
                retJson.put("retCode","9001");
                retJson.put("retMsg","该标签无游戏");
                return retJson;
            }
        }
/*
        List<Integer> tagList = ro.getTagList();
        if (tagList.size()!=0){
            List<Integer> gameIdList = new ArrayList<>();
            for (Integer tagId : tagList){
                //String tag_sql = "LEFT JOIN of_shop_members t1 ON (t1.id = t.id) ";
                tagMapEntityQueryWrapper.eq("","");
            }
        }
*/
        //分页查询
        int limit = ro.getLimit();
        int page = ro.getPage();
        int offset = limit * (page - 1);
        String limit_sql = "LIMIT " + limit + " OFFSET " + offset;
        gamesEntityQueryWrapper.last(limit_sql);

        gamesEntityQueryWrapper.orderByDesc("id");

        List<GamesEntity> gamesEntityList = gamesMapper.selectList(gamesEntityQueryWrapper);

        if (gamesEntityList.size()>=1){
            //如果需要查询游戏的tag，调用viewTag(Integer id)查询
            if (ro.isNeedTag()){
                List<List<TagEntity>> gameTagList = new ArrayList<>();
                for (GamesEntity gamesEntity : gamesEntityList){
                    gameTagList.add(viewTag(gamesEntity.getId()));
                }
                retJson.put("tagList",gameTagList);
            }
            retJson.put("GameList",gamesEntityList);
        }

        return retJson;
    }


    //根据游戏id查询其tag
    public List<TagEntity> viewTag(Integer id) {
        QueryWrapper<TagMapEntity> tagMapEntityQueryWrapper = new QueryWrapper<>();
        tagMapEntityQueryWrapper
                .eq("game_id",id)
                .eq("flag",1);
        List<TagMapEntity> tagMapEntityList = tagMapMapper.selectList(tagMapEntityQueryWrapper);
        if (tagMapEntityList.size()!=0){
            //提取tagMapEntityList中tagId字段作为一个新列表
            List<Integer> tagIdList = tagMapEntityList.stream().map(TagMapEntity::getTagId).collect(Collectors.toList());
            //根据tagId列表批量查询
            return tagMapper.selectBatchIds(tagIdList);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public JSONObject getTags() throws Exception {
        JSONObject retJson = new JSONObject();
        QueryWrapper<TagEntity> tagEntityQueryWrapper = new QueryWrapper<>();
        List<TagEntity> tagEntityList = tagMapper.selectList(tagEntityQueryWrapper);
        retJson.put("TagList",tagEntityList);
        return retJson;
    }
}
