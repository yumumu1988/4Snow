package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghailin on 2017/4/30.
 */
//一年内干保门诊RAAS容量和份额
public class LeaderOneYearBar extends ChartObject {
    public LeaderOneYearBar(String sTime, String eTime, List<String> productList, String chart){
        productNameList = productList;
        chartName = chart;
        String dep1 = "一门诊内科";
        String dep2 = "二门诊内科";

        String productWhere = Utils.getProductWhere(productNameList);

        baseCategoryList = new ArrayList<String>(){{
            add(dep1);
            add(dep2);
        }};

        String categorySql = String.format("select date as date from dep where date >= %s and date <= %s group by date order by date ", sTime, eTime);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select date as x1, dep as x2, product as l, sum(count) as y from dep where date >= %s and date <= %s and dep in ('一门诊内科', '二门诊内科') and product in (%s) group by date, dep, product", sTime, eTime, productWhere);

        groupValueObjectList = jdbcTemplate.query(sql, new RowMapper<GroupValueObject>() {
            @Override
            public GroupValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new GroupValueObject(resultSet);
            }
        });
    }
}
