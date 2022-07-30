package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.lang.spring.util.StringObject;
import com.bytehonor.sdk.starter.jdbc.condition.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

/**
 * SELECT DISTINCT(column) FROM TableName WHERE condition
 * 
 * @author lijianqiang
 *
 */
public class DistinctPrepareStatement extends MysqlPrepareStatement {

    private final String column;

    public DistinctPrepareStatement(Class<?> clazz, String column, SqlCondition condition) {
        super(clazz, condition);
        this.column = SqlColumnUtils.camelToUnderline(column);
    }

    @Override
    public String sql() {
        if (StringObject.isEmpty(column)) {
            throw new JdbcSdkException("DISTINCT column isEmpty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT(").append(column).append(") FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlCondition.isArgEmpty(condition)) {
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
