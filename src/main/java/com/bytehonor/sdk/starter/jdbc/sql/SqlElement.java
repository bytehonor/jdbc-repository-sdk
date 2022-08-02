package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.lang.spring.string.StringObject;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;

public class SqlElement<T> {

    private String column;

    private Class<T> type;

    private static void required(String column) {
        if (StringObject.isEmpty(column)) {
            throw new JdbcSdkException("column cannt be empty");
        }
    }

    public static SqlElement<Long> longer(String column) {
        required(column);

        SqlElement<Long> sd = new SqlElement<Long>();
        sd.setColumn(column);
        sd.setType(Long.class);
        return sd;
    }

    public static SqlElement<Integer> integer(String column) {
        required(column);

        SqlElement<Integer> sd = new SqlElement<Integer>();
        sd.setColumn(column);
        sd.setType(Integer.class);
        return sd;
    }

    public static SqlElement<String> stringer(String column) {
        required(column);

        SqlElement<String> sd = new SqlElement<String>();
        sd.setColumn(column);
        sd.setType(String.class);
        return sd;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

}
