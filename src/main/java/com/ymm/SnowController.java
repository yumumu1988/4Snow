package com.ymm;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.operations.Mod;
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

    private static final List<String> proList = new ArrayList<String>(){{
        add("雅施达");
        add("科素亚");
        add("代文");
    }};

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/")
    public ModelAndView loadMainPage(){
        ModelAndView modelAndView = new ModelAndView("mainPage");
        return modelAndView;
    }

    @RequestMapping(value = "/trendLine")
    public ModelAndView trendLine(){
        ModelAndView modelAndView = new ModelAndView("trendLine");
        return modelAndView;
    }

    @RequestMapping(value = "/trendLineChart")
    @ResponseBody
    public String trendLineChart(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("name") String name, @RequestParam("type") String type){
        LineChartObject lineChartObject = new LineChartObject(startTime.replace("-", ""), endTime.replace("-", ""), proList, name, type);
        String format = "\"{y}\"";
        if(!type.equalsIgnoreCase("容量")){
            format = "\"{y}%\"";
        }
        return Utils.getJson("lineChart", name, lineChartObject.getCategories(), lineChartObject.getSeries()).replace("@format", format);
    }

    @RequestMapping(value = "/pie")
    public ModelAndView pie(){
        ModelAndView modelAndView = new ModelAndView("pie");
        return modelAndView;
    }

    @RequestMapping(value = "/pieChart")
    @ResponseBody
    public String pieChart(@RequestParam("date") String date, @RequestParam("product") String product, @RequestParam("type") String type, @RequestParam("name") String name){
        PieChartObject pieChartObject = new PieChartObject(date.replace("-", ""), product, name, type);
        String fileName = "pieChart";
        if(!type.equalsIgnoreCase("容量")){
            fileName = "piePerChart";
        }
        return Utils.getJson(fileName, name, "", pieChartObject.getSeries());
    }

    @RequestMapping(value = "/oneYearBar")
    public ModelAndView oneYearBar(){
        ModelAndView modelAndView = new ModelAndView("oneYearBar");
        return modelAndView;
    }

    @RequestMapping(value = "/oneYearBarChart")
    @ResponseBody
    public String oneYearBarChart(@RequestParam("name") String name, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("type") String type){
        OneYearChart oneYearChart = new OneYearChart(startTime.replace("-", ""), endTime.replace("-", ""), proList, name, type);
        return Utils.getJson(type, name, oneYearChart.getCategories(), oneYearChart.getSeries());
    }

    @RequestMapping(value = "/oneMonthBar")
    public ModelAndView oneMonthBar(){
        ModelAndView modelAndView = new ModelAndView("oneMonthBar");
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= 50; i++){
            map.put(i, i);
        }
        modelAndView.addObject("topList", map);
        return  modelAndView;
    }

    @RequestMapping(value = "/oneMonthBarChart")
    @ResponseBody
    public String oneMonthBarChart(@RequestParam("name") String name, @RequestParam("date") String date, @RequestParam("top1") String top1, @RequestParam("top2") String top2, @RequestParam("type") String type){
        OneMonthDepChart oneMonthDepChart = new OneMonthDepChart(date.replace("-", ""), proList, name, type, top1, top2);
        return Utils.getJson(type, name, oneMonthDepChart.getCategories(), oneMonthDepChart.getSeries());
    }

    @RequestMapping(value = "/oneMonthTopBar")
    public ModelAndView oneMonthTopBar(){
        ModelAndView modelAndView = new ModelAndView("oneMonthTopBar");
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= 50; i++){
            map.put(i, i);
        }
        modelAndView.addObject("topList", map);
        return modelAndView;
    }

    @RequestMapping(value = "/oneMonthTopBarChart")
    @ResponseBody
    public String oneMonthTopBarChart(@RequestParam("date") String date, @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("top1") String top1, @RequestParam("top2") String top2){
        OneMonthTopBar oneMonthTopBar = new OneMonthTopBar(date.replace("-", ""), proList, name, top1 ,top2);
        return Utils.getJson(oneMonthTopBar, type);
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
