package org.ltboys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.ltboys.dto.ro.RegisterRo;
import org.ltboys.mysql.entity.UserEntity;
import org.ltboys.mysql.mapper.UserMapper;
import org.ltboys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            retJson.put("retCode","9999");
            retJson.put("retMsg","用户名重复");
            return retJson;
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setAccount(ro.getAccount());
        userEntity.setPassword(ro.getPassword());
        userEntity.setUserName(ro.getUserName());
        int fact = userMapper.insert(userEntity);

        if(fact != 1){
            retJson.put("retCode","9999");
            retJson.put("retMsg","注册失败");
            return retJson;
        }

        retJson.put("retCode","0000");
        retJson.put("retMsg","注册成功");
        return retJson;
    }
}
