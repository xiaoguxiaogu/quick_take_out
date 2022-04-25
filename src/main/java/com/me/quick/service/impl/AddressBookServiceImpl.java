package com.me.quick.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.quick.entity.AddressBook;
import com.me.quick.mapper.AddressBookMapper;
import com.me.quick.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jnlp.ServiceManager;

/**
 * @author chen
 * @Date 2022-04-21-11:30
 */
@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
