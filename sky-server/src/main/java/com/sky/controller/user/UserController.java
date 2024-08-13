package com.sky.controller.user;


import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api("client 端口")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation("微信端登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        User user1 = userService.wxLogin(userLoginDTO);
        Map<String,Object> map = new HashMap<>();
        map.put(JwtClaimsConstant.USER_ID,user1.getId());
        String jwt = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), map);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setId(user1.getId());
        userLoginVO.setOpenid(user1.getOpenid());
        userLoginVO.setToken(jwt);
        return Result.success(userLoginVO);


    }
}
