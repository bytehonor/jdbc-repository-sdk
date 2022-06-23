package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.starter.jdbc.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.query.SqlArgGroup;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

public class SelectPrepareStatement extends MysqlPrepareStatement {

    public SelectPrepareStatement(Class<?> clazz, QueryCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(table.getFullColumns()).append(" FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition.getGroup()));
        sql.append(SqlStringUtils.toOrderSql(condition.getOrder()));
        sql.append(SqlStringUtils.toLimitSql(condition.getPage()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlArgGroup.isArgsEmpty(condition.getGroup())) {
            if (condition.getPage() == null) {
                // 禁全表无分页查询
                throw new RuntimeException("select sql condition group args isEmpty");
            }
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
