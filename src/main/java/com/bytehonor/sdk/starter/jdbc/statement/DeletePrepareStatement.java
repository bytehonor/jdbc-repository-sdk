package com.bytehonor.sdk.starter.jdbc.statement;

import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.SqlFormatter;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

/**
 * DELETE FROM TableName WHERE condition
 * 
 * @author lijianqiang
 *
 */
public class DeletePrepareStatement extends AbstractPrepareStatement {

    public DeletePrepareStatement(Class<?> clazz, SqlCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        if (condition == null) {
            throw new JdbcSdkException("delete sql condition null");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(table.getName());
        sql.append(SqlFormatter.toWhereSql(condition.getWhere()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlCondition.isArgEmpty(condition)) {
            throw new JdbcSdkException("delete sql condition args isEmpty");
        }

        return condition.values().toArray();
    }

    @Override
    public int[] types() {
        if (SqlCondition.isArgEmpty(condition)) {
            throw new JdbcSdkException("delete sql condition args isEmpty");
        }
        return SqlInjectUtils.listArray(condition.types());
    }

}
