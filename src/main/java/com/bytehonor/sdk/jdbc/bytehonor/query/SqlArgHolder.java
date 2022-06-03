package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlLogic;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlOperator;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlColumnUtils;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlConstants;

public class SqlArgHolder {

    private static final String BLANK = SqlConstants.BLANK;

    private final SqlLogic logic;

    private final StringBuilder sql;

    private final List<String> columns;

    private final List<Object> values;

    private final List<Integer> sqlTypes;

    private final List<String> javaTypes;

    private int argSize;

    private SqlArgHolder(SqlLogic logic) {
        this.logic = logic != null ? logic : SqlLogic.AND;
        this.sql = new StringBuilder();
        this.columns = new ArrayList<String>();
        this.values = new ArrayList<Object>();
        this.sqlTypes = new ArrayList<Integer>();
        this.javaTypes = new ArrayList<String>();
        this.argSize = 0;
    }

    public static SqlArgHolder create(SqlLogic logic) {
        return new SqlArgHolder(logic);
    }

    /**
     * 
     * @param condition
     * @return
     */
    public SqlArgHolder add(SqlCondition condition) {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(condition.getOperator(), "operator");

        if (condition.getValue() == null) {
            return this;
        }

        String column = SqlColumnUtils.camelToUnderline(condition.getKey());
        if (StringObject.isEmpty(column)) {
            return this;
        }

        this.columns.add(column);

        if (argSize > 0) {
            this.sql.append(BLANK).append(logic.getKey()).append(BLANK);
        }
        argSize++;

        if (SqlOperator.IN.getKey().equals(condition.getOperator().getKey())) {
            this.sql.append(column).append(BLANK).append(condition.getOperator().getOpt()).append(BLANK)
                    .append(condition.getValue());
            return this;
        }
        this.sql.append(column).append(BLANK).append(condition.getOperator().getOpt()).append(BLANK)
                .append(SqlConstants.PARAM);
        this.values.add(condition.getValue());
        this.sqlTypes.add(condition.getType());
        this.javaTypes.add(condition.getValue().getClass().getName());
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

    public SqlLogic getLogic() {
        return logic;
    }

    public int getArgSize() {
        return argSize;
    }

    public List<String> getColumns() {
        return columns;
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
