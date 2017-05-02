package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhanghailin on 2017/5/2.
 */
//当月TOP病房客户容量份额柱状图
public class OneMonthRoomTopBar extends ChartObject {
    public OneMonthRoomTopBar(String name, String date, String top1, String top2, List<String> proList) {
        chartName = name;
        productNameList = proList;

        String productWhere = Utils.getProductWhere(productNameList);

//        String categorySql = String.format("select doctor from room where date = %s and product in (%s) group by doctor order by sum(count) desc limit %s, %s", date, productWhere, Integer.parseInt(top1) - 1, Integer.parseInt(top2) - Integer.parseInt(top1) + 1);
//
//        categoryList = jdbcTemplate.queryForList(categorySql, String.class);
//
//        String slq = String.format("select doctor as x, product as l, sum(count) as y from room where date = %s and product in (%s) group by doctor, product", date, productWhere);
//
//        valueObjectList = jdbcTemplate.query(slq, new RowMapper<ValueObject>() {
//            @Override
//            public ValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
//                ValueObject item = new ValueObject(resultSet);
//                return item;
//            }
//        });
//
//
        String baseCategorySql = String.format("select doctor from room where date = %s and product in (%s) group by doctor order by sum(count) desc limit %s, %s", date, productWhere, Integer.parseInt(top1) - 1, Integer.parseInt(top2) - Integer.parseInt(top1) + 1);

        baseCategoryList = jdbcTemplate.queryForList(baseCategorySql, String.class);

        String categorySql = String.format("select doctor as date, dep as doctor from room where date = %s and product in (%s) group by doctor, dep order by sum(count) desc limit %s, %s", date, productWhere, Integer.parseInt(top1) - 1, Integer.parseInt(top2) - Integer.parseInt(top1) + 1);

        specialCategoryList = jdbcTemplate.query(categorySql, new RowMapper<SpecialCategoryObject>() {
            @Override
            public SpecialCategoryObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new SpecialCategoryObject(resultSet);
            }
        });


        String sql = String.format("select doctor as x2, dep as x1, product as l, sum(count) as y from room where date = %s and product in (%s) group by doctor, product, dep", date, productWhere);

        groupValueObjectList = jdbcTemplate.query(sql, new RowMapper<GroupValueObject>() {
            @Override
            public GroupValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new GroupValueObject(resultSet);
            }
        });


    }
}
