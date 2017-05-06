package com.ymm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
        add("洛汀新");
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

    @RequestMapping(value = "/depOneYearBar")
    public ModelAndView depOneYearBar()
    {
        ModelAndView modelAndView = new ModelAndView("depOneYearBar");
        modelAndView.addObject("depList", Utils.getDepList());
        return modelAndView;
    }

    @RequestMapping(value = "/depOneYearBarChart")
    @ResponseBody
    public String depOneYearBarChart(@RequestParam("name") String name, @RequestParam("startTime") String startTime,
                                     @RequestParam("endTime") String endTime, @RequestParam("dep") String dep, @RequestParam("type") String type){
        DepOneYearBar depOneYearBar = new DepOneYearBar(startTime.replace("-", ""), endTime.replace("-", ""), dep, proList, name);
        return Utils.getJson(depOneYearBar, type);
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

    @RequestMapping(value = "/depMultiMonthTop")
    public ModelAndView depMultiMonthTop(){
        ModelAndView modelAndView = new ModelAndView("depMultiMonthTop");
        modelAndView.addObject("depList", Utils.getDepList());
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= 50; i++){
            map.put(i, i);
        }
        modelAndView.addObject("topList", map);
        return modelAndView;
    }

    @RequestMapping(value = "/depMultiMonthTopChart")
    @ResponseBody
    public String depMultiMonthTopChart(@RequestParam("name") String name, @RequestParam("startTime") String startTime,
                                        @RequestParam("endTime") String endTime, @RequestParam("dep") String dep,
                                        @RequestParam("top1") String top1, @RequestParam("top2") String top2,
                                        @RequestParam("type") String type){
        DepMultiMonthTop depMultiMonthTop = new DepMultiMonthTop(startTime.replace("-", ""), endTime.replace("-", ""), dep, proList, name, top1, top2);
        return Utils.getGroupJson(depMultiMonthTop, type);
    }

    @RequestMapping(value = "centerMultiMonthTop")
    public ModelAndView centerMultiMonthTop(){
        ModelAndView modelAndView = new ModelAndView("centerMultiMonthTop");
        return modelAndView;
    }

    @RequestMapping(value = "/centerMultiMonthTopChart")
    @ResponseBody
    public String centerMultiMonthTop(@RequestParam("name") String name, @RequestParam("type") String type,
                                      @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime){
        CenterMultiMonthTop centerMultiMonthTop = new CenterMultiMonthTop(startTime.replace("-", ""), endTime.replace("-", "") , proList, name);
        return Utils.getSpecialGroupJson(centerMultiMonthTop, type);
    }

    @RequestMapping(value = "/innerMultiMonthTop")
    public ModelAndView innerMultiMonthTop(){
        ModelAndView modelAndView = new ModelAndView("innerMultiMonthTop");
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
//        for (int i = 1; i <= 50; i++){
//            map.put(i, i);
//        }
        map.put(3, 3);
        modelAndView.addObject("topList", map);
        return modelAndView;
    }

    @RequestMapping(value = "/innerMultiMonthTopChart")
    @ResponseBody
    public String innerMultiMonthTopChart(@RequestParam("name") String name, @RequestParam("type") String type,
                                          @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                          @RequestParam("top") int top){
        InnerMultiMonthTop innerMultiMonthTop = new InnerMultiMonthTop(startTime.replace("-", ""), endTime.replace("-", ""), proList, top, name);
        return innerMultiMonthTop.getJson(type);
    }

    @RequestMapping(value = "/leaderOneYearBar")
    public ModelAndView leaderOneYearBar(){
        ModelAndView modelAndView = new ModelAndView("LeaderOneYearBar");

        return modelAndView;
    }

    @RequestMapping(value = "/leaderOneYearBarChart")
    @ResponseBody
    public String leaderOneYearBarChart(@RequestParam(name = "name") String name, @RequestParam("type") String type,
                                        @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime){
        LeaderOneYearBar leaderOneYearBar = new LeaderOneYearBar(startTime.replace("-", ""), endTime.replace("-", ""), proList, name);
        return Utils.getGroupJson(leaderOneYearBar, type);
    }

    @RequestMapping(value = "/leaderOneMonthTop")
    public ModelAndView leaderOneMonthTop(){
        ModelAndView modelAndView = new ModelAndView("leaderOneMonthTop");

        return modelAndView;
    }

    @RequestMapping(value = "/leaderOneMonthTopChart")
    @ResponseBody
    public String leaderOneMonthTopChart(@RequestParam("name") String name, @RequestParam("date") String date,
                                         @RequestParam("type") String type){
        LeaderOneMonthTop leaderOneMonthTop = new LeaderOneMonthTop(date.replace("-", ""), name, proList);

        return Utils.getSpecialGroupJson(leaderOneMonthTop, type);

    }

    @RequestMapping(value = "/leaderMultiMonthTop")
    public ModelAndView leaderMultiMonthTop(){
        ModelAndView modelAndView = new ModelAndView("leaderMultiMonthTop");

        return modelAndView;
    }

    @RequestMapping(value = "/leaderMultiMonthTopChart")
    @ResponseBody
    public String leaderMultiMonthTopChart(@RequestParam("name") String name, @RequestParam("startTime") String startTime,
                                           @RequestParam("endTime") String endTime, @RequestParam("dep") String dep,
                                           @RequestParam("type") String type){
        LeaderMultiMonthTop leaderMultiMonthTop = new LeaderMultiMonthTop(startTime.replace("-", ""), endTime.replace("-", ""), name, dep, proList
        );

        return Utils.getGroupJson(leaderMultiMonthTop, type);
    }

    @RequestMapping(value = "/trendRoomBar")
    public ModelAndView trendRoomBar(){
        ModelAndView modelAndView = new ModelAndView("trendRoomBar");
        return modelAndView;
    }

    @RequestMapping(value = "/trendRoomBarChart")
    @ResponseBody
    public String trendRoomBarChart(@RequestParam("name") String name, @RequestParam("startTime") String startTime,
                                    @RequestParam("endTime") String endTime, @RequestParam("type") String type){
        TrendRoomBar trendRoomBar = new TrendRoomBar(name, startTime.replace("-", ""), endTime.replace("-", ""), proList);
        return Utils.getJson(trendRoomBar, type);
    }

    @RequestMapping(value = "/oneMonthRoomBar")
    public ModelAndView oneMonthRoomBar(){
        ModelAndView modelAndView = new ModelAndView("oneMonthRoomBar");
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= 50; i++){
            map.put(i, i);
        }
        modelAndView.addObject("topList", map);
        return modelAndView;
    }

    @RequestMapping(value = "/oneMonthRoomBarChart")
    @ResponseBody
    public String oneMonthRoomBarChart(@RequestParam("name") String name, @RequestParam("date") String date, @RequestParam("top1") String top1, @RequestParam("top2") String top2, @RequestParam("type") String type){
        OneMonthRoomBar oneMonthRoomBar = new OneMonthRoomBar(name, date.replace("-", ""), top1, top2, proList);
        return Utils.getJson(oneMonthRoomBar, type);
    }

    @RequestMapping(value = "/oneMonthRoomTopBar")
    public ModelAndView oneMonthRoomTopBar(){
        ModelAndView modelAndView = new ModelAndView("oneMonthRoomTopBar");
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= 50; i++){
            map.put(i, i);
        }
        modelAndView.addObject("topList", map);
        return modelAndView;
    }

    @RequestMapping(value = "/oneMonthRoomTopBarChart")
    @ResponseBody
    public String oneMonthRoomTopBarChart(@RequestParam("date") String date, @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("top1") String top1, @RequestParam("top2") String top2){
        OneMonthRoomTopBar oneMonthRoomTopBar = new OneMonthRoomTopBar(name, date.replace("-", ""), top1, top2, proList);
//        return Utils.getJson(oneMonthRoomTopBar, type);
        return Utils.getSpecialGroupJson(oneMonthRoomTopBar, type);
    }

    @RequestMapping(value = "/roomOneYearBar")
    public ModelAndView roomOneYearBar(){
        ModelAndView modelAndView = new ModelAndView("roomOneYearBar");
        modelAndView.addObject("depList", Utils.getRoomList());
        return modelAndView;
    }

    @RequestMapping(value = "/roomOneYearBarChart")
    @ResponseBody
    public String roomOneYearBarChart(@RequestParam("name") String name, @RequestParam("startTime") String startTime,
                                      @RequestParam("endTime") String endTime, @RequestParam("dep") String dep,
                                      @RequestParam("type") String type){
        RoomOneYearBar roomOneYearBar = new RoomOneYearBar(name, startTime.replace("-", ""), endTime.replace("-", ""), dep, proList);
        return Utils.getJson(roomOneYearBar, type);
    }

    @RequestMapping(value = "/shenRoomOneMonthTop")
    public ModelAndView shenRoomOneMonthTop(){
        ModelAndView modelAndView = new ModelAndView("shenRoomOneMonthTop");
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        map.put(3, 3);
        modelAndView.addObject("topList", map);
        return modelAndView;
    }

    @RequestMapping(value = "/shenRoomOneMonthTopChart")
    @ResponseBody
    public String shenRoomOneMonthTopChart(@RequestParam("name") String name, @RequestParam("type") String type,
                                           @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                           @RequestParam("top") int top){
        ShenRoomOneMonthTop shenRoomOneMonthTop = new ShenRoomOneMonthTop(startTime.replace("-", ""), endTime.replace("-", ""), proList, top, name);
        return shenRoomOneMonthTop.getJson(type);
    }

    @RequestMapping(value = "/roomMultiMonthTop")
    public ModelAndView roomMultiMonthTop(){
        ModelAndView modelAndView = new ModelAndView("roomMultiMonthTop");
        modelAndView.addObject("depList", Utils.getRoomList());
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 1; i <= 50; i++){
            map.put(i, i);
        }
        modelAndView.addObject("topList", map);
        return modelAndView;
    }

    @RequestMapping(value = "/roomMultiMonthTopChart")
    @ResponseBody
    public String roomMultiMonthTopChart(@RequestParam("name") String name, @RequestParam("startTime") String startTime,
                                         @RequestParam("endTime") String endTime, @RequestParam("dep") String dep,
                                         @RequestParam("top1") String top1, @RequestParam("top2") String top2,
                                         @RequestParam("type") String type){
        RoomMultiMonthTop roomMultiMonthTop = new RoomMultiMonthTop(startTime.replace("-", ""), endTime.replace("-", ""), dep, proList, name, top1, top2);
        return Utils.getGroupJson(roomMultiMonthTop, type);
    }
}
