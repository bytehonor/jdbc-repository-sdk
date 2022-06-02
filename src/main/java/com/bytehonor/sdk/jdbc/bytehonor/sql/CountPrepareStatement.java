package com.bytehonor.sdk.jdbc.bytehonor.sql;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlStringUtils;

public class CountPrepareStatement extends MysqlPrepareStatement {

    public CountPrepareStatement(Class<?> clazz, QueryCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(").append(table.getPrimaryKey()).append(") FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition.getGroup()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (condition.getGroup() == null || CollectionUtils.isEmpty(condition.getGroup().args())) {
            return new Object[0];
        }
        return condition.getGroup().args().toArray();
    }

}
