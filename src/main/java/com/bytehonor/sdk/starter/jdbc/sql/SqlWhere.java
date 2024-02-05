package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.constant.QueryLogic;
import com.bytehonor.sdk.lang.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.sql.key.KeyRewriter;

public class SqlWhere {

    private static final Logger LOG = LoggerFactory.getLogger(SqlWhere.class);

    private static final String BLANK = SqlConstants.BLANK;

    private final QueryLogic logic;

    private final StringBuilder sql;

    /**
     * 下划线key
     */
    private final List<String> keys;

    private final List<Object> values;

    private final List<Integer> sqlTypes;

    private final List<String> javaTypes;

    private final KeyRewriter rewriter;

    private int size;

    private SqlWhere(QueryLogic logic, KeyRewriter rewriter) {
        this.logic = logic != null ? logic : QueryLogic.AND;
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
     * @param filter
     * @return
     */
    public SqlWhere safeAdd(SqlFilter filter) {
        if (SqlFilter.accept(filter) == false) {
            LOG.warn("SqlFilter ignore, key:{}, value:{}", filter.getKey(), filter.getValue());
            return this;
        }
        doRead(filter);
        return this;
    }

    private void doRead(SqlFilter filter) {
        // 转成下划线
        String key = rewriter.rewrite(filter.getKey());
        if (SpringString.isEmpty(key)) {
            return;
        }

        this.keys.add(key);

        if (size > 0) {
            this.sql.append(BLANK).append(logic.getKey()).append(BLANK);
        }

        size++;

        // in值直接拼在sql语句中
        if (SqlOperator.IN.getKey().equals(filter.getOperator().getKey())) {
            this.sql.append(key).append(BLANK).append(filter.getOperator().getOpt()).append(BLANK)
                    .append(filter.getValue());
        } else {
            this.sql.append(key).append(BLANK).append(filter.getOperator().getOpt()).append(BLANK)
                    .append(SqlConstants.PARAM);
            this.values.add(filter.getValue());
            this.sqlTypes.add(filter.getSqlType());
            this.javaTypes.add(filter.getJavaType());
        }
    }

    @Override
    public String toString() {
        return toSql();
    }

    public boolean isEmpty() {
        return size < 1;
    }

    public String toSql() {
        if (isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ").append(sql.toString());
        return sb.toString();
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
