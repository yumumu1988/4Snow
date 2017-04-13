package com.ymm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zhanghailin on 2017/4/9.
 */

@Controller
public class SnowController {

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/")
    public ModelAndView loadMainPage(){
        ModelAndView modelAndView = new ModelAndView("mainPage");
        return modelAndView;
    }



    @RequestMapping(value = "/depOneMonthTop")
    public ModelAndView depOneMonthTop(){
        ModelAndView modelAndView = new ModelAndView("depOneMonthTop");
        modelAndView.addObject("depList", Utils.getDepList());
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= 50; i++){
            map.put(i, i);
        }
        modelAndView.addObject("topList", map);
        return modelAndView;
    }

    @RequestMapping(value = "/depOneMonthTopChart")
    @ResponseBody
    public String depOneMonthTopChart(@RequestParam("name") String name, @RequestParam("dep")String dep, @RequestParam("date") String date, @RequestParam("top1") String top1, @RequestParam("top2") String top2, @RequestParam("chart") String chart){
        List<String > list = new ArrayList<String>(){{
            add("雅施达");
            add("科素亚");
            add("代文");
        }};
        DepOneMonthTop depOneMonthTop = new DepOneMonthTop(date.replace("-", ""), dep, list, name, top1, top2);
        return Utils.getJson(depOneMonthTop, chart);
    }
}
