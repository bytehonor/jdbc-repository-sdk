package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.starter.jdbc.query.SqlArgCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

public class SelectPrepareStatement extends MysqlPrepareStatement {

    public SelectPrepareStatement(Class<?> clazz, SqlArgCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(table.getFullColumns()).append(" FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition));
        sql.append(SqlStringUtils.toOrderSql(condition.getOrder()));
        sql.append(SqlStringUtils.toLimitSql(condition.getPage()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlArgCondition.isArgsEmpty(condition)) {
            if (condition.getPage() == null) {
                // 禁全表无分页查询
                throw new RuntimeException("select sql condition args isEmpty");
            }
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
