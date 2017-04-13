package com.ymm;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by James on 4/12/2017.
 */
public class ValueObject {
    private String x;
    private String y;
    private String legend;

    public ValueObject(ResultSet resultSet) {
        try {
            this.x = resultSet.getString("x");
            this.y = resultSet.getString("y");
            this.legend = resultSet.getString("l");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
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

    public ValueObject() {
    }

    public ValueObject(String x, String y, String legend) {
        this.x = x;
        this.y = y;
        this.legend = legend;
    }
}
