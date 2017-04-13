package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhanghailin on 2017/4/12.
 */
public class DepOneMonthTop extends ChartObject {

    public DepOneMonthTop(String date, String dep, List<String> productList, String chart, String top1, String top2){
        chartName = chart;
        productNameList = productList;

        String productWhere = Utils.getProductWhere(productNameList);

        String categorySql = String.format("select doctor from dep where date = %s and dep = '%s' and product in (%s) group by doctor order by sum(count) desc limit %s, %s", date, dep, productWhere, Utils.getTop1(top1), Utils.getTop2(top1, top2));

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select doctor as x, product as l, sum(count) as y from dep where date = %s and dep = '%s' and product in (%s) group by doctor, product", date, dep, productWhere);

        valueObjectList = jdbcTemplate.query(sql, new RowMapper<ValueObject>() {
            @Override
            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new ValueObject(resultSet);
            }
        });

    }
}
