package com.me.quick;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author chen
 * @Date 2022-04-15-15:19
 */
@SpringBootApplication
@Slf4j
@ServletComponentScan
@EnableTransactionManagement
public class QuickApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickApplication.class,args);
        log.info("项目启动成功");
    }
}
