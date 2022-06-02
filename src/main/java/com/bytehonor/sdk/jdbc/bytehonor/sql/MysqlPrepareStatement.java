package com.bytehonor.sdk.jdbc.bytehonor.sql;

import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchCondition;

public abstract class MysqlPrepareStatement implements PrepareStatement {

    protected final MetaTable table;

    protected final MatchCondition condition;

    public MysqlPrepareStatement(MetaTable table, MatchCondition condition) {
        this.table = table;
        this.condition = condition;
    }

    public MetaTable getTable() {
        return table;
    }

    public MatchCondition getCondition() {
        return condition;
    }

}
