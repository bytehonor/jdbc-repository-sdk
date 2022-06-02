package com.bytehonor.sdk.jdbc.bytehonor.sql;

import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchCondition;

public class CountPrepareStatement extends MysqlPrepareStatement {

    public CountPrepareStatement(MetaTable table, MatchCondition condition) {
        super(table, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(").append(table.getPrimaryKey()).append(") FROM ").append(table.getTableName());

        if (condition.getGroup() != null) {
            sql.append(condition.getGroup().toSql());
        }
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (condition.getGroup() == null) {
            return new Object[0];
        }
        return condition.getGroup().args();
    }

}
