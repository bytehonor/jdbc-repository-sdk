package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

import com.bytehonor.sdk.lang.spring.constant.QueryLogic;
import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.sql.SqlFilter.SqlFilterColumn;
import com.bytehonor.sdk.starter.jdbc.sql.rewrite.KeyRewriter;

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

    private int size;

    private SqlWhere(QueryLogic logic, KeyRewriter rewriter) {
        this.logic = logic != null ? logic : QueryLogic.AND;
        this.filter = SqlFilter.plain();
        this.rewriter = rewriter;
        this.sql = new StringBuilder();
        this.keys = new ArrayList<String>(128);
        this.values = new ArrayList<Object>(128);
        this.sqlTypes = new ArrayList<Integer>(128);
        this.javaTypes = new ArrayList<String>(128);
        this.size = 0;
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
//        if (SqlFilterColumn.accept(column) == false) {
//            LOG.warn("SqlFilter ignore, key:{}, value:{}", column.getKey(), column.getValue());
//            return this;
//        }
        filter.with(column);
        doRead(column);
        return this;
    }

    private void doRead(SqlFilterColumn column) {
        // 转成下划线
        String key = rewriter.rewrite(column.getKey());
        if (SpringString.isEmpty(key)) {
            return;
        }

        if (size > 0) {
            this.sql.append(SqlConstants.BLANK).append(logic.getKey()).append(SqlConstants.BLANK);
        }

        size++;
        this.sql.append(key).append(SqlConstants.BLANK).append(column.getOperator().getOpt()).append(SqlConstants.BLANK)
                .append(SqlConstants.PARAM);
        this.keys.add(key);
        this.values.add(SqlFilter.valueOf(column));
        this.sqlTypes.add(column.getSqlType());
        this.javaTypes.add(column.getJavaType());
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

        check();

        StringBuilder sb = new StringBuilder();
        sb.append("WHERE ").append(sql.toString());
        return sb.toString().trim();
    }

    public void check() {
        if (keys.size() != values.size()) {
            throw new JdbcSdkException("keys not match values");
        }
    }

    public QueryLogic getLogic() {
        return logic;
    }

    public int getArgSize() {
        return size;
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
