package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartserviceImpl implements ShoppingCartService {

    @Autowired
    protected ShoppingCartMapper shoppingCartMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list!=null&&list.size()>0){
            ShoppingCart shoppingCart1=list.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            shoppingCartMapper.updateNumberByid(shoppingCart1);
        }
        else{
            Long dishId = shoppingCartDTO.getDishId();
            if(dishId!=null){
                Dish byId = dishMapper.getById(dishId);
                shoppingCart.setImage(byId.getImage());
                shoppingCart.setAmount(byId.getPrice());
                shoppingCart.setName(byId.getName());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
            }
            else {

                Setmeal byId = setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setImage(byId.getImage());
                shoppingCart.setAmount(byId.getPrice());
                shoppingCart.setName(byId.getName());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
            }
        }

        shoppingCartMapper.insert(shoppingCart);
    }

    @Override
    public void clear() {
        shoppingCartMapper.clear(BaseContext.getCurrentId());
    }

    @Override
    public List<ShoppingCart> show() {

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }
}
