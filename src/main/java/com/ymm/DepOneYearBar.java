package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by zhanghailin on 2017/4/12.
 */
//一年间各门诊容量及份额
public class DepOneYearBar extends ChartObject {

    public DepOneYearBar(String sTime, String eTime, String dep, List<String> productList, String chart){
        chartName = chart;
        productNameList = productList;

        String productWhere = Utils.getProductWhere(productNameList);

        String categorySql = String.format("select date from dep where date >= %s and date <= %s group by date order by date", sTime, eTime);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select date as x, product as l, sum(count) as y from dep where date >= %s and date <= %s and dep = '%s' and product in (%s) group by date, product", sTime, eTime, dep, productWhere);

        valueObjectList = jdbcTemplate.query(sql, new RowMapper<ValueObject>() {
            @Override
            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new ValueObject(resultSet);
            }
        });
    }
}
