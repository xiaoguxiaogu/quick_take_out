package com.me.quick.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.quick.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @Date 2022-04-21-11:29
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
