package com.me.quick.dto;


import com.me.quick.entity.Dish;
import com.me.quick.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
//继承可以拥有父类的私有属性，但是不能直接用.访问
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;


}
