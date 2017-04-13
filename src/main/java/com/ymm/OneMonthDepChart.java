package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 4/12/2017.
 */
public class OneMonthDepChart extends ChartObject {
    private String chartName;
    private List<String> productNameList;
    private String chartType;
    private List<String> categoryList = new ArrayList<String>();
    private List<ValueObject> valueObjectList = new ArrayList<ValueObject>();

    public OneMonthDepChart(String date, List<String> productList, List<String> depList, String chart, String type){
        chartName = chart;
        chartType = type;
        productNameList = productList;

        String productWhere = Utils.getProductWhere(productNameList);
        String depWhere = Utils.getProductWhere(depList);

        String categorySql = String.format("select dep from dep where date = %s and product in (%s) and dep in (%s) group by dep order by sum(count) desc", date, productWhere, depWhere);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select dep, product, sum(count) as count from dep where data = %s and product in (%s) and dep in (%s) group by dep;", date, productWhere, depWhere);

        valueObjectList = jdbcTemplate.query(sql, new RowMapper<ValueObject>() {
            @Override
            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                ValueObject item = new ValueObject(resultSet.getString("dep"),resultSet.getString("count"),resultSet.getString("product"));
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
