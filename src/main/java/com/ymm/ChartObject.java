package com.ymm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 4/12/2017.
 */
public class ChartObject {
//    multiple="multiple"
//    data-live-search="true"
    @Autowired
    protected JdbcTemplate jdbcTemplate = (JdbcTemplate)Application.applicationContext.getBean("jdbcTemplate");

    protected String chartName;
    protected List<String> productNameList;
    protected List<String> categoryList = new ArrayList<>();
    protected List<String> baseCategoryList = new ArrayList<>();
    protected List<ValueObject> valueObjectList = new ArrayList<>();
    protected List<GroupValueObject> groupValueObjectList = new ArrayList<>();

    public String getChartName() {
        return chartName;
    }

    public String getCategories(){
        return Utils.getCategories(categoryList);
    }

    public String getSeries(){
        return Utils.getSeries(productNameList, categoryList, valueObjectList);
    }

    public String getGroupCategories(){
        return Utils.getGroupCategories(baseCategoryList, categoryList);
    }

    public String getGroupSeries(){
        return Utils.getGroupSeries(productNameList, baseCategoryList, categoryList, groupValueObjectList);
    }
}
