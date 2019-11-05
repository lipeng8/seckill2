package com.cn.springboot.seckill2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng
 * @CreateDate: 2019/7/19$ 15:08$
 * @Version: 1.0
 */
@EnableAutoConfiguration
@RestController
public class StartUpClass {
    @RequestMapping("/")
    public String home(){
        return "hello world";
    }
    public static void main(String[] args) {
        System.out.println("hello world");
        SpringApplication.run(StartUpClass.class,args);
    }
}
