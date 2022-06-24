package com.bytehonor.sdk.starter.jdbc.condition;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.define.spring.constant.QueryLogic;
import com.bytehonor.sdk.define.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.util.StringObject;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class SqlArgHolder {

    private static final Logger LOG = LoggerFactory.getLogger(SqlArgHolder.class);

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

    private int argSize;

    private SqlArgHolder(QueryLogic logic) {
        this.logic = logic != null ? logic : QueryLogic.AND;
        this.sql = new StringBuilder();
        this.keys = new ArrayList<String>();
        this.values = new ArrayList<Object>();
        this.sqlTypes = new ArrayList<Integer>();
        this.javaTypes = new ArrayList<String>();
        this.argSize = 0;
    }

    public static SqlArgHolder create(QueryLogic logic) {
        return new SqlArgHolder(logic);
    }

    /**
     * 
     * @param column
     * @return
     */
    public SqlArgHolder safeAdd(SqlColumn column) {
        if (SqlColumn.accept(column) == false) {
            LOG.warn("put SqlColumn ignore, key:{}, value:{}", column.getKey(), column.getValue());
            return this;
        }

        // 转成下划线
        String key = SqlColumnUtils.camelToUnderline(column.getKey());
        if (StringObject.isEmpty(key)) {
            return this;
        }

        this.keys.add(key);

        if (argSize > 0) {
            this.sql.append(BLANK).append(logic.getKey()).append(BLANK);
        }

        argSize++;

        if (SqlOperator.IN.getKey().equals(column.getOperator().getKey())) {
            this.sql.append(key).append(BLANK).append(column.getOperator().getOpt()).append(BLANK)
                    .append(column.getValue());
            return this;
        }
        this.sql.append(key).append(BLANK).append(column.getOperator().getOpt()).append(BLANK)
                .append(SqlConstants.PARAM);
        this.values.add(column.getValue());
        this.sqlTypes.add(column.getSqlType());
        this.javaTypes.add(column.getJavaType());
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
        return sql.toString();
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
