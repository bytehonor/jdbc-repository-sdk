package com.bytehonor.sdk.starter.jdbc.statement;

import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.SqlFormatter;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

/**
 * SELECT columns FROM TableName WHERE condition Order Page
 * 
 * @author lijianqiang
 *
 */
public class SelectPrepareStatement extends AbstractPrepareStatement {

    public SelectPrepareStatement(Class<?> clazz, SqlCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(table.getFullColumns()).append(" FROM ").append(table.getName());

        sql.append(SqlFormatter.toWhereSql(condition));
        sql.append(SqlFormatter.toOrderSql(condition.getOrder()));
        sql.append(SqlFormatter.toLimitSql(condition.getPager()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlCondition.isArgEmpty(condition)) {
            if (condition.getPager() == null) {
                // 禁全表无分页查询
                throw new JdbcSdkException("select sql condition args isEmpty");
            }
            return new Object[0];
        }
        return condition.values().toArray();
    }

    @Override
    public int[] types() {
        if (SqlCondition.isArgEmpty(condition)) {
            return new int[0];
        }
        return SqlInjectUtils.listArray(condition.types());
    }
}
