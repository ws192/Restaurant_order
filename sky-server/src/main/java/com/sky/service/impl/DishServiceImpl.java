package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.record.DVALRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DishServiceImpl implements DishService {


    @Autowired
    DishMapper dishMapper;

    @Autowired
    FlavorMapper flavorMapper;

    @Autowired
    SetMealDishMapper setMealDishMapper;


    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
    */
    public List<Dish> list(Long categoryId) {
    Dish dish = Dish.builder()
        .categoryId(categoryId)
        .status(StatusConstant.ENABLE)
        .build();
    return dishMapper.list(dish);
    }



    /**
     * 新增菜品与口味
     * @param dishDTO
     */
    @Override
    @Transactional
    public void savewithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insertDish(dish);

        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });

        if(!flavors.isEmpty()) {
            flavorMapper.insertBatch(flavors);
        }
    }


    /**
     *
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(Objects.equals(dish.getStatus(), StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setMealDishIds = setMealDishMapper.getSetMealDishIds(ids);
        if(setMealDishIds !=null && !setMealDishIds.isEmpty()){
            throw  new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            flavorMapper.deleteByDid(id);
//        }
        dishMapper.deleteBatch(ids);
        flavorMapper.deleteBatch(ids);
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish byId = dishMapper.getById(id);
        List<DishFlavor> dishFlavors= flavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(byId,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        flavorMapper.deleteByDid(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors !=null && !flavors.isEmpty()){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            flavorMapper.insertBatch(flavors);
        }

    }

     /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = flavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }


}
