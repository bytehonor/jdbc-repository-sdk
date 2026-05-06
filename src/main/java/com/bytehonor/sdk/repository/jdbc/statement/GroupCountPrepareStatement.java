package com.bytehonor.sdk.repository.jdbc.statement;

import com.bytehonor.sdk.framework.lang.function.ClassGetter;
import com.bytehonor.sdk.framework.lang.function.Getters;
import com.bytehonor.sdk.framework.lang.string.StringKit;
import com.bytehonor.sdk.repository.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.repository.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.repository.jdbc.sql.SqlFormatter;
import com.bytehonor.sdk.repository.jdbc.sql.SqlOrder;
import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.repository.jdbc.util.SqlInjectUtils;

/**
 * SELECT column, COUNT(PrimaryKey) as size FROM TableName WHERE condition GROUP
 * BY column;
 *
 * 分组计数语句构造器。
 *
 * @author lijianqiang
 */
public class GroupCountPrepareStatement extends AbstractPrepareStatement {

    private final String column;

    public <T> GroupCountPrepareStatement(Class<T> clazz, ClassGetter<T, ?> getter, SqlCondition condition) {
        super(clazz, condition);
        this.column = SqlColumnUtils.camelToUnderline(Getters.field(getter));
    }

    @Override
    public String sql() {
        if (StringKit.isEmpty(column)) {
            throw new JdbcSdkException("GROUP BY column isEmpty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT `").append(column).append("` AS `value`,");
        sql.append(" COUNT(").append(table.getPrimary()).append(") AS `size` FROM ").append(table.getName());

        SqlFormatter.connect(sql, condition.getWhere());
        sql.append(" GROUP BY `").append(column).append("` ");
        sql.append(orderBy((condition.getOrder())));
        return sql.toString();
    }

    private String orderBy(SqlOrder order) {
        String sql = order != null ? order.toSql() : "";
        if (StringKit.isEmpty(sql)) {
            // 空排序时显式返回 ORDER BY NULL，避免数据库隐式排序。
            return "ORDER BY NULL";
        }
        return sql;
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
