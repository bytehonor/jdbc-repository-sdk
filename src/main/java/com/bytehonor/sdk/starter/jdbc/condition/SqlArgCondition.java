package com.bytehonor.sdk.starter.jdbc.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.define.spring.constant.QueryLogic;

public class SqlArgCondition {

    private SqlOrder order;

    private SqlPage page;

    // private final List<SqlColumn> columns;

    private final SqlArgHolder holder;

    private SqlArgCondition(QueryLogic logic, SqlPage page) {
        this.order = null;
        this.page = page;
        // this.columns = new ArrayList<SqlColumn>();
        this.holder = SqlArgHolder.create(logic);
    }

    public static SqlArgCondition create() {
        return create(QueryLogic.AND, SqlPage.create());
    }

    public static SqlArgCondition create(QueryLogic logic) {
        return create(logic, SqlPage.create());
    }

    public static SqlArgCondition create(QueryLogic logic, SqlPage page) {
        Objects.requireNonNull(logic, "logic");

        return new SqlArgCondition(logic, page);
    }

    public static SqlArgCondition id(Long id) {
        Objects.requireNonNull(id, "id");
        SqlArgCondition condition = SqlArgCondition.create(QueryLogic.AND, SqlPage.of(0, 1));
        return condition.eq("id", id);
    }

    public SqlArgCondition eq(String key, String value) {
        return this.safeAdd(SqlColumn.eq(key, value));
    }

    public SqlArgCondition eq(String key, Long value) {
        return this.safeAdd(SqlColumn.eq(key, value));
    }

    public SqlArgCondition eq(String key, Integer value) {
        return this.safeAdd(SqlColumn.eq(key, value));
    }

    public SqlArgCondition eq(String key, Boolean value) {
        return this.safeAdd(SqlColumn.eq(key, value));
    }

    public SqlArgCondition neq(String key, String value) {
        return this.safeAdd(SqlColumn.neq(key, value));
    }

    public SqlArgCondition neq(String key, Long value) {
        return this.safeAdd(SqlColumn.neq(key, value));
    }

    public SqlArgCondition neq(String key, Integer value) {
        return this.safeAdd(SqlColumn.neq(key, value));
    }

    public SqlArgCondition neq(String key, Boolean value) {
        return this.safeAdd(SqlColumn.neq(key, value));
    }

    public SqlArgCondition gt(String key, Long value) {
        return this.safeAdd(SqlColumn.gt(key, value));
    }

    public SqlArgCondition gt(String key, Integer value) {
        return this.safeAdd(SqlColumn.gt(key, value));
    }

    public SqlArgCondition egt(String key, Long value) {
        return this.safeAdd(SqlColumn.egt(key, value));
    }

    public SqlArgCondition egt(String key, Integer value) {
        return this.safeAdd(SqlColumn.egt(key, value));
    }

    public SqlArgCondition lt(String key, Long value) {
        return this.safeAdd(SqlColumn.lt(key, value));
    }

    public SqlArgCondition lt(String key, Integer value) {
        return this.safeAdd(SqlColumn.lt(key, value));
    }

    public SqlArgCondition elt(String key, Long value) {
        return this.safeAdd(SqlColumn.elt(key, value));
    }

    public SqlArgCondition elt(String key, Integer value) {
        return this.safeAdd(SqlColumn.elt(key, value));
    }

    public SqlArgCondition like(String key, String value) {
        return this.safeAdd(SqlColumn.like(key, value));
    }

    public SqlArgCondition likeLeft(String key, String value) {
        return this.safeAdd(SqlColumn.likeLeft(key, value));
    }

    public SqlArgCondition likeRight(String key, String value) {
        return this.safeAdd(SqlColumn.likeRight(key, value));
    }

    public SqlArgCondition strings(String key, Collection<String> value) {
        return this.safeAdd(SqlColumn.strings(key, value));
    }

    public SqlArgCondition longs(String key, Collection<Long> value) {
        return this.safeAdd(SqlColumn.longs(key, value));
    }

    public SqlArgCondition integers(String key, Collection<Integer> value) {
        return this.safeAdd(SqlColumn.integers(key, value));
    }

    public SqlArgCondition descBy(String key) {
        this.order = SqlOrder.descOf(key);
        return this;
    }

    public SqlArgCondition ascBy(String key) {
        this.order = SqlOrder.ascOf(key);
        return this;
    }

    public SqlArgCondition safeAdd(SqlColumn column) {
        // this.columns.add(column);
        this.holder.safeAdd(column);
        return this;
    }

    public static boolean isArgEmpty(SqlArgCondition condition) {
        Objects.requireNonNull(condition, "condition");

        return condition.getHolder().isEmpty();
    }

    public String toSql() {
        if (holder.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ").append(holder.toSql());
        return sb.toString();
    }

    public List<Object> values() {
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

    public SqlOrder getOrder() {
        return order;
    }

    public void setOrder(SqlOrder order) {
        this.order = order;
    }

    public SqlPage getPage() {
        return page;
    }

    public void setPage(SqlPage page) {
        this.page = page;
    }

//    public List<SqlColumn> getColumns() {
//        return columns;
//    }

    public SqlArgHolder getHolder() {
        return holder;
    }

}
