package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by James on 5/4/2017.
 */
public class RoomMultiMonthTop extends ChartObject {
    public RoomMultiMonthTop(String startTime, String endTime, String dep, List<String> productList, String chart, String top1, String top2) {
        productNameList = productList;
        chartName = chart;

        String productWhere = Utils.getProductWhere(productNameList);

        String baseCategorySql = String.format("select doctor from room where date = %s and dep = '%s' and product in (%s) group by doctor order by sum(count) desc limit %s, %s", endTime, dep, productWhere, Utils.getTop1(top1), Utils.getTop2(top1, top2));

        baseCategoryList = jdbcTemplate.queryForList(baseCategorySql, String.class);

        String categorySql = String.format("select date from room where date >= %s and date <= %s group by date order by date", startTime, endTime);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select date as x1, doctor as x2, sum(count) as y, product as l from room where date >= %s and date <= %s and dep = '%s' and product in (%s) group by date, doctor, product", startTime, endTime, dep, productWhere);

        groupValueObjectList = jdbcTemplate.query(sql, new RowMapper<GroupValueObject>() {
            @Override
            public GroupValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new GroupValueObject(resultSet);
            }
        });
    }
}
