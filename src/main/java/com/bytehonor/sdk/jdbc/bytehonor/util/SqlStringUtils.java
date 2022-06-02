package com.bytehonor.sdk.jdbc.bytehonor.util;

import com.bytehonor.sdk.jdbc.bytehonor.query.SqlConditionGroup;
import com.bytehonor.sdk.jdbc.bytehonor.query.SqlOrder;
import com.bytehonor.sdk.jdbc.bytehonor.query.SqlPage;

public class SqlStringUtils {

    public static String toWhereSql(SqlConditionGroup group) {
        if (group == null) {
            return "";
        }
        return group.toSql();
    }

    public static String toOrderSql(SqlOrder order) {
        if (order == null) {
            return "";
        }
        return order.toSql();
    }

    public static String toLimitSql(SqlPage page) {
        if (page == null) {
            return "";
        }
        return page.toSql();
    }

}
