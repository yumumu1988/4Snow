package com.ymm;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghailin on 2017/4/19.
 */
//内科门诊(多月)TOP客户容量及份额
public class InnerMultiMonthTop extends ChartObject {

    private int top;

    public InnerMultiMonthTop(String sTime, String eTime, List<String> productList, int top, String chart){
        productNameList = productList;
        chartName = chart;
        String dep = "内科门诊";
        this.top = top;

        String productWhere = Utils.getProductWhere(productNameList);

        String baseCategorySql = String.format("select date from dep where date >= %s and date <= %s group by date order by date ", sTime, eTime);

        baseCategoryList = jdbcTemplate.queryForList(baseCategorySql, String.class);

        String categorySql = String.format("select date, doctor from dep where date >= %s and date <= %s and dep = '%s' and product in (%s) group by date, doctor order by sum(count) desc", sTime, eTime, dep, productWhere);

        specialCategoryList = jdbcTemplate.query(categorySql, new RowMapper<SpecialCategoryObject>() {
            @Override
            public SpecialCategoryObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new SpecialCategoryObject(resultSet);
            }
        });

        String sql = String.format("select date as x2, doctor as x1, product as l, sum(count) as y from dep where date >= %s and date <= %s and dep = '%s' and product in (%s) group by date, doctor, product", sTime, eTime, dep, productWhere);

        groupValueObjectList = jdbcTemplate.query(sql, new RowMapper<GroupValueObject>() {
            @Override
            public GroupValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new GroupValueObject(resultSet);
            }
        });
    }

    public String getJson(String type){
        return Utils.getJson(type, chartName, getGroupCategories(), getGroupSeries());
    }


    @Override
    public String getGroupCategories() {
        String format1 = "{\"name\": \"%s\", \"categories\": [%s]}";
        String format2 = "\"%s\"";
        List<String> result1 = new ArrayList<>();
        for (String item : baseCategoryList){
            int topCount = top;
            List<String> result2 = new ArrayList<>();
            for (SpecialCategoryObject sco : specialCategoryList){
                if(topCount == 0){
                    break;
                }
                if(sco.getDate().equalsIgnoreCase(item)){
                    result2.add(String.format(format2, sco.getDoctor()));
                    topCount--;
                }
            }
            result1.add(String.format(format1, item, StringUtils.join(result2, ",")));
        }

        return StringUtils.join(result1, ",");
    }

    @Override
    public String getGroupSeries() {
        List<String> result  = new ArrayList<>();
        String format = "{\"name\": \"%s\",\"data\":[%s]}";
        for (String pro : productNameList){

            List<String> valueList = new ArrayList<>();
            for(String x2 : baseCategoryList){
                int topCount = top;
                for (SpecialCategoryObject sco : specialCategoryList){
                    if (topCount == 0){
                        break;
                    }
                    if(!sco.getDate().equalsIgnoreCase(x2)){
                        continue;
                    }
                    String yValue = "0";
                    for (GroupValueObject value : groupValueObjectList){
                        if (value.getLegend().equalsIgnoreCase(pro) && value.getX2().equalsIgnoreCase(x2) && value.getX1().equalsIgnoreCase(sco.getDoctor())){

                            yValue = value.getY();
                            top--;

                            break;
                        }
                    }
                    valueList.add(yValue);
                }
            }

            result.add(String.format(format, pro, StringUtils.join(valueList, ",")));
        }

        return StringUtils.join(result, ",");
    }
}
