package com.bytehonor.sdk.starter.jdbc.util;

import com.bytehonor.sdk.starter.jdbc.condition.SqlArgCondition;
import com.bytehonor.sdk.starter.jdbc.condition.SqlOrder;
import com.bytehonor.sdk.starter.jdbc.condition.SqlPage;

public class SqlStringUtils {

    public static String toWhereSql(SqlArgCondition group) {
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
