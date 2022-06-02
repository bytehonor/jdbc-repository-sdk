package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlLogic;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlOperator;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlConstants;

public class SqlArgHolder {

    private final SqlLogic logic;

    private final StringBuilder sql;

    private final List<Object> args;

    private final List<Integer> argTypes;

    private int size;

    private SqlArgHolder(SqlLogic logic) {
        this.logic = logic != null ? logic : SqlLogic.AND;
        this.sql = new StringBuilder();
        this.args = new ArrayList<Object>();
        this.argTypes = new ArrayList<Integer>();
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

        if (StringObject.isEmpty(condition.getKey()) || condition.getValue() == null) {
            return this;
        }
        if (size > 0) {
            this.sql.append(SqlConstants.BLANK).append(logic.getKey()).append(SqlConstants.BLANK);
        }
        size++;

        if (SqlOperator.IN.getKey().equals(condition.getOperator().getKey())) {
            this.sql.append(condition.getKey()).append(SqlConstants.BLANK).append(condition.getOperator().getOpt())
                    .append(SqlConstants.BLANK).append(condition.getValue());
            return this;
        }
        this.sql.append(condition.getKey()).append(SqlConstants.BLANK).append(condition.getOperator().getOpt())
                .append(SqlConstants.BLANK).append(SqlConstants.PARAM);
        this.args.add(condition.getValue());
        this.argTypes.add(condition.getType());
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

    public List<Integer> getArgTypes() {
        return argTypes;
    }

}
