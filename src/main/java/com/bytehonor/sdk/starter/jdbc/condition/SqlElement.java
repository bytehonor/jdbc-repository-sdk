package com.bytehonor.sdk.starter.jdbc.condition;

public class SqlElement<T> {

    private String column;

    private Class<T> type;

    public static SqlElement<Long> longer(String column) {
        SqlElement<Long> sd = new SqlElement<Long>();
        sd.setColumn(column);
        sd.setType(Long.class);
        return sd;
    }

    public static SqlElement<Integer> integer(String column) {
        SqlElement<Integer> sd = new SqlElement<Integer>();
        sd.setColumn(column);
        sd.setType(Integer.class);
        return sd;
    }

    public static SqlElement<String> stringer(String column) {
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
