package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FlavorMapper {

    @Select("select * from sky_take_out.dish_flavor where dish_id=#{id}")
    List<DishFlavor> getByDishId(Long id);

    /**
     * 批量插入
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    @Delete("delete from sky_take_out.dish_flavor where dish_id =#{dishId}")
    void deleteByDid(Long dishId);

    void deleteBatch(List<Long> ids);
}
