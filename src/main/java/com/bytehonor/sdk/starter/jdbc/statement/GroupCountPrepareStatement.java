package com.bytehonor.sdk.starter.jdbc.statement;

import com.bytehonor.sdk.lang.spring.function.ClassGetter;
import com.bytehonor.sdk.lang.spring.function.Getters;
import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

/**
 * SELECT column, COUNT(PrimaryKey) as size FROM TableName WHERE condition GROUP
 * BY column;
 * 
 * @author lijianqiang
 *
 */
public class GroupCountPrepareStatement extends AbstractPrepareStatement {

    private final String column;

    public <T> GroupCountPrepareStatement(Class<T> clazz, ClassGetter<T, ?> getter, SqlCondition condition) {
        super(clazz, condition);
        this.column = SqlColumnUtils.camelToUnderline(Getters.field(getter));
    }

    @Override
    public String sql() {
        if (SpringString.isEmpty(column)) {
            throw new JdbcSdkException("GROUP BY column isEmpty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `").append(column).append("` AS `value`,");
        sql.append(" COUNT(").append(table.getPrimaryKey()).append(") AS `size` FROM ").append(table.getTableName());

        sql.append(SqlStringUtils.toWhereSql(condition));
        sql.append(" GROUP BY `").append(column).append("`");
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
