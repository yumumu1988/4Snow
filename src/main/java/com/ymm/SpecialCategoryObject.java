package com.ymm;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhanghailin on 2017/4/17.
 */
public class SpecialCategoryObject {
    private String date;
    private String doctor;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public SpecialCategoryObject(ResultSet resultSet){
        try {
            this.date = resultSet.getString("date");
            this.doctor = resultSet.getString("doctor");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
