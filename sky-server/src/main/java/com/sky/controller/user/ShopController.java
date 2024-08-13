package com.sky.controller.user;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺状态")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus(){
        return Result.success((Integer) redisTemplate.opsForValue().get("shop_status"));
    }


}
