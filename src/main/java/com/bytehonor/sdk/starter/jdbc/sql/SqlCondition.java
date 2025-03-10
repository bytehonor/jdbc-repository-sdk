package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.lang.spring.constant.QueryLogic;
import com.bytehonor.sdk.starter.jdbc.sql.SqlOrder.SqlOrderColumn;
import com.bytehonor.sdk.starter.jdbc.sql.key.KeyRewriter;
import com.bytehonor.sdk.starter.jdbc.sql.key.UnderlineRewriter;

public class SqlCondition {

    private final SqlWhere where;

    private final SqlOrder order;

    private final SqlPager pager;

    private SqlCondition(QueryLogic logic, SqlPager pager, KeyRewriter rewriter) {
        Objects.requireNonNull(logic, "logic");
        Objects.requireNonNull(pager, "pager");
        Objects.requireNonNull(rewriter, "rewriter");

        this.where = SqlWhere.create(logic, rewriter);
        this.order = SqlOrder.create(rewriter);
        this.pager = pager;
    }

    public static SqlCondition create() {
        return new SqlCondition(QueryLogic.AND, SqlPager.create(), new UnderlineRewriter());
    }

    public static SqlCondition create(QueryLogic logic, SqlPager pager) {
        return new SqlCondition(logic, pager, new UnderlineRewriter());
    }

    public static SqlCondition create(KeyRewriter rewriter) {
        return new SqlCondition(QueryLogic.AND, SqlPager.create(), rewriter);
    }

    public static SqlCondition create(QueryLogic logic, SqlPager pager, KeyRewriter rewriter) {
        return new SqlCondition(logic, pager, rewriter);
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

    public SqlCondition order(List<SqlOrderColumn> columns) {
        this.order.sorts(columns);
        return this;
    }

    public boolean unlimited() {
        return this.pager.unlimited();
    }

    public boolean canOrder() {
        return order.canOrder();
    }
}
