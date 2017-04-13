package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by zhanghailin on 2017/4/12.
 */
public class OneMonthTopBar extends ChartObject {

    public OneMonthTopBar(String date, List<String> productList, String chart, String top1, String top2){
        chartName = chart;
        productNameList = productList;

        String productWhere = Utils.getProductWhere(productNameList);

        String categorySql = String.format("select doctor from dep where date = %s and product in (%s) group by doctor order by sum(count) desc limit %s, %s", date, productWhere, Integer.parseInt(top1) - 1, Integer.parseInt(top2) - Integer.parseInt(top1) + 1);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String slq = String.format("select doctor as x, product as l, sum(count) as y from dep where date = %s and product in (%s) group by doctor, product", date, productWhere);

        valueObjectList = jdbcTemplate.query(slq, new RowMapper<ValueObject>() {
            @Override
            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                ValueObject item = new ValueObject(resultSet);
                return item;
            }
        });
    }

}
