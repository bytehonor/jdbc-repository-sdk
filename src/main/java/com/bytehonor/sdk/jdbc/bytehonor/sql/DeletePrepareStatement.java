package com.bytehonor.sdk.jdbc.bytehonor.sql;

import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchCondition;

public class DeletePrepareStatement extends MysqlPrepareStatement {

    public DeletePrepareStatement(MetaTable table, MatchCondition condition) {
        super(table, condition);
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(table.getTableName());

        if (condition.getGroup() == null) {
            throw new RuntimeException("delete sql condition group null");
        }
        sql.append(condition.getGroup().toSql());
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (condition.getGroup() == null) {
            throw new RuntimeException("delete sql condition group null");
        }
        if (condition.getGroup().getHolder().isEmpty()) {
            throw new RuntimeException("delete sql condition group holder isEmpty");
        }
        return condition.getGroup().args();
    }

}
