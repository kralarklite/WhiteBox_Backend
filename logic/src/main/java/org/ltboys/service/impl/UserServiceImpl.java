package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.aop.exception.TokenException;
import org.ltboys.context.utils.JwtUtil;
import org.ltboys.dto.ro.*;
import org.ltboys.dto.vo.UserBriefVo;
import org.ltboys.mysql.entity.*;
import org.ltboys.mysql.mapper.*;
import org.ltboys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.ltboys.context.utils.JwtUtil.checkUserId;
import static org.ltboys.context.utils.JwtUtil.getUserId;

/**
 * @author kralarklite
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CollectMapMapper collectMapMapper;

    @Autowired
    private RateGameMapper rateGameMapper;

    @Autowired
    private GamesMapper gamesMapper;

    @Override
    public JSONObject register(RegisterRo ro) throws Exception {

        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("account",ro.getAccount());
        if (userMapper.exists(userEntityQueryWrapper)){
            retJson.put("retCode","9901");
            retJson.put("retMsg","用户名重复");
            return retJson;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setAccount(ro.getAccount());
        userEntity.setPassword(ro.getPassword());
        userEntity.setUserName(ro.getUserName());
        userEntity.setCreatedAt(new Date());
        int fact = userMapper.insert(userEntity);

        if(fact != 1){
            retJson.put("retCode","9906");
            retJson.put("retMsg","注册失败");
            return retJson;
        }

        retJson.put("retCode","0000");
        retJson.put("retMsg","注册成功");
        return retJson;
    }

    @Override
    public JSONObject login(LoginRo ro) throws Exception {

        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();

        //查询是否存在账号
        userEntityQueryWrapper.eq("account",ro.getAccount());
        if (!userMapper.exists(userEntityQueryWrapper)){
            retJson.put("retCode","9902");
            retJson.put("retMsg","用户不存在");
            return retJson;
        }

        //查询密码是否正确
        userEntityQueryWrapper.eq("password",ro.getPassword());
        if (!userMapper.exists(userEntityQueryWrapper)){
            retJson.put("retCode","9903");
            retJson.put("retMsg","密码错误");
            return retJson;
        }

        UserEntity userEntity = userMapper.selectOne(userEntityQueryWrapper);

        //查询账号状态
        if (userEntity.getFlag()!=1){
            //判断是否解封
            if (userEntity.getBanUntil().before(new Date())) {
                UserEntity updatePara = new UserEntity();
                updatePara.setFlag(1);
                int fact = userMapper.update(updatePara,userEntityQueryWrapper);
                if(fact != 1){
                    retJson.put("retCode","9905");
                    retJson.put("retMsg","登录失败");
                    return retJson;
                }
            } else {
                retJson.put("retCode","9904");
                retJson.put("retMsg","喔唷，崩溃啦！显示账号的时候出现了点问题，可能是账号已封禁");
                retJson.put("limit",userEntity.getBanUntil());
                return retJson;
            }
        }

        //更新登录时间
        UserEntity updatePara = new UserEntity();
        updatePara.setLastLoginAt(new Date());
        int fact = userMapper.update(updatePara,userEntityQueryWrapper);
        if(fact != 1){
            retJson.put("retCode","9905");
            retJson.put("retMsg","登录失败");
            return retJson;
        }

        JSONObject info = new JSONObject();
        info.put("username", userEntity.getUserName());
        info.put("password", "******");
        info.put("account",userEntity.getAccount());

        //发放token
        String token = JwtUtil.sign(userEntity.getId().toString(),info.toString());
        retJson.put("retCode","0000");
        retJson.put("retMsg","登录成功");
        retJson.put("userId",userEntity.getId());
        retJson.put("token",token);
        retJson.put("recommended",userEntity.getRecommended());
        return retJson;
    }

    @Override
    public JSONObject homepage(String token) throws Exception {

        //String id = getUserId(token);
        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("id",getUserId(token));

        //查询账号是否存在
        if (!userMapper.exists(userEntityQueryWrapper)){
            retJson.put("retCode","9902");
            retJson.put("retMsg","用户不存在");
            return retJson;
        }

        //查询账号封禁状态
        userEntityQueryWrapper.eq("flag", 1);
        if (!userMapper.exists(userEntityQueryWrapper)) {
            retJson.put("retCode","9904");
            retJson.put("retMsg","喔唷，崩溃啦！显示账号的时候出现了点问题，可能是账号已封禁");
            return retJson;
        }

        userEntityQueryWrapper.select("user_name","icon","sex","profile","birthday","phone_number");

        List<Map<String , Object>> mapList = userMapper.selectMaps(userEntityQueryWrapper);
        retJson.put("info",mapList);


        return retJson;
    }

    @Override
    public JSONObject myArticles(String token) throws Exception {

        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("id",getUserId(token));

        //查询账号是否存在
        if (!userMapper.exists(userEntityQueryWrapper)){
            retJson.put("retCode","9902");
            retJson.put("retMsg","用户不存在");
            return retJson;
        }

        //查询账号封禁状态
        userEntityQueryWrapper.eq("flag", 1);
        if (!userMapper.exists(userEntityQueryWrapper)) {
            retJson.put("retCode","9904");
            retJson.put("retMsg","喔唷，崩溃啦！显示账号的时候出现了点问题，可能是账号已封禁");
            return retJson;
        }

        QueryWrapper<ArticleEntity> articleEntityQueryWrapper = new QueryWrapper<>();
        articleEntityQueryWrapper
                .eq("user_id",getUserId(token))
                .eq("flag",1);
        List<ArticleEntity> articleEntityList = articleMapper.selectList(articleEntityQueryWrapper);
        retJson.put("articles",articleEntityList);
        return retJson;
    }

    @Override
    public JSONObject myComments(String token) throws Exception {

        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("id",getUserId(token));

        //查询账号是否存在
        if (!userMapper.exists(userEntityQueryWrapper)){
            retJson.put("retCode","9902");
            retJson.put("retMsg","用户不存在");
            return retJson;
        }

        //查询账号封禁状态
        userEntityQueryWrapper.eq("flag", 1);
        if (!userMapper.exists(userEntityQueryWrapper)) {
            retJson.put("retCode","9904");
            retJson.put("retMsg","喔唷，崩溃啦！显示账号的时候出现了点问题，可能是账号已封禁");
            return retJson;
        }

        QueryWrapper<CommentEntity> commentEntityQueryWrapper = new QueryWrapper<>();
        commentEntityQueryWrapper
                .eq("user_id",getUserId(token))
                .eq("flag",1);
        List<CommentEntity> commentEntityList = commentMapper.selectList(commentEntityQueryWrapper);
        retJson.put("comments",commentEntityList);
        return retJson;
    }

    @Override
    public JSONObject brief(IdRo ro) throws Exception {

        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper
                .eq("id", ro.getId())
                .select("user_name","icon","sex");

        //List<Object> objectList = userMapper.selectObjs(userEntityQueryWrapper);

        //如果selectOne查询的目标不存在会将null赋给userEntity，导致在调用其get方法时报NullPointerException，因此使用selectList
//        UserEntity userEntity = userMapper.selectOne(userEntityQueryWrapper);
//        UserBriefVo vo = new UserBriefVo();
//        try {
//            vo.setUserName(userEntity.getUserName());
//            vo.setIcon(userEntity.getIcon());
//            vo.setSex(userEntity.getSex());
//        } catch (NullPointerException nullPointerException) {
//            retJson.put("retCode","9902");
//            retJson.put("retMsg","用户不存在");
//            return retJson;
//        }
        List<UserEntity> userEntityList = userMapper.selectList(userEntityQueryWrapper);
        if (userEntityList.size()==0) {
            retJson.put("retCode","9902");
            retJson.put("retMsg","用户不存在");
            return retJson;
        }
        UserEntity userEntity = userEntityList.get(0);
        UserBriefVo vo = new UserBriefVo();
        vo.setUserName(userEntity.getUserName());
        vo.setIcon(userEntity.getIcon());
        vo.setSex(userEntity.getSex());

        retJson.put("user",vo);

        return retJson;
    }

    @Override
    public JSONObject updateUser(String token, UpdateUserRo ro) throws Exception {

        if (!checkUserId(token, String.valueOf(ro.getUserId()))) throw new TokenException("code3:非法操作！请重新登录！");

        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("id",ro.getUserId())
                .eq("flag",1);

        if (!userMapper.exists(userEntityQueryWrapper)) {
            retJson.put("retCode","9900");
            retJson.put("retMsg","用户状态异常");
            return retJson;
        }

        UserEntity updatePara = new UserEntity();
        updatePara.setUserName(ro.getUserName());
        updatePara.setIcon(ro.getIcon());
        updatePara.setSex(ro.getSex());
        updatePara.setProfile(ro.getProfile());
        updatePara.setBirthday(ro.getBirthday());
        updatePara.setPhoneNumber(ro.getPhoneNumber());

        int fact = userMapper.update(updatePara,userEntityQueryWrapper);
        if (fact!=1) {
            retJson.put("retCode","9907");
            retJson.put("retMsg","修改用户信息失败");
            return retJson;
        }

        retJson.put("retCode","0000");
        retJson.put("retMsg","修改用户信息成功");
        return retJson;
    }

    @Override
    public JSONObject addCollect(String token, UserCollectRo ro) throws Exception {

        if (!checkUserId(token, String.valueOf(ro.getUserId()))) throw new TokenException("code3:非法操作！请重新登录！");

        JSONObject retJson = new JSONObject();

        QueryWrapper<CollectMapEntity> collectMapEntityQueryWrapper = new QueryWrapper<>();
        collectMapEntityQueryWrapper
                .eq("user_id", ro.getUserId())
                .eq("game_id", ro.getGameId());

        List<CollectMapEntity> collectMapEntityList = collectMapMapper.selectList(collectMapEntityQueryWrapper);
        if (collectMapEntityList.size()!=0) {
            CollectMapEntity collectMapEntity = collectMapEntityList.get(0);
            if (collectMapEntity.getFlag()==1) {
                retJson.put("retCode", "9912");
                retJson.put("retMsg", "收藏失败，用户已收藏此游戏");
            } else {
                CollectMapEntity updatePara = new CollectMapEntity();
                updatePara.setFlag(1);
                updatePara.setUpdatedAt(new Date());
                int fact = collectMapMapper.update(updatePara, collectMapEntityQueryWrapper);
                if (fact != 1) {
                    retJson.put("retCode", "9910");
                    retJson.put("retMsg", "收藏失败，数据修改异常");
                } else {
                    retJson.put("retCode", "0000");
                    retJson.put("retMsg", "收藏成功");
                }
            }
            return retJson;
        } else {
            CollectMapEntity collectMapEntity = new CollectMapEntity();
            collectMapEntity.setUserId(ro.getUserId());
            collectMapEntity.setGameId(ro.getGameId());
            collectMapEntity.setFlag(1);
            int fact = collectMapMapper.insert(collectMapEntity);
            if (fact != 1) {
                retJson.put("retCode", "9910");
                retJson.put("retMsg", "收藏失败，数据修改异常");
            } else {
                retJson.put("retCode", "0000");
                retJson.put("retMsg", "收藏成功");
            }
            return retJson;
        }
    }

    @Override
    public JSONObject deleteCollect(String token, UserCollectRo ro) throws Exception {

        if (!checkUserId(token, String.valueOf(ro.getUserId()))) throw new TokenException("code3:非法操作！请重新登录！");

        JSONObject retJson = new JSONObject();

        QueryWrapper<CollectMapEntity> collectMapEntityQueryWrapper = new QueryWrapper<>();
        collectMapEntityQueryWrapper
                .eq("user_id", ro.getUserId())
                .eq("game_id", ro.getGameId())
                .eq("flag", 1);
        if (!collectMapMapper.exists(collectMapEntityQueryWrapper)) {
            retJson.put("retCode", "9911");
            retJson.put("retMsg", "删除失败，无此用户收藏此游戏的信息");
            return retJson;
        }

        CollectMapEntity updatePara = new CollectMapEntity();
        updatePara.setFlag(0);
        updatePara.setUpdatedAt(new Date());
        int fact = collectMapMapper.update(updatePara, collectMapEntityQueryWrapper);
        if (fact != 1) {
            retJson.put("retCode", "9910");
            retJson.put("retMsg", "删除失败，数据修改异常");
            return retJson;
        }

        retJson.put("retCode", "0000");
        retJson.put("retMsg", "删除收藏成功");
        return retJson;
    }

    @Override
    public JSONObject myCollects(String token) throws Exception {

        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("id",getUserId(token));

        //查询账号是否存在
        if (!userMapper.exists(userEntityQueryWrapper)){
            retJson.put("retCode","9902");
            retJson.put("retMsg","用户不存在");
            return retJson;
        }

        //查询账号封禁状态
        userEntityQueryWrapper.eq("flag", 1);
        if (!userMapper.exists(userEntityQueryWrapper)) {
            retJson.put("retCode","9904");
            retJson.put("retMsg","喔唷，崩溃啦！显示账号的时候出现了点问题，可能是账号已封禁");
            return retJson;
        }

        QueryWrapper<CollectMapEntity> collectMapEntityQueryWrapper = new QueryWrapper<>();
        collectMapEntityQueryWrapper
                .eq("user_id",getUserId(token))
                .eq("flag",1);
        List<CollectMapEntity> collectMapEntityList = collectMapMapper.selectList(collectMapEntityQueryWrapper);
        List<Integer> gameIdList = collectMapEntityList.stream().map(CollectMapEntity::getGameId).collect(Collectors.toList());
        if (gameIdList.size()==0) {
            retJson.put("retCode","9908");
            retJson.put("retMsg","该用户无收藏");
            return retJson;
        }
        List<GamesEntity> gamesEntityList = gamesMapper.selectBatchIds(gameIdList);
        retJson.put("gamesEntityList",gamesEntityList);
        retJson.put("retCode", "0000");
        retJson.put("retMsg", "查询成功");
        return retJson;
    }

    @Override
    public JSONObject rateGame(String token, RateGameRo ro) throws Exception {

        if (!checkUserId(token, String.valueOf(ro.getUserId()))) throw new TokenException("code3:非法操作！请重新登录！");

        JSONObject retJson = new JSONObject();

        if (ro.getRate()<1 || ro.getRate()>5) {
            retJson.put("retCode", "9921");
            retJson.put("retMsg", "分数异常");
            return retJson;
        }

        QueryWrapper<RateGameEntity> rateGameEntityQueryWrapper = new QueryWrapper<>();
        rateGameEntityQueryWrapper
                .eq("user_id", ro.getUserId())
                .eq("game_id", ro.getGameId());

        List<RateGameEntity> rateGameEntityList = rateGameMapper.selectList(rateGameEntityQueryWrapper);
        if (rateGameEntityList.size() == 0) {
            RateGameEntity rateGameEntity = new RateGameEntity();
            rateGameEntity.setRate(ro.getRate());
            rateGameEntity.setUserId(ro.getUserId());
            rateGameEntity.setGameId(ro.getGameId());
            int fact = rateGameMapper.insert(rateGameEntity);
            if (fact != 1) {
                retJson.put("retCode", "9920");
                retJson.put("retMsg", "评分失败，数据修改异常");
            } else {
                retJson.put("retCode", "0000");
                retJson.put("retMsg", "评分成功");
            }
            return retJson;
        } else {
            RateGameEntity updatePara = new RateGameEntity();
            updatePara.setRate(ro.getRate());
            updatePara.setUpdatedAt(new Date());
            int fact = rateGameMapper.update(updatePara, rateGameEntityQueryWrapper);
            if (fact != 1) {
                retJson.put("retCode", "9920");
                retJson.put("retMsg", "评分失败，数据修改异常");
            } else {
                retJson.put("retCode", "0000");
                retJson.put("retMsg", "修改评分成功");
            }
            return retJson;
        }
    }

    @Override
    public JSONObject viewGameUser(UserCollectRo ro) throws Exception {

        JSONObject retJson = new JSONObject();

        QueryWrapper<RateGameEntity> rateGameEntityQueryWrapper = new QueryWrapper<>();
        QueryWrapper<CollectMapEntity> collectMapEntityQueryWrapper = new QueryWrapper<>();

        rateGameEntityQueryWrapper
                .eq("user_id", ro.getUserId())
                .eq("game_id", ro.getGameId());

        collectMapEntityQueryWrapper
                .eq("user_id", ro.getUserId())
                .eq("game_id", ro.getGameId());

        List<RateGameEntity> rateGameEntityList = rateGameMapper.selectList(rateGameEntityQueryWrapper);
        List<CollectMapEntity> collectMapEntityList = collectMapMapper.selectList(collectMapEntityQueryWrapper);

        if (rateGameEntityList.size()!=0) {
            retJson.put("rateFlag", 1);
            retJson.put("rate", rateGameEntityList.get(0).getRate());
        } else {
            retJson.put("rateFlag", 0);
            retJson.put("rate", 0);
        }

        if (collectMapEntityList.size()!=0 && collectMapEntityList.get(0).getFlag()==1) {
            retJson.put("collectFlag", 1);
        } else {
            retJson.put("collectFlag", 0);
        }

        return retJson;
    }

    @Override
    public JSONObject chooseGame(String token, ChooseGameRo ro) throws Exception {

        JSONObject retJson = new JSONObject();

        for (Integer id: ro.getChooseList()) {
            UserCollectRo userCollectRo = new UserCollectRo();
            userCollectRo.setUserId(ro.getUserId());
            userCollectRo.setGameId(id);
            try {
                addCollect(token,userCollectRo);
            } catch (Exception ignored) {
            }
        }
        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("id", ro.getUserId());
        UserEntity updatePara = new UserEntity();
        updatePara.setRecommended(1);
        userMapper.update(updatePara, userEntityQueryWrapper);
        retJson.put("retCode", "0000");
        retJson.put("retMsg", "成功");
        return retJson;
    }
}
