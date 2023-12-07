package com.bytehonor.sdk.starter.jdbc.statement;

import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.SqlMaker;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

/**
 * SELECT COUNT(PrimaryKey) FROM TableName WHERE condition
 * 
 * @author lijianqiang
 *
 */
public class SelectCountPrepareStatement extends AbstractPrepareStatement {

    public SelectCountPrepareStatement(Class<?> clazz, SqlCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(").append(table.getPrimaryKey()).append(") FROM ").append(table.getTableName());

        sql.append(SqlMaker.toWhereSql(condition));
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
