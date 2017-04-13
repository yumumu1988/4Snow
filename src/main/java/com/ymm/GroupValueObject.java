package com.ymm;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhanghailin on 2017/4/13.
 */
public class GroupValueObject {
    private String x1;
    private String x2;
    private String y;
    private String legend;

    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getX2() {
        return x2;
    }

    public void setX2(String x2) {
        this.x2 = x2;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public GroupValueObject(ResultSet resultSet) {
        try {
            this.x1 = resultSet.getString("x1");
            this.x2 = resultSet.getString("x2");
            this.y = resultSet.getString("y");
            this.legend = resultSet.getString("l");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
