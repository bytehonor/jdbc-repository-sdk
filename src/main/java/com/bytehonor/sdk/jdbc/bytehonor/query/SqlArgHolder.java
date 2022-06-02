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

    private final List<Object> args;

    private final List<Integer> types;

    private int size;

    private SqlArgHolder(SqlLogic logic) {
        this.logic = logic != null ? logic : SqlLogic.AND;
        this.sql = new StringBuilder();
        this.args = new ArrayList<Object>();
        this.types = new ArrayList<Integer>();
        this.size = 0;
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

        if (size > 0) {
            this.sql.append(BLANK).append(logic.getKey()).append(BLANK);
        }
        size++;

        if (SqlOperator.IN.getKey().equals(condition.getOperator().getKey())) {
            this.sql.append(column).append(BLANK).append(condition.getOperator().getOpt()).append(BLANK)
                    .append(condition.getValue());
            return this;
        }
        this.sql.append(column).append(BLANK).append(condition.getOperator().getOpt()).append(BLANK)
                .append(SqlConstants.PARAM);
        this.args.add(condition.getValue());
        this.types.add(condition.getType());

        return this;
    }

    @Override
    public String toString() {
        return toSql();
    }

    public boolean isEmpty() {
        return size < 1;
    }

    public String toSql() {
        return sql.toString();
    }

    public SqlLogic getLogic() {
        return logic;
    }

    public StringBuilder getSql() {
        return sql;
    }

    public List<Object> getArgs() {
        return args;
    }

    public List<Integer> getTypes() {
        return types;
    }

}
