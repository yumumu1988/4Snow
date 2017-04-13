package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 4/12/2017.
 */
//一年间友谊门诊RAAS容量与份额图
public class OneYearChart extends ChartObject{
    private String chartName;
    private List<String> productNameList;
    private String chartType;
    private List<String> categoryList = new ArrayList<String>();
    private List<ValueObject> valueObjectList = new ArrayList<ValueObject>();


    public OneYearChart(String sTime, String eTime, List<String> productList, String chart, String type){
        chartName = chart;
        productNameList = productList;
        chartType = type;

        String categorySql = String.format("select date from dep where date >= %s and date <= %s group by date order by date", sTime, eTime);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String productWhere = Utils.getProductWhere(productNameList);

        String sql = String.format("select date, product, sum(count) as count from dep where date >= %s and date <= %s and product in (%s) group by date, product order by date desc;", sTime, eTime, productWhere);

        valueObjectList = jdbcTemplate.query(sql, new RowMapper<ValueObject>() {
            @Override
            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                ValueObject item = new ValueObject(resultSet.getString("date"),resultSet.getString("count"),resultSet.getString("product"));
                return item;
            }
        });
    }

    public String getStacking(){
       return Utils.getStacking(chartType);
    }

    public String getyFormat(){
        return Utils.getyFormat(chartType);
    }

    public String getChartName() {
        return chartName;
    }

    public String getCategories(){
        return Utils.getCategories(categoryList);
    }

    public String getSeries(){
        return Utils.getSeries(productNameList, categoryList, valueObjectList);
    }
}
