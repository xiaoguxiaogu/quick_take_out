package com.me.quick.dto;


import com.me.quick.entity.Setmeal;
import com.me.quick.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
