package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhanghailin on 2017/5/2.
 */
public class OneMonthRoomBar extends ChartObject {
    public OneMonthRoomBar(String chart, String date, String top1, String top2, List<String> proList) {
        productNameList = proList;
        chartName = chart;

        String productWhere = Utils.getProductWhere(productNameList);

        String categorySql = String.format("select dep from room where date = %s and product in (%s) group by dep order by sum(count) desc limit %s, %s", date, productWhere, Utils.getTop1(top1), Utils.getTop2(top1, top2));

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select dep as x, product as l, sum(count) as y from room where date = %s and product in (%s) group by dep,product;", date, productWhere);

        valueObjectList = jdbcTemplate.query(sql, new RowMapper<ValueObject>() {
            @Override
            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new ValueObject(resultSet);
            }
        });
    }
}
