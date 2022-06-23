package com.bytehonor.sdk.starter.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.define.spring.constant.QueryLogic;
import com.bytehonor.sdk.define.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.util.StringObject;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class SqlArgHolder {

    private static final String BLANK = SqlConstants.BLANK;

    private final QueryLogic logic;

    private final StringBuilder sql;

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
    public SqlArgHolder add(SqlColumn column) {
        Objects.requireNonNull(column, "column");
        Objects.requireNonNull(column.getOperator(), "operator");

        if (column.getValue() == null) {
            return this;
        }

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
        this.sqlTypes.add(column.getType());
        this.javaTypes.add(column.getValue().getClass().getName());
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
