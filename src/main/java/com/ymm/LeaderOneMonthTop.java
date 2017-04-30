package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghailin on 2017/4/30.
 */
//当月干保门诊TOP客户容量及份额
public class LeaderOneMonthTop extends ChartObject {
    public LeaderOneMonthTop(String date, String chart, List<String> productList) {
        productNameList = productList;
        chartName = chart;
        String dep1 = "一门诊内科";
        String dep2 = "二门诊内科";

        String productWhere = Utils.getProductWhere(productNameList);

        baseCategoryList = new ArrayList<String>(){{
            add(dep1);
            add(dep2);
        }};

        String categorySql1 = String.format("select dep as date, doctor from dep where date = %s and dep = '一门诊内科' and product in (%s) group by dep, doctor order by sum(count) desc limit 3", date, productWhere);
        String categorySql2 = String.format("select dep as date, doctor from dep where date = %s and dep = '二门诊内科' and product in (%s) group by dep, doctor order by sum(count) desc limit 3", date, productWhere);

        specialCategoryList = jdbcTemplate.query(String.format("(%s) union all (%s)", categorySql1, categorySql2), new RowMapper<SpecialCategoryObject>() {
            @Override
            public SpecialCategoryObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new SpecialCategoryObject(resultSet);
            }
        });

        String sql = String.format("select doctor as x1, dep as x2, product as l, sum(count) as y from dep where date = %s and dep in ('一门诊内科', '二门诊内科') and product in (%s) group by doctor, dep, product", date, productWhere);

        groupValueObjectList = jdbcTemplate.query(sql, new RowMapper<GroupValueObject>() {
            @Override
            public GroupValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new GroupValueObject(resultSet);
            }
        });
    }
}
