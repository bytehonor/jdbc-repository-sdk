package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.starter.jdbc.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.query.SqlArgGroup;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

public class CountPrepareStatement extends MysqlPrepareStatement {

    public CountPrepareStatement(Class<?> clazz, QueryCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(").append(table.getPrimaryKey()).append(") FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition.getGroup()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlArgGroup.isArgsEmpty(condition.getGroup())) {
            return new Object[0];
        }
        return condition.getGroup().args().toArray();
    }

    @Override
    public int[] types() {
        if (SqlArgGroup.isArgsEmpty(condition.getGroup())) {
            return new int[0];
        }
        return SqlInjectUtils.listArray(condition.getGroup().types());
    }

}
