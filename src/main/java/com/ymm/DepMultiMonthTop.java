package com.ymm;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhanghailin on 2017/4/13.
 */
//多月各门诊TOP客户容量及份额
public class DepMultiMonthTop extends ChartObject {

    public DepMultiMonthTop(String sTime, String eTime, String dep, List<String> productList, String chart, String top1, String top2){
        productNameList = productList;
        chartName = chart;

        String productWhere = Utils.getProductWhere(productNameList);

        String baseCategorySql = String.format("select doctor from dep where date = %s and dep = '%s' and product in (%s) group by doctor order by sum(count) desc limit %s, %s", eTime, dep, productWhere, Utils.getTop1(top1), Utils.getTop2(top1, top2));

        baseCategoryList = jdbcTemplate.queryForList(baseCategorySql, String.class);

        String categorySql = String.format("select date from dep where date >= %s and date <= %s group by date order by date", sTime, eTime);

        categoryList = jdbcTemplate.queryForList(categorySql, String.class);

        String sql = String.format("select date as x1, doctor as x2, sum(count) as y, product as l from dep where date >= %s and date <= %s and dep = '%s' and product in (%s) group by date, doctor, product", sTime, eTime, dep, productWhere);

        groupValueObjectList = jdbcTemplate.query(sql, new RowMapper<GroupValueObject>() {
            @Override
            public GroupValueObject mapRow(ResultSet resultSet, int i) throws SQLException {
                return new GroupValueObject(resultSet);
            }
        });
    }
}
