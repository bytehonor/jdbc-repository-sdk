package com.bytehonor.sdk.starter.jdbc.sql;

public class SqlFormatter {

    public static Object toWhereSql(SqlWhere where, String prefix) {
        if (where == null) {
            return "";
        }
        return where.toSql(); // TODO
    }

    public static String toWhereSql(SqlWhere where) {
        if (where == null) {
            return "";
        }
        return where.toSql();
    }
    
    public static String toOrderSql(SqlOrder order, String prefix) {
        if (order == null) {
            return "";
        }
        return order.toSql(prefix);
    }

    public static String toOrderSql(SqlOrder order) {
        if (order == null) {
            return "";
        }
        return order.toSql();
    }

    public static String toLimitSql(SqlPager page) {
        if (page == null) {
            return "";
        }
        return page.toSql();
    }

}
