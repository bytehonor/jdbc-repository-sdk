package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.lang.spring.constant.QueryLogic;

public class SqlCondition {

    private final SqlOrder order;

    private final SqlPager pager;

    private final SqlArgHolder holder;

    private SqlCondition(QueryLogic logic, SqlPager pager) {
        Objects.requireNonNull(logic, "logic");
        Objects.requireNonNull(pager, "pager");

        this.order = SqlOrder.non();
        this.pager = pager;
        this.holder = SqlArgHolder.create(logic);
    }

    public static SqlCondition create() {
        return new SqlCondition(QueryLogic.AND, SqlPager.create());
    }

    public static SqlCondition create(QueryLogic logic, SqlPager page) {
        return new SqlCondition(logic, page);
    }

    public static SqlCondition id(Long id) {
        Objects.requireNonNull(id, "id");
        SqlCondition condition = SqlCondition.create(QueryLogic.AND, SqlPager.of(0, 1));
        return condition.eq("id", id);
    }

    public SqlCondition eq(String key, String value) {
        return this.doSafeAdd(SqlMatcher.eq(key, value));
    }

    public SqlCondition eq(String key, Long value) {
        return this.doSafeAdd(SqlMatcher.eq(key, value));
    }

    public SqlCondition eq(String key, Integer value) {
        return this.doSafeAdd(SqlMatcher.eq(key, value));
    }

    public SqlCondition eq(String key, Boolean value) {
        return this.doSafeAdd(SqlMatcher.eq(key, value));
    }

    public SqlCondition neq(String key, String value) {
        return this.doSafeAdd(SqlMatcher.neq(key, value));
    }

    public SqlCondition neq(String key, Long value) {
        return this.doSafeAdd(SqlMatcher.neq(key, value));
    }

    public SqlCondition neq(String key, Integer value) {
        return this.doSafeAdd(SqlMatcher.neq(key, value));
    }

    public SqlCondition neq(String key, Boolean value) {
        return this.doSafeAdd(SqlMatcher.neq(key, value));
    }

    public SqlCondition gt(String key, Long value) {
        return this.doSafeAdd(SqlMatcher.gt(key, value));
    }

    public SqlCondition gt(String key, Integer value) {
        return this.doSafeAdd(SqlMatcher.gt(key, value));
    }

    public SqlCondition egt(String key, Long value) {
        return this.doSafeAdd(SqlMatcher.egt(key, value));
    }

    public SqlCondition egt(String key, Integer value) {
        return this.doSafeAdd(SqlMatcher.egt(key, value));
    }

    public SqlCondition lt(String key, Long value) {
        return this.doSafeAdd(SqlMatcher.lt(key, value));
    }

    public SqlCondition lt(String key, Integer value) {
        return this.doSafeAdd(SqlMatcher.lt(key, value));
    }

    public SqlCondition elt(String key, Long value) {
        return this.doSafeAdd(SqlMatcher.elt(key, value));
    }

    public SqlCondition elt(String key, Integer value) {
        return this.doSafeAdd(SqlMatcher.elt(key, value));
    }

    public SqlCondition like(String key, String value) {
        return this.doSafeAdd(SqlMatcher.like(key, value));
    }

    public SqlCondition likeLeft(String key, String value) {
        return this.doSafeAdd(SqlMatcher.likeLeft(key, value));
    }

    public SqlCondition likeRight(String key, String value) {
        return this.doSafeAdd(SqlMatcher.likeRight(key, value));
    }

    public <T> SqlCondition in(String key, Collection<T> value, Class<T> type) {
        return this.doSafeAdd(SqlMatcher.in(key, value, type));
    }

    public <T> SqlCondition in(String key, Collection<T> value, String type) {
        return this.doSafeAdd(SqlMatcher.in(key, value, type));
    }

//    public SqlCondition ins(String key, Collection<String> value) {
//        return this.doSafeAdd(SqlMatcher.ins(key, value));
//    }
//
//    public SqlCondition inl(String key, Collection<Long> value) {
//        return this.doSafeAdd(SqlMatcher.inl(key, value));
//    }
//
//    public SqlCondition ini(String key, Collection<Integer> value) {
//        return this.doSafeAdd(SqlMatcher.ini(key, value));
//    }

    public SqlCondition desc(String key) {
        this.order.desc(key);
        return this;
    }

    public SqlCondition asc(String key) {
        this.order.asc(key);
        return this;
    }

    public SqlCondition doSafeAdd(SqlMatcher column) {
        // this.columns.add(column);
        this.holder.safeAdd(column);
        return this;
    }

    public static boolean isArgEmpty(SqlCondition condition) {
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

    public SqlPager getPager() {
        return pager;
    }

    public SqlArgHolder getHolder() {
        return holder;
    }

    public void offset(int offset) {
        this.pager.setOffset(offset);
    }

    public void limit(int limit) {
        this.pager.setLimit(limit);
    }

    public void order(SqlOrder order) {
        if (order != null) {
            this.order.setKey(order.getKey());
            this.order.setDesc(order.isDesc());
        }
    }

    public boolean isQueryAll() {
        return this.pager.isAll();
    }
}
