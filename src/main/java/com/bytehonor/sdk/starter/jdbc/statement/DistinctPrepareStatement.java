package com.bytehonor.sdk.starter.jdbc.statement;

import com.bytehonor.sdk.lang.spring.function.ClassGetter;
import com.bytehonor.sdk.lang.spring.function.Getters;
import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.SqlFormatter;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

/**
 * SELECT DISTINCT(column) FROM TableName WHERE condition
 * 
 * @author lijianqiang
 *
 */
public class DistinctPrepareStatement extends AbstractPrepareStatement {

    private final String column;

    public <T> DistinctPrepareStatement(Class<T> clazz, ClassGetter<T, ?> getter, SqlCondition condition) {
        super(clazz, condition);
        this.column = SqlColumnUtils.camelToUnderline(Getters.field(getter));
    }

    @Override
    public String sql() {
        if (SpringString.isEmpty(column)) {
            throw new JdbcSdkException("DISTINCT column isEmpty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT(").append(column).append(") FROM ").append(table.getName());

        sql.append(SqlFormatter.toWhereSql(condition));
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
