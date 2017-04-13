package com.ymm;

import jdk.internal.dynalink.beans.StaticClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by James on 4/12/2017.
 */
@Component
public class Utils {
    private static ResourceLoader resourceLoader;
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    private void setJdbcTemplate(JdbcTemplate jt){
        jdbcTemplate = jt;
    }

    @Autowired
    private void setResourceLoader(ResourceLoader loader){
        resourceLoader= loader;
    }

    public static String getCategories(List<String> categoryList){
        List<String> result = new ArrayList<String>();
        String format = "\"%s\"";
        for (String item : categoryList){
            result.add(String.format(format, item));
        }
        return StringUtils.join(result, ",");
    }

    public static String getProductWhere(List<String> productNameList){
        List<String> proList = new ArrayList<String>();
        for (String item : productNameList){
            proList.add(String.format("'%s'", item));
        }

        return StringUtils.join(proList, ",");
    }

    public static String getStacking(String chartType){
        if(chartType.equalsIgnoreCase("容量")){
            return "\"normal\"";
        } else {
            return "\"percent\"";
        }
    }

    public static String getyFormat(String chartType){
        if(chartType.equalsIgnoreCase("容量")){
            return "\"{y}\"";
        } else {
            return "\"{point.percentage:.0f}%\"";
        }
    }

    public static List<String> sortProduct(List<String> productNameList){
        Collections.sort(productNameList, Collator.getInstance(java.util.Locale.CHINA));
        Collections.reverse(productNameList);
        return productNameList;
    }

    public static String getSeries(List<String> productNameList, List<String> categoryList, List<ValueObject> valueObjectList){
        List<String> result = new ArrayList<String>();
        String format = "{\"name\":\"%s\",\"data\":[%s]}";
        productNameList = sortProduct(productNameList);
        for (String pro : productNameList){
            List<String> valueList = new ArrayList<String>();
            for (String x : categoryList){
                String y = "0";
                for (ValueObject item : valueObjectList){
                    if(item.getLegend().equalsIgnoreCase(pro) && item.getX().equalsIgnoreCase(x)){
                        y = item.getY();
                        break;
                    }
                }
                valueList.add(y);
            }
            result.add(String.format(format, pro, StringUtils.join(valueList, ",")));
        }
        return StringUtils.join(result, ",");
    }

    public static String getGroupSeries(){
        return "";
    }

    public static String getJson(ChartObject chartObject, String fileName){
        return getJson(fileName, chartObject.getChartName(), chartObject.getCategories(), chartObject.getSeries());
    }

    public static String getJson(String fileName, String title, String categories, String series){
        Resource resource = resourceLoader.getResource("classpath:" + fileName);
        StringBuilder jsonString = new StringBuilder();
        String line = null;
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null && line  != "" ) {
                jsonString.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString.toString().replace("@title", title).replace("@categories",categories).replace("@series",series);
    }

    public static String getTop1(String top1){
        return Integer.toString(Integer.parseInt(top1) - 1);
    }

    public static String getTop2(String top1, String top2){
        return Integer.toString(Integer.parseInt(top2) - Integer.parseInt(top1) + 1);
    }

    public static LinkedHashMap<String,String> getDepList() {
        List<String> list = jdbcTemplate.queryForList("select dep from dep group by dep", String.class);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String item : list){
            map.put(item, item);
        }
        return map;
    }
}
