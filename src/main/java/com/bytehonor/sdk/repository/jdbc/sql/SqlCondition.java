package com.bytehonor.sdk.repository.jdbc.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.framework.lang.constant.QueryLogic;
import com.bytehonor.sdk.repository.jdbc.sql.SqlFilter.SqlFilterColumn;
import com.bytehonor.sdk.repository.jdbc.sql.SqlOrder.SqlOrderColumn;
import com.bytehonor.sdk.repository.jdbc.sql.rewrite.KeyRewriter;
import com.bytehonor.sdk.repository.jdbc.sql.rewrite.UnderlineRewriter;

public class SqlCondition {

    private final SqlPager pager;

    private final SqlWhere where;

    private final SqlOrder order;

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
        return this.filter(SqlFilterColumn.eq(key, value));
    }

    public SqlCondition eq(String key, Long value) {
        return this.filter(SqlFilterColumn.eq(key, value));
    }

    public SqlCondition eq(String key, Integer value) {
        return this.filter(SqlFilterColumn.eq(key, value));
    }

    public SqlCondition eq(String key, Boolean value) {
        return this.filter(SqlFilterColumn.eq(key, value));
    }

    public SqlCondition neq(String key, String value) {
        return this.filter(SqlFilterColumn.neq(key, value));
    }

    public SqlCondition neq(String key, Long value) {
        return this.filter(SqlFilterColumn.neq(key, value));
    }

    public SqlCondition neq(String key, Integer value) {
        return this.filter(SqlFilterColumn.neq(key, value));
    }

    public SqlCondition neq(String key, Boolean value) {
        return this.filter(SqlFilterColumn.neq(key, value));
    }

    public SqlCondition gt(String key, Long value) {
        return this.filter(SqlFilterColumn.gt(key, value));
    }

    public SqlCondition gt(String key, Integer value) {
        return this.filter(SqlFilterColumn.gt(key, value));
    }

    public SqlCondition egt(String key, Long value) {
        return this.filter(SqlFilterColumn.egt(key, value));
    }

    public SqlCondition egt(String key, Integer value) {
        return this.filter(SqlFilterColumn.egt(key, value));
    }

    public SqlCondition lt(String key, Long value) {
        return this.filter(SqlFilterColumn.lt(key, value));
    }

    public SqlCondition lt(String key, Integer value) {
        return this.filter(SqlFilterColumn.lt(key, value));
    }

    public SqlCondition elt(String key, Long value) {
        return this.filter(SqlFilterColumn.elt(key, value));
    }

    public SqlCondition elt(String key, Integer value) {
        return this.filter(SqlFilterColumn.elt(key, value));
    }

    public SqlCondition like(String key, String value) {
        return this.filter(SqlFilterColumn.like(key, value));
    }

    public SqlCondition likeLeft(String key, String value) {
        return this.filter(SqlFilterColumn.likeLeft(key, value));
    }

    public SqlCondition likeRight(String key, String value) {
        return this.filter(SqlFilterColumn.likeRight(key, value));
    }

    public <T> SqlCondition in(String key, Collection<T> value, Class<T> type) {
        return this.filter(SqlFilterColumn.in(key, value, type));
    }

    public SqlCondition filter(SqlFilterColumn filter) {
        this.where.filter(filter);
        return this;
    }

    public SqlCondition filters(List<SqlFilterColumn> list) {
        if (CollectionUtils.isEmpty(list)) {
            return this;
        }
        for (SqlFilterColumn item : list) {
            filter(item);
        }
        return this;
    }

    public boolean canFilter() {
        return getWhere().canFilter();
    }

    public boolean ignoreFilter() {
        return !canFilter();
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

    public boolean canOrder() {
        return order.canOrder();
    }

    public SqlCondition desc(String key) {
        this.order.desc(key);
        return this;
    }

    public SqlCondition asc(String key) {
        this.order.asc(key);
        return this;
    }

    public <T> SqlCondition order(SqlOrderColumn column) {
        this.order.with(column);
        return this;
    }

    public <T> SqlCondition orders(List<SqlOrderColumn> list) {
        if (CollectionUtils.isEmpty(list)) {
            return this;
        }
        for (SqlOrderColumn item : list) {
            order(item);
        }
        return this;
    }

    public boolean unlimited() {
        return this.pager.unlimited();
    }

}
