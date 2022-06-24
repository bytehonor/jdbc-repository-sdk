package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.starter.jdbc.condition.SqlArgCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

public class DeletePrepareStatement extends MysqlPrepareStatement {

    public DeletePrepareStatement(Class<?> clazz, SqlArgCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(table.getTableName());

        if (condition == null) {
            throw new RuntimeException("delete sql condition null");
        }

        sql.append(SqlStringUtils.toWhereSql(condition));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlArgCondition.isArgEmpty(condition)) {
            throw new RuntimeException("delete sql condition args isEmpty");
        }

        return condition.values().toArray();
    }

    @Override
    public int[] types() {
        if (SqlArgCondition.isArgEmpty(condition)) {
            throw new RuntimeException("delete sql condition args isEmpty");
        }
        return SqlInjectUtils.listArray(condition.types());
    }

}
