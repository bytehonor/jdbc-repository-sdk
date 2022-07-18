package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.starter.jdbc.condition.SqlArgCondition;
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

    public DistinctPrepareStatement(Class<?> clazz, String column, SqlArgCondition condition) {
        super(clazz, condition);
        this.column = SqlColumnUtils.camelToUnderline(column);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT(").append(column).append(") FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlArgCondition.isArgEmpty(condition)) {
            return new Object[0];
        }
        return condition.values().toArray();
    }

    @Override
    public int[] types() {
        if (SqlArgCondition.isArgEmpty(condition)) {
            return new int[0];
        }
        return SqlInjectUtils.listArray(condition.types());
    }

}
