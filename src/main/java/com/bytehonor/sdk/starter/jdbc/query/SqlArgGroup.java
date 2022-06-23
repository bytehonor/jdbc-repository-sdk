package com.bytehonor.sdk.starter.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.define.spring.constant.QueryLogic;

public class SqlArgGroup {

    private static final Logger LOG = LoggerFactory.getLogger(SqlArgGroup.class);

    private final QueryLogic logic;

    private final List<SqlCondition> conditions;

    private final SqlArgHolder holder;

    private SqlArgGroup(QueryLogic logic) {
        this.logic = logic;
        this.conditions = new ArrayList<SqlCondition>();
        this.holder = SqlArgHolder.create(logic);
    }

    public static SqlArgGroup create(QueryLogic logic) {
        Objects.requireNonNull(logic, "logic");

        return new SqlArgGroup(logic);
    }

    public SqlArgGroup add(SqlCondition condition) {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(condition.getOperator(), "operator");

        if (SqlCondition.accept(condition) == false) {
            LOG.debug("put condition ignore, key:{}, value:{}", condition.getKey(), condition.getValue());
            return this;
        }
        this.conditions.add(condition);
        this.holder.add(condition);
        return this;
    }

    public static boolean isArgsEmpty(SqlArgGroup group) {
        Objects.requireNonNull(group, "group");

        return group.getHolder().isEmpty();
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
        return holder.getValues();
    }

    public List<Integer> types() {
        if (holder == null) {
            return new ArrayList<Integer>();
        }
        return holder.getSqlTypes();
    }

    @Override
    public String toString() {
        return toSql();
    }

    public QueryLogic getLogic() {
        return logic;
    }

    public List<SqlCondition> getConditions() {
        return conditions;
    }

    public SqlArgHolder getHolder() {
        return holder;
    }

}
