package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.constant.QueryLogicEnum;

public class SqlConditionGroup {

    private final QueryLogicEnum logic;

    private final List<SqlCondition> conditions;

    private final SqlArgHolder holder;

    private SqlConditionGroup(QueryLogicEnum logic) {
        this.logic = logic;
        this.conditions = new ArrayList<SqlCondition>();
        this.holder = SqlArgHolder.create(logic);
    }

    public static SqlConditionGroup create(QueryLogicEnum logic) {
        Objects.requireNonNull(logic, "logic");

        return new SqlConditionGroup(logic);
    }

    public SqlConditionGroup and(SqlCondition condition) {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(condition.getOperator(), "operator");
        if (StringObject.isEmpty(condition.getKey()) || condition.getValue() == null) {
            return this;
        }
        this.conditions.add(condition);
        this.holder.add(condition);
        return this;
    }

    public String toSql() {
        if (CollectionUtils.isEmpty(conditions)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ").append(holder.toSql());
        return sb.toString();
    }

    public List<Object> args() {
        if (holder == null) {
            return new ArrayList<Object>();
        }
        return holder.getArgs();
    }

    @Override
    public String toString() {
        return toSql();
    }

    public QueryLogicEnum getLogic() {
        return logic;
    }

    public List<SqlCondition> getConditions() {
        return conditions;
    }

    public SqlArgHolder getHolder() {
        return holder;
    }

}
