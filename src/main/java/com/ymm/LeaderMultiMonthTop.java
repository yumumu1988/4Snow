package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghailin on 2017/5/1.
 */
//多月干保门诊TOP客户容量及份额
public class LeaderMultiMonthTop extends ChartObject {
    public LeaderMultiMonthTop(String startTime, String endTime, String chart, String dep, List<String> productList) {
        productNameList = productList;
        chartName = chart;

        String productWhere = Utils.getProductWhere(productNameList);

        String baseCategorySql = String.format("select doctor from dep where date = %s and dep = '%s' and product in (%s) group by doctor order by sum(count) desc limit 3", endTime, dep, productWhere);

        baseCategoryList = jdbcTemplate.queryForList(baseCategorySql, String.class);

        String categorySql = String.format("select date as date from dep where date >= %s and date <= %s group by date order by date ", startTime, endTime);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select date as x1, doctor as x2, product as l, sum(count) as y from dep where date >= %s and date <= %s and dep = '%s' and product in (%s) group by date, doctor, product", startTime, endTime, dep, productWhere);

        groupValueObjectList = jdbcTemplate.query(sql, new RowMapper<GroupValueObject>() {
            @Override
            public GroupValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new GroupValueObject(resultSet);
            }
        });
    }
}
