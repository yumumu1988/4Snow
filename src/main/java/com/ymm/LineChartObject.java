package com.ymm;

import javafx.scene.shape.LineTo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 4/12/2017.
 */
//一年市场还原走势容量线份额线形图
public class LineChartObject extends ChartObject {
    private String chartName;
    private List<String> productNameList;
    private String chartType;
    private List<String> categoryList = new ArrayList<String>();
    private List<LineValueObject> valueObjectList = new ArrayList<LineValueObject>();
    private List<LineTotalValueObject> totalValueObjectList = new ArrayList<LineTotalValueObject>();

    public LineChartObject(String sTime, String eTime, List<String> productList, String chart, String type){
        chartName = chart;
        productNameList = productList;
        chartType = type;
        String categorySql = String.format("select date from dep where date >= %s and date <= %s group by date order by date", sTime, eTime);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        List<String> proList = new ArrayList<String>();
        for (String item : productNameList){
            proList.add(String.format("'%s'", item));
        }

        String productWhere = StringUtils.join(proList, ",");
        List<String> noYSDList = new ArrayList<>();
        for (String item : productList){
            if (item.equalsIgnoreCase("雅施达")){
                continue;
            }
            noYSDList.add(String.format("'%s'", item));
        }
        String noYSDProductWhere = StringUtils.join(noYSDList, ",");

//        String sql = String.format("select date, product, round(sum(count)) as count from ( (select date, product, count from dep where date >= %s and date <= %s and product in (%s)) union all (select date, product, round(count/7,2) as count from room where date >= %s and date <= %s and product in (%s)) ) dep group by date, product order by date desc;", sTime, eTime, productWhere, sTime, eTime, productWhere);
        String sql = String.format("select date, product, round(sum(count)) as count from ( (select date, product, count from dep where date >= %s and date <= %s and product in (%s)) union all (select date, product, round(count/7,2) as count from room where date >= %s and date <= %s and product in (%s)) union all (select date, product, round(count/7.5,2) as count from room where date >= %s and date <= %s and product in (%s)) ) dep group by date, product order by date desc;", sTime, eTime, productWhere, sTime, eTime, noYSDProductWhere, sTime, eTime, "\"雅施达\"");

        valueObjectList = jdbcTemplate.query(sql, new RowMapper<LineValueObject>() {
            @Override
            public LineValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                LineValueObject item = new LineValueObject();
                item.setCount(resultSet.getString("count"));
                item.setProduct(resultSet.getString("product"));
                item.setDate(resultSet.getString("date"));
                return item;
            }

        });

//        String totalSql = String.format("select date, round(sum(count)) as count from ((select date, product, count from dep where date >= %s and date <= %s and product in (%s)) union all (select date, product, round(count/7, 2) as count from room where date >= %s and date <= %s and product in (%s))) dep group by date;", sTime, eTime, productWhere, sTime, eTime, productWhere);
        String totalSql = String.format("select date, round(sum(count)) as count from ((select date, product, count from dep where date >= %s and date <= %s and product in (%s)) union all (select date, product, round(count/7, 2) as count from room where date >= %s and date <= %s and product in (%s)) union all (select date, product, round(count/7.5, 2) as count from room where date >= %s and date <= %s and product in (%s))) dep group by date;", sTime, eTime, productWhere, sTime, eTime, noYSDProductWhere, sTime, eTime, "\"雅施达\"");

        totalValueObjectList = jdbcTemplate.query(totalSql, new RowMapper<LineTotalValueObject>() {
            @Override
            public LineTotalValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                LineTotalValueObject item = new LineTotalValueObject();
                item.setCount(resultSet.getString("count"));
                item.setDate(resultSet.getString("date"));
                return item;
            }
        });
    }

    public String getChartName() {
        return chartName;
    }

    public String getCategories(){
        List<String> result = new ArrayList<String>();
        String format = "\"%s\"";
        for (String item : categoryList){
            result.add(String.format(format, item));
        }
        return StringUtils.join(result, ",");
    }

    public String getSeries(){
        List<String> result = new ArrayList<String>();
        String format = "{\"name\":\"%s\",\"data\":[%s]}";
        for (String pro : productNameList){
            List<String> valueList = new ArrayList<String>();
            for (String date : categoryList){
                String value = "0";
                for (LineValueObject object : valueObjectList){
                    if(object.getProduct().equalsIgnoreCase(pro) && object.getDate().equalsIgnoreCase(date)){
                        if(chartType.equalsIgnoreCase("容量")){
                            value = object.getCount();
                        } else {
                            String totalValue = "0";
                            for (LineTotalValueObject totalValueObject : totalValueObjectList){
                                if(totalValueObject.getDate().equalsIgnoreCase(date)){
                                    totalValue = totalValueObject.getCount();
                                    break;
                                }
                            }
                            if (totalValue.equalsIgnoreCase("0")){
                                value = "0";
                            } else {
                                value = String.format("%.2f", Integer.parseInt(object.getCount())*100.00/Integer.parseInt(totalValue));
                            }
                        }
                        break;
                    }
                }
                valueList.add(value);
            }
            result.add(String.format(format, pro, StringUtils.join(valueList, ",")));
        }

        return StringUtils.join(result, ",");
    }
}

class LineValueObject{
    private String date;
    private String product;
    private String count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

class LineTotalValueObject{
    private String date;
    private String count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
