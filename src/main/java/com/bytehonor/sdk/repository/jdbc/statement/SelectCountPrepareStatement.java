package com.bytehonor.sdk.repository.jdbc.statement;

import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.repository.jdbc.sql.SqlFormatter;
import com.bytehonor.sdk.repository.jdbc.util.SqlInjectUtils;

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
        sql.append("SELECT COUNT(").append(table.getPrimary()).append(") FROM ").append(table.getName());
        SqlFormatter.connect(sql, condition.getWhere());
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (condition.ignoreFilter()) {
            return new Object[0];
        }
        return condition.values().toArray();
    }

    @Override
    public int[] types() {
        if (condition.ignoreFilter()) {
            return new int[0];
        }
        return SqlInjectUtils.listArray(condition.types());
    }

}
