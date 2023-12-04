package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;

@Deprecated
public class SqlField<T> {

    private String column;

    private Class<T> type;

    private static void required(String column) {
        if (SpringString.isEmpty(column)) {
            throw new JdbcSdkException("column cannt be empty");
        }
    }

    public static SqlField<Long> longer(String column) {
        required(column);

        SqlField<Long> sd = new SqlField<Long>();
        sd.setColumn(column);
        sd.setType(Long.class);
        return sd;
    }

    public static SqlField<Integer> integer(String column) {
        required(column);

        SqlField<Integer> sd = new SqlField<Integer>();
        sd.setColumn(column);
        sd.setType(Integer.class);
        return sd;
    }

    public static SqlField<String> stringer(String column) {
        required(column);

        SqlField<String> sd = new SqlField<String>();
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
