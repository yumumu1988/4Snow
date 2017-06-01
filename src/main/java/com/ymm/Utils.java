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

    public static String getTop1(String top1){
        return Integer.toString(Integer.parseInt(top1) - 1);
    }

    public static String getTop2(String top1, String top2){
        return Integer.toString(Integer.parseInt(top2) - Integer.parseInt(top1) + 1);
    }

    public static LinkedHashMap<String, String> getProList(){
        List<String> list = jdbcTemplate.queryForList("select product from dep group by product", String.class);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String item : list){
            map.put(item, item);
        }
        return map;
    }

    public static LinkedHashMap<String,String> getDepList() {
        List<String> list = jdbcTemplate.queryForList("select dep from dep group by dep", String.class);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String item : list){
            map.put(item, item);
        }
        return map;
    }

    public static LinkedHashMap<String,String> getRoomList() {
        List<String> list = jdbcTemplate.queryForList("select dep from room group by dep", String.class);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (String item : list){
            map.put(item, item);
        }
        return map;
    }

    public static String getCategories(List<String> categoryList){
        List<String> result = new ArrayList<String>();
        String format = "\"%s\"";
        for (String item : categoryList){
            result.add(String.format(format, item));
        }
        return StringUtils.join(result, ",");
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

    /*
    x1 is date and x2 is name
     */
    public static String getGroupCategories(List<String> baseCategoryList, List<String> categoryList) {
        String format1 = "{\"name\": \"%s\", \"categories\": [%s]}";
        String format2 = "\"%s\"";
        List<String> formart2ResultList = new ArrayList<>();
        for (String item : categoryList){
            formart2ResultList.add(String.format(format2, item));
        }
        String format2Result = StringUtils.join(formart2ResultList, ",");
        List<String> format1ResultList = new ArrayList<>();
        for (String item : baseCategoryList){
            format1ResultList.add(String.format(format1, item, format2Result));
        }
        return StringUtils.join(format1ResultList, ",");
    }

    public static String getGroupSeries(List<String> productNameList, List<String> baseCategoryList, List<String> categoryList, List<GroupValueObject> groupValueObjectList){
        List<String> result  = new ArrayList<>();
        String format = "{\"name\": \"%s\",\"data\":[%s]}";
        productNameList = sortProduct(productNameList);
        for (String pro : productNameList){
            List<String> valueList  = new ArrayList<>();
            for (String x2 : baseCategoryList){
                for(String x1 : categoryList){
                    String y = "0";
                    for (GroupValueObject item : groupValueObjectList){
                        if(item.getLegend().equalsIgnoreCase(pro) && item.getX1().equalsIgnoreCase(x1) && item.getX2().equalsIgnoreCase(x2)){
                            y = item.getY();
                            break;
                        }
                    }
                    valueList.add(y);
                }
            }
            result.add(String.format(format, pro, StringUtils.join(valueList, ",")));
        }
        return StringUtils.join(result, ",");

    }

    public static String getGroupJson(ChartObject chartObject, String filename){
        return getJson(filename, chartObject.getChartName(), chartObject.getGroupCategories(), chartObject.getGroupSeries());
    }

    /*
    x1 is name and x2 is also name
     */
    public static String getSpecialGroupCategories(List<String> baseCategoryList, List<SpecialCategoryObject> categoryList){
        String format1 = "{\"name\": \"%s\", \"categories\": [%s]}";
        List<String> format1ResultList = new ArrayList<>();
        for (String item : baseCategoryList){

            String result = fillCategory(item, categoryList);

            format1ResultList.add(String.format(format1, item, result));
        }
        return StringUtils.join(format1ResultList, ",");
    }

    private static String fillCategory(String baseCategory, List<SpecialCategoryObject> categoryList) {
        List<String> result = new ArrayList<>();
        String format2 = "\"%s\"";
        for (SpecialCategoryObject item : categoryList){
            if(item.getDate().equalsIgnoreCase(baseCategory)){
                result.add(String.format(format2, item.getDoctor()));
            }
        }
        return StringUtils.join(result, ",");
    }

    public static String getSpecialGroupSeries(List<String> productNameList, List<String> baseCategoryList, List<SpecialCategoryObject> specialCategoryList, List<GroupValueObject> groupValueObjectList){
        List<String> result  = new ArrayList<>();
        String format = "{\"name\": \"%s\",\"data\":[%s]}";
        productNameList = sortProduct(productNameList);
        for (String pro : productNameList){
            List<String> valueList  = new ArrayList<>();
            for (String x2 : baseCategoryList){
                for (SpecialCategoryObject x1 : specialCategoryList){
                    if(x1.getDate().equalsIgnoreCase(x2)){

                        String y = "0";

                        for (GroupValueObject item : groupValueObjectList){
                            if(item.getLegend().equalsIgnoreCase(pro) && item.getX2().equalsIgnoreCase(x2) && item.getX1().equalsIgnoreCase(x1.getDoctor())){

                                y = item.getY();
                                break;
                            }
                        }

                        valueList.add(y);
                    }
                }
            }
            result.add(String.format(format, pro, StringUtils.join(valueList, ",")));
        }
        return StringUtils.join(result, ",");
    }

    public static String getSpecialGroupJson(ChartObject chartObject, String filename){
        return getJson(filename, chartObject.getChartName(), chartObject.getSpecialGroupCategories(), chartObject.getSpecialGroupSeries());
    }

}
