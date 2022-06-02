package com.bytehonor.sdk.jdbc.bytehonor.sql;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.jdbc.bytehonor.query.MatchCondition;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlStringUtils;

public class SelectPrepareStatement extends MysqlPrepareStatement {

    public SelectPrepareStatement(Class<?> clazz, MatchCondition condition) {
        super(clazz, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(table.formColumns()).append(" FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition.getGroup()));
        sql.append(SqlStringUtils.toOrderSql(condition.getOrder()));
        sql.append(SqlStringUtils.toLimitSql(condition.getPage()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (condition.getGroup() == null) {
            return new Object[0];
        }
        if (condition.getGroup() == null || CollectionUtils.isEmpty(condition.getGroup().args())) {
            return new Object[0];
        }
        return condition.getGroup().args().toArray();
    }
}
