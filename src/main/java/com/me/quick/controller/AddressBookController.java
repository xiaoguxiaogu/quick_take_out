package com.me.quick.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.me.quick.common.BaseContext;
import com.me.quick.common.R;
import com.me.quick.entity.AddressBook;
import com.me.quick.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author chen
 * @Date 2022-04-21-11:31
 */
@RestController
@Slf4j
@RequestMapping("addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId((BaseContext.getCurrentId()));
        log.info("addressBook:{}",addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        //先将所有地址改为0，为非默认。以免发生混淆
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.eq(AddressBook::getIsDefault,1);
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(addressBookLambdaUpdateWrapper);

        //再将当前地址设置为默认地址，1
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<AddressBook> update(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }


    /**
     * 根据id查地址
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<AddressBook> getOne(@PathVariable Long id){
        AddressBook addressBook=addressBookService.getById(id);
        if(addressBook!=null){
            return R.success(addressBook);
        }else{
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);
        if(addressBook==null){
            return R.error("没有找到，应该是没有了");
        }else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询制定用户全部地址
     * @param addressBook
     * @return
     */
    @GetMapping("list")
    public R<List<AddressBook>> list(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());

        //条件构造器
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId());
        addressBookLambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> addressBookList = addressBookService.list(addressBookLambdaQueryWrapper);
        return R.success(addressBookList);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long[] ids){
        addressBookService.removeByIds(Arrays.asList(ids));
        return R.success("删除成功");
    }




}
