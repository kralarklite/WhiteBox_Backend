package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.dto.ro.IdRo;
import org.ltboys.dto.ro.QueryGamesRo;
import org.ltboys.mysql.entity.GameStatisticEntity;
import org.ltboys.mysql.entity.CollectMapEntity;
import org.ltboys.mysql.entity.GamesEntity;
import org.ltboys.mysql.entity.TagEntity;
import org.ltboys.mysql.entity.TagMapEntity;
import org.ltboys.mysql.mapper.*;
import org.ltboys.mysql.entity.*;
import org.ltboys.mysql.mapper.*;
import org.ltboys.service.GamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author kralarklite
 */
@Slf4j
@Service
public class GamesServiceImpl implements GamesService {

    @Autowired
    private GamesMapper gamesMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagMapMapper tagMapMapper;

    @Autowired
    private GameStatisticMapper gameStatisticMapper;

    @Autowired
    private CollectMapMapper collectMapMapper;

    @Autowired
    private RateGameMapper rateGameMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject viewGame(IdRo ro) throws Exception{
        JSONObject retJson = new JSONObject();

        QueryWrapper<GamesEntity> gamesEntityQueryWrapper = new QueryWrapper<>();
        gamesEntityQueryWrapper
                .eq("id",ro.getId())
                .eq("flag",1);
        gamesEntityQueryWrapper
                .select("id","name","cover","head","`desc`","publisher","score","release_time");
        List<Map<String , Object>> viewGameList = gamesMapper.selectMaps(gamesEntityQueryWrapper);
        //List<GamesEntity> gamesEntityList = gamesMapper.selectList(gamesEntityQueryWrapper);

        if (viewGameList.size()!=1){
            retJson.put("retCode","9000");
            retJson.put("retMsg","game数据异常");
            return retJson;
        }

        gameStatistic(ro.getId());

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

        //查询近期热门游戏
        if (ro.isNeedHotGame()){
            QueryWrapper<GameStatisticEntity> gameStatisticEntityQueryWrapper = new QueryWrapper<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -7);
            gameStatisticEntityQueryWrapper
                    //.ge("stat_time",calendar)
                    .apply("DATE(stat_time) >= {0}", LocalDate.now().minusWeeks(1))
                    .eq("flag", 1)
                    .select("SUM(total) as total","game_id")
                    .groupBy("game_id")
                    .orderByDesc("total")
                    .last("LIMIT 20");
            List<Map<String,Object>> gameStatisticEntityList = gameStatisticMapper.selectMaps(gameStatisticEntityQueryWrapper);
            if (gameStatisticEntityList.size()!=0) {
                List<Integer> gameIdList = gameStatisticEntityList.stream().map(map -> (Integer)map.get("game_id")).collect(Collectors.toList());
                //List<GamesEntity> gamesEntityList = gamesMapper.selectBatchIds(gameIdList);
                gamesEntityQueryWrapper
                        .in("id", gameIdList)
                        .orderBy(true, true, "RAND()")
                        .last("LIMIT " + ro.getLimit());
                List<GamesEntity> gamesEntityList = gamesMapper.selectList(gamesEntityQueryWrapper);
                retJson.put("GameList",gamesEntityList);
                if (ro.isNeedTag()){
                    List<List<TagEntity>> gameTagList = new ArrayList<>();
                    for (GamesEntity gamesEntity : gamesEntityList){
                        gameTagList.add(viewTag(gamesEntity.getId()));
                    }
                    retJson.put("tagList",gameTagList);
                }
                return retJson;
            }

        }


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
        if (limit!=0){
            int page = ro.getPage();
            int offset = limit * (page - 1);
            String limit_sql = "LIMIT " + limit + " OFFSET " + offset;
            gamesEntityQueryWrapper.last(limit_sql);
        }

        //随机排序还是按照id排序
        if (ro.isNeedRand()) {
            gamesEntityQueryWrapper.orderBy(true, true, "RAND()");
        } else {
            gamesEntityQueryWrapper.orderByAsc("id");
        }

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



    @Override
    public JSONObject recommendGames(IdRo ro) throws Exception{
        JSONObject retJson = new JSONObject();
        QueryWrapper<RateGameEntity> collectMapEntityQueryWrapper = new QueryWrapper<>();
        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();

        // Assuming `ro` contains the user_id you want to query
        collectMapEntityQueryWrapper
                .eq("user_id", ro.getId());


        try {
            // Assuming collectMapEntityMapper is your MyBatis or other ORM mapper for CollectMapEntity
            List<RateGameEntity> userCollectedGamesList = rateGameMapper.selectList(collectMapEntityQueryWrapper);

            Map<Integer,Double> userRateMap = new HashMap<>();

            for (RateGameEntity rateGameEntity : userCollectedGamesList) {
                userRateMap.put(rateGameEntity.getGameId(), rateGameEntity.getRate());
            }

            List<Integer> userIdList = userMapper.selectList(userEntityQueryWrapper.select("id"))
                    .stream()
                    .map(UserEntity::getId)
                    .collect(Collectors.toList());
            Double max_similarity = 0.0;
            Integer recom_userid = 0;
            Map<Integer,Double> max_sim = new HashMap<>();
            max_sim.put(recom_userid, max_similarity);
            for (Integer userid : userIdList){
                if (userid.equals(ro.getId())){
                    continue;
                }

                QueryWrapper<RateGameEntity> othercollectMapEntityQueryWrapper = new QueryWrapper<>();
                othercollectMapEntityQueryWrapper
                        .eq("user_id", userid);
                List<RateGameEntity> otheruserCollectedGamesList = rateGameMapper.selectList(othercollectMapEntityQueryWrapper);
                Map<Integer,Double> otheruserRateMap = new HashMap<>();
                for (RateGameEntity othercollectMapEntity : otheruserCollectedGamesList) {
                    otheruserRateMap.put(othercollectMapEntity.getGameId(),othercollectMapEntity.getRate());
                }
                double similarity = calculatePearsonSimilarity(userRateMap,otheruserRateMap);
                if (similarity > max_similarity){
                    max_similarity = similarity;
                    max_sim.put(userid, similarity);
                }
            }
            // After finding the user with maximum similarity
            Integer maxSimilarityUserId = max_sim.keySet().iterator().next();
            Map<Integer, Double> maxSimilarityUserRateMap = new HashMap<>();

            QueryWrapper<RateGameEntity> maxSimilarityUserCollectMapQueryWrapper = new QueryWrapper<>();
            maxSimilarityUserCollectMapQueryWrapper.eq("user_id", maxSimilarityUserId);

            List<RateGameEntity> maxSimilarityUserCollectedGamesList = rateGameMapper.selectList(maxSimilarityUserCollectMapQueryWrapper);

            for (RateGameEntity maxSimilarityUserRateGameEntity : maxSimilarityUserCollectedGamesList) {
                maxSimilarityUserRateMap.put(maxSimilarityUserRateGameEntity.getGameId(), maxSimilarityUserRateGameEntity.getRate());
            }

// Find the recommended gameId based on the difference between maxSimilarityUserRateMap and userRateMap
            Set<Integer> gameIdsInMaxSimilarityUser = maxSimilarityUserRateMap.keySet();
            Set<Integer> gameIdsInUser = userRateMap.keySet();

// Find gameIds in maxSimilarityUserRateMap but not in userRateMap
            Set<Integer> gameIdsNotInUser = new HashSet<>(gameIdsInMaxSimilarityUser);
            gameIdsNotInUser.removeAll(gameIdsInUser);

// Output the recommended gameId
            if (!gameIdsNotInUser.isEmpty()) {
                Integer recommendedGameId = gameIdsNotInUser.iterator().next();
                retJson.put("recommendedGameId", recommendedGameId);
            } else {
                retJson.put("recommendedGameId", null);
            }

        } catch (Exception e) {
            // Handle the exception appropriately (logging, rethrowing, etc.)
            throw new Exception("Error while querying user_collected_games: " + e.getMessage());
        }

        return retJson;

    }

    private double calculatePearsonSimilarity(Map<Integer,Double> user1, Map<Integer,Double> user2) {
        List<Integer> commonItems = new ArrayList<>();

        for (Entry<Integer, Double> entry : user1.entrySet()) {
            Integer itemId = entry.getKey();
            if (user2.containsKey(itemId)) {
                commonItems.add(itemId);
            }
        }

        if (commonItems.isEmpty()) {
            // No common items, similarity is undefined
            return 0.0;
        }

        double sumX = 0.0;
        double sumY = 0.0;
        double sumX_Sq = 0.0;
        double sumY_Sq = 0.0;
        double sumXY = 0.0;
        int N = commonItems.size();

        for (Integer itemId : commonItems) {
            double ratingUser1 = user1.get(itemId);
            double ratingUser2 = user2.get(itemId);

            sumX += ratingUser1;
            sumY += ratingUser2;
            sumX_Sq += Math.pow(ratingUser1, 2);
            sumY_Sq += Math.pow(ratingUser2, 2);
            sumXY += ratingUser1 * ratingUser2;
        }

        double numerator = sumXY - (sumX * sumY / N);
        double denominator = Math.sqrt((sumX_Sq - Math.pow(sumX, 2) / N) * (sumY_Sq - Math.pow(sumY, 2) / N));

        // Avoid division by zero
        if (denominator == 0) {
            return 0.0;
        }

        return numerator / denominator;
    }


    //每次调用此方法时使游戏近期访问量+1
    public void gameStatistic(int gameId) {
        QueryWrapper<GameStatisticEntity> gameStatisticEntityQueryWrapper = new QueryWrapper<>();
        gameStatisticEntityQueryWrapper
                .eq("game_id", gameId)
                .apply("DATE(stat_time) = {0}", LocalDate.now());

        int fact;
        if (gameStatisticMapper.exists(gameStatisticEntityQueryWrapper)) {
            GameStatisticEntity gameStatisticEntity = gameStatisticMapper.selectOne(gameStatisticEntityQueryWrapper);
            GameStatisticEntity updatePara = new GameStatisticEntity();
            updatePara.setTotal(gameStatisticEntity.getTotal()+1);
            fact = gameStatisticMapper.update(updatePara,gameStatisticEntityQueryWrapper);
        } else {
            GameStatisticEntity gameStatisticEntity = new GameStatisticEntity();
            gameStatisticEntity.setGameId(gameId);
            gameStatisticEntity.setStatTime(new Date());
            gameStatisticEntity.setTotal(1);
            gameStatisticEntity.setFlag(1);
            fact = gameStatisticMapper.insert(gameStatisticEntity);
        }

        if (fact != 1) System.out.println("游戏数据统计异常");
    }
}
