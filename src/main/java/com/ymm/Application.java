package com.ymm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghailin on 2017/4/9.
 */
@SpringBootApplication
public class Application {

    public static ApplicationContext applicationContext;
    public static void main(String[] args){
        applicationContext = SpringApplication.run(Application.class, args);
        System.out.println("*********");
        List<String> list = new ArrayList<>();
        list.add("代文");
        list.add("雅施达");
        list.add("科素亚");
        OneMonthTopBar oneMonthTopBar = new OneMonthTopBar("201703", list, "X", "6", "10");
        System.out.println(Utils.getJson(oneMonthTopBar, "columnChart"));
    }

}
