package com.bytehonor.sdk.starter.jdbc.sql;

public class SqlFormatter {

//    public static String toWhereSql(SqlCondition condition) {
//        if (condition == null) {
//            return "";
//        }
////        return condition.toSql();
//        return null;
//    }
    
    public static String toWhereSql(SqlWhere where) {
        if (where == null) {
            return "";
        }
        return where.toSql();
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
