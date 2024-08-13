package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    static final String wxUrl="https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        System.out.println(userLoginDTO.toString());
        Map<String,String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("grant_type","authorization_code");
        map.put("js_code",userLoginDTO.getCode());

        String s = HttpClientUtil.doGet(wxUrl, map);
        System.out.println(s);
        JSONObject jsonObject = JSON.parseObject(s);
        String openid = jsonObject.getString("openid");
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        }

        User byOpenId = userMapper.getByOpenId(openid);

        if(byOpenId==null){
            User user=new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
            return user;
        }
        return byOpenId;
    }
}
