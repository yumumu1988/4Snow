package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by James on 5/4/2017.
 */
public class RoomOneYearBar extends ChartObject {
    public RoomOneYearBar(String chart, String startTime, String endTime, String dep, List<String> productList) {
        chartName = chart;
        productNameList = productList;

        String productWhere = Utils.getProductWhere(productNameList);

        String categorySql = String.format("select date from room where date >= %s and date <= %s group by date order by date", startTime, endTime);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select date as x, product as l, sum(count) as y from room where date >= %s and date <= %s and dep = '%s' and product in (%s) group by date, product", startTime, endTime, dep, productWhere);

        valueObjectList = jdbcTemplate.query(sql, new RowMapper<ValueObject>() {
            @Override
            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new ValueObject(resultSet);
            }
        });
    }
}
