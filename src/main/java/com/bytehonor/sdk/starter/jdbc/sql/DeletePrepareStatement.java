package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.starter.jdbc.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.query.SqlArgGroup;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

public class DeletePrepareStatement extends MysqlPrepareStatement {

    public DeletePrepareStatement(Class<?> clazz, QueryCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(table.getTableName());

        if (condition.getGroup() == null) {
            throw new RuntimeException("delete sql condition group null");
        }

        sql.append(SqlStringUtils.toWhereSql(condition.getGroup()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlArgGroup.isArgsEmpty(condition.getGroup())) {
            throw new RuntimeException("delete sql condition group args isEmpty");
        }

        return condition.getGroup().args().toArray();
    }

    @Override
    public int[] types() {
        if (SqlArgGroup.isArgsEmpty(condition.getGroup())) {
            throw new RuntimeException("delete sql condition group args isEmpty");
        }
        return SqlInjectUtils.listArray(condition.getGroup().types());
    }

}
