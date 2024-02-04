package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.lang.spring.constant.QueryLogic;
import com.bytehonor.sdk.lang.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class SqlWhere {

    private static final Logger LOG = LoggerFactory.getLogger(SqlWhere.class);

    private static final String BLANK = SqlConstants.BLANK;

    private final QueryLogic logic;

    private final List<SqlFilter> filters;

    private final StringBuilder sql;

    /**
     * 下划线key
     */
    private final List<String> keys;

    private final List<Object> values;

    private final List<Integer> sqlTypes;

    private final List<String> javaTypes;

    private int argSize;

    private SqlWhere(QueryLogic logic) {
        this.logic = logic != null ? logic : QueryLogic.AND;
        this.filters = new ArrayList<SqlFilter>(128);
        this.sql = new StringBuilder();
        this.keys = new ArrayList<String>(128);
        this.values = new ArrayList<Object>(128);
        this.sqlTypes = new ArrayList<Integer>(128);
        this.javaTypes = new ArrayList<String>(128);
        this.argSize = 0;
    }

    public static SqlWhere create(QueryLogic logic) {
        return new SqlWhere(logic);
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
        filters.add(filter);
        return this;

    }

    @Override
    public String toString() {
        return toSql();
    }

    public boolean isEmpty() {
        return argSize < 1;
    }

    public String toSql() {
        if (isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ").append(sql.toString());
        return sb.toString();
    }

    private void doRead(SqlFilter filter) {
        // 转成下划线
        String key = SqlColumnUtils.camelToUnderline(filter.getKey());
        if (SpringString.isEmpty(key)) {
            return;
        }

        this.keys.add(key);

        if (argSize > 0) {
            this.sql.append(BLANK).append(logic.getKey()).append(BLANK);
        }

        argSize++;

        if (SqlOperator.IN.getKey().equals(filter.getOperator().getKey())) {
            this.sql.append(key).append(BLANK).append(filter.getOperator().getOpt()).append(BLANK)
                    .append(filter.getValue());
            return;
        }
        this.sql.append(key).append(BLANK).append(filter.getOperator().getOpt()).append(BLANK)
                .append(SqlConstants.PARAM);
        this.values.add(filter.getValue());
        this.sqlTypes.add(filter.getSqlType());
        this.javaTypes.add(filter.getJavaType());
    }

    public QueryLogic getLogic() {
        return logic;
    }

    public int getArgSize() {
        return argSize;
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
