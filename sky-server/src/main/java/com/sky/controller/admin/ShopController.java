package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺状态")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result setStatus(@PathVariable Integer status){

        redisTemplate.opsForValue().set("shop_status",status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus(){
        return Result.success((Integer) redisTemplate.opsForValue().get("shop_status"));
    }


}
