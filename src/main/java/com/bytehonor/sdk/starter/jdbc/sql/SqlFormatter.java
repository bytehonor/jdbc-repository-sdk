package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;

public class SqlFormatter {

    public static void connect(StringBuilder sb, SqlPart... parts) {
        for (SqlPart part : parts) {
            String sql = part.toSql();
            if (SpringString.isEmpty(sql)) {
                continue;
            }
            sb.append(SqlConstants.BLANK).append(sql);
        }
    }

//    public static String toWhereSql(SqlWhere where) {
//        if (where == null) {
//            return "";
//        }
//        return where.toSql();
//    }
//
//    public static String toOrderSql(SqlOrder order) {
//        if (order == null) {
//            return "";
//        }
//        return order.toSql();
//    }
//
//    public static String toLimitSql(SqlPager page) {
//        if (page == null) {
//            return "";
//        }
//        return page.toSql();
//    }

}
