package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.bytehonor.sdk.define.bytehonor.constant.HttpConstants;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlLogic;

/**
 * 
 * @author lijianqiang
 *
 */
public final class QueryCondition {

    private static int LIMIT_DEF = HttpConstants.LIMIT_DEF;

    private SqlPage page;

    private SqlOrder order;

    private final SqlConditionGroup group;

    private QueryCondition(SqlLogic logic) {
        this.page = SqlPage.create();
        this.order = null;
        this.group = SqlConditionGroup.create(logic);
    }

    public static QueryCondition id(Long id) {
        Objects.requireNonNull(id, "id");

        return and(0, 1).eq("id", id);
    }

    public static QueryCondition create() {
        return and();
    }

    public static QueryCondition create(int offset, int limit) {
        return and(offset, limit);
    }

    public static QueryCondition and() {
        return and(0, LIMIT_DEF);
    }

    public static QueryCondition and(int offset, int limit) {
        return create(SqlLogic.AND, SqlPage.of(offset, limit));
    }

    public static QueryCondition or() {
        return or(0, LIMIT_DEF);
    }

    public static QueryCondition or(int offset, int limit) {
        return create(SqlLogic.OR, SqlPage.of(offset, limit));
    }

    public static QueryCondition create(SqlLogic logic, SqlPage page) {
        Objects.requireNonNull(logic, "logic");
        Objects.requireNonNull(page, "page");

        QueryCondition codition = new QueryCondition(logic);
        codition.setPage(page);
        return codition;
    }

    private QueryCondition doAdd(SqlCondition condition) {
        group.add(condition);
        return this;
    }

    /**
     * 等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition eq(String key, String value) {
        return this.doAdd(SqlCondition.eq(key, value));
    }

    /**
     * 等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition eq(String key, Long value) {
        return this.doAdd(SqlCondition.eq(key, value));
    }

    /**
     * 等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition eq(String key, Integer value) {
        return this.doAdd(SqlCondition.eq(key, value));
    }

    /**
     * 等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition eq(String key, Boolean value) {
        return this.doAdd(SqlCondition.eq(key, value));
    }

    /**
     * 不等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition neq(String key, String value) {
        return this.doAdd(SqlCondition.eq(key, value));
    }

    /**
     * 不等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition neq(String key, Long value) {
        return this.doAdd(SqlCondition.neq(key, value));
    }

    /**
     * 不等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition neq(String key, Integer value) {
        return this.doAdd(SqlCondition.neq(key, value));
    }

    /**
     * 不等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition neq(String key, Boolean value) {
        return this.doAdd(SqlCondition.neq(key, value));
    }

    /**
     * 大于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition gt(String key, Long value) {
        return this.doAdd(SqlCondition.gt(key, value));
    }

    /**
     * 大于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition gt(String key, Integer value) {
        return this.doAdd(SqlCondition.gt(key, value));
    }

    /**
     * 大于等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition egt(String key, Long value) {
        return this.doAdd(SqlCondition.egt(key, value));
    }

    /**
     * 大于等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition egt(String key, Integer value) {
        return this.doAdd(SqlCondition.egt(key, value));
    }

    /**
     * 小于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition lt(String key, Long value) {
        return this.doAdd(SqlCondition.lt(key, value));
    }

    /**
     * 小于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition lt(String key, Integer value) {
        return this.doAdd(SqlCondition.lt(key, value));
    }

    /**
     * 小于等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition elt(String key, Long value) {
        return this.doAdd(SqlCondition.elt(key, value));
    }

    /**
     * 小于等于
     * 
     * @param key
     * @param value
     * @return
     */
    public QueryCondition elt(String key, Integer value) {
        return this.doAdd(SqlCondition.elt(key, value));
    }

    public QueryCondition like(String key, String value) {
        return this.doAdd(SqlCondition.like(key, value));
    }

    public QueryCondition in(String key, List<String> value) {
        return this.doAdd(SqlCondition.in(key, value));
    }

    public QueryCondition in(String key, Set<String> value) {
        return this.doAdd(SqlCondition.in(key, value));
    }

    public QueryCondition inLong(String key, List<Long> value) {
        return this.doAdd(SqlCondition.inLong(key, value));
    }

    public QueryCondition inLong(String key, Set<Long> value) {
        return this.doAdd(SqlCondition.inLong(key, value));
    }

    public QueryCondition inInt(String key, List<Integer> value) {
        return this.doAdd(SqlCondition.inInt(key, value));
    }

    public QueryCondition inInt(String key, Set<Integer> value) {
        return this.doAdd(SqlCondition.inInt(key, value));
    }

    public QueryCondition descBy(String key) {
        this.order = SqlOrder.descOf(key);
        return this;
    }

    public QueryCondition ascBy(String key) {
        this.order = SqlOrder.ascOf(key);
        return this;
    }

    public void setOffset(int offset) {
        this.page.setOffset(offset);
    }

    public void setLimit(int limit) {
        this.page.setLimit(limit);
    }

    public SqlPage getPage() {
        return page;
    }

    public void setPage(SqlPage page) {
        this.page = page;
    }

    public SqlOrder getOrder() {
        return order;
    }

    public void setOrder(SqlOrder order) {
        this.order = order;
    }

    public SqlConditionGroup getGroup() {
        return group;
    }

}
