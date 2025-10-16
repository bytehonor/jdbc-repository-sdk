package com.bytehonor.sdk.repository.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

import com.bytehonor.sdk.framework.lang.constant.QueryLogic;
import com.bytehonor.sdk.framework.lang.constant.SqlOperator;
import com.bytehonor.sdk.framework.lang.string.StringKit;
import com.bytehonor.sdk.repository.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.repository.jdbc.sql.SqlFilter.SqlFilterColumn;
import com.bytehonor.sdk.repository.jdbc.sql.rewrite.KeyRewriter;

public class SqlWhere implements SqlPart {

    private final QueryLogic logic;

    private final SqlFilter filter;

    private final KeyRewriter rewriter;

    private final StringBuilder sql;

    /**
     * 下划线key
     */
    private final List<String> keys;

    private final List<Object> values;

    private final List<Integer> sqlTypes;

    private final List<String> javaTypes;

    private SqlWhere(QueryLogic logic, KeyRewriter rewriter) {
        this.logic = logic != null ? logic : QueryLogic.AND;
        this.filter = SqlFilter.plain();
        this.rewriter = rewriter;
        this.sql = new StringBuilder();
        this.keys = new ArrayList<String>(128);
        this.values = new ArrayList<Object>(128);
        this.sqlTypes = new ArrayList<Integer>(128);
        this.javaTypes = new ArrayList<String>(128);
    }

    public static SqlWhere create(QueryLogic logic, KeyRewriter rewriter) {
        return new SqlWhere(logic, rewriter);
    }

    /**
     * 
     * @param column
     * @return
     */
    public SqlWhere filter(SqlFilterColumn column) {
        filter.with(column);
        doRead(column);
        return this;
    }

    private void doRead(SqlFilterColumn column) {
        // 转成下划线
        String key = rewriter.rewrite(column.getKey());
        if (StringKit.isEmpty(key)) {
            return;
        }

        if (keys.size() > 0) {
            this.sql.append(SqlConstants.BLANK).append(logic.getKey()).append(SqlConstants.BLANK);
        }
        this.keys.add(key); // keys 只记录

        // in 不支持占位符写法
        if (SqlOperator.IN.equals(column.getOperator())) {
            this.sql.append(SqlFilter.patternIn(key, column.getValue(), column.getJavaType()));
        } else {
            this.sql.append(SqlFilter.patternOf(key, column.getOperator()));
            this.values.add(SqlFilter.valueOf(column));
            this.sqlTypes.add(column.getSqlType());
            this.javaTypes.add(column.getJavaType());
        }

    }

    @Override
    public String toString() {
        return toSql();
    }

    public boolean canFilter() {
        return filter.canFilter();
    }

    @Override
    public String toSql() {
        if (canFilter() == false) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("WHERE ").append(sql.toString());
        return sb.toString().trim();
    }

    public QueryLogic getLogic() {
        return logic;
    }

    public List<String> getKeys() {
        return keys;
    }

    public List<String> getJavaTypes() {
        return javaTypes;
    }

    public List<Object> getValues() {
        return values;
    }

    public List<Integer> getSqlTypes() {
        return sqlTypes;
    }

}
