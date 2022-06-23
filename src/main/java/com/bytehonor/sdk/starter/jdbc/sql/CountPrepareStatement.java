package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.starter.jdbc.query.SqlArgCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

public class CountPrepareStatement extends MysqlPrepareStatement {

    public CountPrepareStatement(Class<?> clazz, SqlArgCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(").append(table.getPrimaryKey()).append(") FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlArgCondition.isArgsEmpty(condition)) {
            return new Object[0];
        }
        return condition.args().toArray();
    }

    @Override
    public int[] types() {
        if (SqlArgCondition.isArgsEmpty(condition)) {
            return new int[0];
        }
        return SqlInjectUtils.listArray(condition.types());
    }

}
