package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhanghailin on 2017/4/17.
 */
public class CenterMultiMonthTop extends ChartObject {

    public CenterMultiMonthTop(String sTime, String eTime, List<String> productList, String chart){
        productNameList = productList;
        chartName = chart;
        String dep = "中心透析室门诊";

        String productWhere = Utils.getProductWhere(productNameList);

        String baseCategorySql = String.format("select date from dep where date >= %s and date <= %s group by date order by date", sTime, eTime);

        baseCategoryList = jdbcTemplate.queryForList(baseCategorySql, String.class);

        String categorySql = String.format("select date, doctor from dep where date >= %s and date <= %s and product in (%s) and dep = '%s' group by date, doctor order by sum(count) desc", sTime, eTime, productWhere, dep);

        specialCategoryList = jdbcTemplate.query(categorySql, new RowMapper<SpecialCategoryObject>() {
            @Override
            public SpecialCategoryObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new SpecialCategoryObject(resultSet);
            }
        });

        String sql = String.format("select date as x2, doctor as x1, product as l, sum(count) as y from dep where date >= %s and date <= %s and dep = '%s' and product in (%s) group by date, doctor, product", sTime, eTime, dep, productWhere);

        groupValueObjectList = jdbcTemplate.query(sql, new RowMapper<GroupValueObject>() {
            @Override
            public GroupValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new GroupValueObject(resultSet);
            }
        });

    }
}
