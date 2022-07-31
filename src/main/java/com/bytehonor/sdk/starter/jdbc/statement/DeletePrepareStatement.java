package com.bytehonor.sdk.starter.jdbc.statement;

import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

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
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(table.getTableName());

        if (condition == null) {
            throw new JdbcSdkException("delete sql condition null");
        }

        sql.append(SqlStringUtils.toWhereSql(condition));
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
