package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.lang.spring.constant.QueryLogic;
import com.bytehonor.sdk.lang.spring.string.SpringString;

public class SqlCondition {

    private final SqlWhere where;

    private final SqlOrder order;

    private final SqlPager pager;

    private SqlCondition(QueryLogic logic, SqlPager pager) {
        Objects.requireNonNull(logic, "logic");
        Objects.requireNonNull(pager, "pager");

        this.where = SqlWhere.create(logic);
        this.order = SqlOrder.non();
        this.pager = pager;
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
        return this.add(SqlFilter.eq(key, value));
    }

    public SqlCondition eq(String key, Long value) {
        return this.add(SqlFilter.eq(key, value));
    }

    public SqlCondition eq(String key, Integer value) {
        return this.add(SqlFilter.eq(key, value));
    }

    public SqlCondition eq(String key, Boolean value) {
        return this.add(SqlFilter.eq(key, value));
    }

    public SqlCondition neq(String key, String value) {
        return this.add(SqlFilter.neq(key, value));
    }

    public SqlCondition neq(String key, Long value) {
        return this.add(SqlFilter.neq(key, value));
    }

    public SqlCondition neq(String key, Integer value) {
        return this.add(SqlFilter.neq(key, value));
    }

    public SqlCondition neq(String key, Boolean value) {
        return this.add(SqlFilter.neq(key, value));
    }

    public SqlCondition gt(String key, Long value) {
        return this.add(SqlFilter.gt(key, value));
    }

    public SqlCondition gt(String key, Integer value) {
        return this.add(SqlFilter.gt(key, value));
    }

    public SqlCondition egt(String key, Long value) {
        return this.add(SqlFilter.egt(key, value));
    }

    public SqlCondition egt(String key, Integer value) {
        return this.add(SqlFilter.egt(key, value));
    }

    public SqlCondition lt(String key, Long value) {
        return this.add(SqlFilter.lt(key, value));
    }

    public SqlCondition lt(String key, Integer value) {
        return this.add(SqlFilter.lt(key, value));
    }

    public SqlCondition elt(String key, Long value) {
        return this.add(SqlFilter.elt(key, value));
    }

    public SqlCondition elt(String key, Integer value) {
        return this.add(SqlFilter.elt(key, value));
    }

    public SqlCondition like(String key, String value) {
        return this.add(SqlFilter.like(key, value));
    }

    public SqlCondition likeLeft(String key, String value) {
        return this.add(SqlFilter.likeLeft(key, value));
    }

    public SqlCondition likeRight(String key, String value) {
        return this.add(SqlFilter.likeRight(key, value));
    }

    public <T> SqlCondition in(String key, Collection<T> value, Class<T> type) {
        return this.add(SqlFilter.in(key, value, type));
    }

    public <T> SqlCondition in(String key, Collection<T> value, String type) {
        return this.add(SqlFilter.in(key, value, type));
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

    public SqlCondition add(SqlFilter filter) {
        this.where.safeAdd(filter);
        return this;
    }

    public static boolean isArgEmpty(SqlCondition condition) {
        Objects.requireNonNull(condition, "condition");

        return condition.getWhere().isEmpty();
    }

    public List<Object> values() {
        if (where == null) {
            return new ArrayList<Object>();
        }
        return where.getValues();
    }

    public List<Integer> types() {
        if (where == null) {
            return new ArrayList<Integer>();
        }
        return where.getSqlTypes();
    }

    public SqlOrder getOrder() {
        return order;
    }

    public SqlPager getPager() {
        return pager;
    }

    public SqlWhere getWhere() {
        return where;
    }

    public SqlCondition offset(int offset) {
        this.pager.setOffset(offset);
        return this;
    }

    public SqlCondition limit(int limit) {
        this.pager.setLimit(limit);
        return this;
    }

    public SqlCondition order(SqlOrder order) {
        if (order != null) {
            this.order.setKey(order.getKey());
            this.order.setDesc(order.isDesc());
        }
        return this;
    }

    public SqlCondition orderIfNon(SqlOrder order) {
        if (sorted()) {
            return this;
        }
        return order(order);
    }

    public boolean unlimited() {
        return this.pager.unlimited();
    }

    public boolean sorted() {
        return SpringString.isEmpty(order.getKey()) == false;
    }
}
