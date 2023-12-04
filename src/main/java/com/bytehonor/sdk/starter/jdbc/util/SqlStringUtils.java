package com.bytehonor.sdk.starter.jdbc.util;

import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.SqlOrder;
import com.bytehonor.sdk.starter.jdbc.sql.SqlPager;

public class SqlStringUtils {

    public static String toWhereSql(SqlCondition condition) {
        if (condition == null) {
            return "";
        }
        return condition.toSql();
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
