package com.ymm;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 4/12/2017.
 */
public class PieChartObject extends ChartObject {
    private String chartName;
    private String yFormat;
    private String productName;
    private String dateTime;
    private List<String> dep;

    public PieChartObject(String date, String product, List<String> depList, String chart, String type){
        chartName = chart;
        if(type.equalsIgnoreCase("份额")){
            yFormat = "\"{point.name}: {point.percentage:.1f} %\"";
        } else {
            yFormat = "\"{point.name}: {y}\"";
        }
        dateTime = date;
        productName = product;
        dep = depList;
    }

    public String getChartName() {
        return chartName;
    }

    public String getyFormat() {
        return yFormat;
    }

    public String getSeries(){
        String depWhere = "";
        if(!dep.contains("全部")){
            String depFormat = "'%s'";
            List<String> depList = new ArrayList<String>();
            for (String item : dep){
                depList.add(String.format(depFormat, item));
            }
            depWhere = " and dep in (" + StringUtils.join(depList, ",") + ") ";
        }

        String sql = String.format("select dep, sum(count) as count from dep where date = %s and product = '%s' %s group by dep order by count desc;", dateTime, productName, depWhere);
        List<String> resultList = new ArrayList<String>();
        String format = "{\"name\":\"%s\",\"y\":%s}";
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        for (Map item : data){
            String str = String.format(format, item.get("dep").toString(), item.get("count").toString());
            resultList.add(str);
        }
        return StringUtils.join(resultList,",");
    }
}
