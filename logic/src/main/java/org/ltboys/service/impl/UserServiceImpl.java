package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.context.utils.JwtUtil;
import org.ltboys.dto.ro.LoginRo;
import org.ltboys.dto.ro.RegisterRo;
import org.ltboys.mysql.entity.UserEntity;
import org.ltboys.mysql.mapper.UserMapper;
import org.ltboys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ltboys.context.utils.JwtUtil.getUserId;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


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
        int fact = userMapper.insert(userEntity);

        if(fact != 1){
            retJson.put("retCode","9900");
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

        JSONObject info = new JSONObject();
        info.put("username", userEntity.getUserName());
        info.put("password", "******");
        info.put("account",userEntity.getAccount());

        //发放token
        String token = JwtUtil.sign(userEntity.getId().toString(),info.toString());
        retJson.put("retCode","0000");
        retJson.put("retMsg","登录成功");
        retJson.put("token",token);
        return retJson;
    }

    @Override
    public JSONObject homepage(String token) throws Exception {

        String id = getUserId(token);
        JSONObject retJson = new JSONObject();

        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("id",getUserId(token))
                .select("user_name","icon","sex","birthday","phone_number");

        List<Map<String , Object>> mapList = userMapper.selectMaps(userEntityQueryWrapper);
        retJson.put("info",mapList);


        return retJson;
    }
}
