package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhanghailin on 2017/5/2.
 */
//一年内友谊病房RAAS容量和份额
public class TrendRoomBar extends ChartObject {
    public TrendRoomBar(String name, String startTime, String endTime, List<String> proList) {
        productNameList = proList;
        chartName = name;
        String productWhere = Utils.getProductWhere(productNameList);

        String categorySql = String.format("select date from room where date >= '%s' and date <= '%s' group by date order by date", startTime, endTime);
        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select date as x, sum(count) as y, product as l from room where date >= '%s' and date <= '%s' and product in (%s) group by date, product", startTime, endTime, productWhere);
        valueObjectList = jdbcTemplate.query(sql, new RowMapper<ValueObject>() {
            @Override
            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new ValueObject(resultSet);
            }
        });

    }
}
