package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.define.bytehonor.constant.HttpConstants;
import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlLogicEnum;
import com.bytehonor.sdk.jdbc.bytehonor.getter.RequestGetter;

/**
 * 
 * @author lijianqiang
 *
 */
public final class QueryCondition {

    private static final Logger LOG = LoggerFactory.getLogger(QueryCondition.class);

    private static int LIMIT_DEF = HttpConstants.LIMIT_DEF;

    private SqlPage page;

    private SqlOrder order;

    private final SqlConditionGroup group;

    private QueryCondition(SqlLogicEnum logic) {
        this.page = SqlPage.create();
        this.order = null;
        this.group = SqlConditionGroup.create(logic);
    }

    public static QueryCondition id(Long id) {
        return and(0, 1).eq("id", id);
    }

    public static QueryCondition create() {
        return and();
    }

    public static QueryCondition and() {
        return and(0, LIMIT_DEF);
    }

    public static QueryCondition and(HttpServletRequest request) {
        Objects.requireNonNull(request, "request");
        int offset = RequestGetter.getOffset(request);
        int limit = RequestGetter.getLimit(request);
        return and(offset, limit);
    }

    public static QueryCondition and(int offset, int limit) {
        return create(SqlLogicEnum.AND, SqlPage.of(offset, limit));
    }

    public static QueryCondition or() {
        return or(0, LIMIT_DEF);
    }

    public static QueryCondition or(HttpServletRequest request) {
        Objects.requireNonNull(request, "request");
        int offset = RequestGetter.getOffset(request);
        int limit = RequestGetter.getLimit(request);
        return or(offset, limit);
    }

    public static QueryCondition or(int offset, int limit) {
        return create(SqlLogicEnum.OR, SqlPage.of(offset, limit));
    }

    public static QueryCondition create(SqlLogicEnum logic, SqlPage page) {
        Objects.requireNonNull(logic, "logic");
        Objects.requireNonNull(page, "page");

        QueryCondition codition = new QueryCondition(logic);
        codition.setPage(page);
        return codition;
    }

    private QueryCondition add(SqlCondition condition) {
        if (StringObject.isEmpty(condition.getKey()) || condition.getValue() == null) {
            LOG.warn("put error condition, key:{}, value:{}", condition.getKey(), condition.getValue());
            return this;
        }
        group.and(condition);
        return this;
    }

    public QueryCondition eq(String key, String value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public QueryCondition eq(String key, Long value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public QueryCondition eq(String key, Integer value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public QueryCondition eq(String key, Boolean value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public QueryCondition neq(String key, String value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public QueryCondition neq(String key, Long value) {
        return this.add(SqlCondition.neq(key, value));
    }

    public QueryCondition neq(String key, Integer value) {
        return this.add(SqlCondition.neq(key, value));
    }

    public QueryCondition neq(String key, Boolean value) {
        return this.add(SqlCondition.neq(key, value));
    }

    public QueryCondition gt(String key, Long value) {
        return this.add(SqlCondition.gt(key, value));
    }

    public QueryCondition gt(String key, Integer value) {
        return this.add(SqlCondition.gt(key, value));
    }

    public QueryCondition egt(String key, Long value) {
        return this.add(SqlCondition.egt(key, value));
    }

    public QueryCondition egt(String key, Integer value) {
        return this.add(SqlCondition.egt(key, value));
    }

    public QueryCondition lt(String key, String value) {
        return this.add(SqlCondition.lt(key, value));
    }

    public QueryCondition lt(String key, Long value) {
        return this.add(SqlCondition.lt(key, value));
    }

    public QueryCondition lt(String key, Integer value) {
        return this.add(SqlCondition.lt(key, value));
    }

    public QueryCondition elt(String key, Long value) {
        return this.add(SqlCondition.elt(key, value));
    }

    public QueryCondition elt(String key, Integer value) {
        return this.add(SqlCondition.elt(key, value));
    }

    public QueryCondition like(String key, String value) {
        return this.add(SqlCondition.like(key, value));
    }

    public QueryCondition in(String key, List<String> value) {
        return this.add(SqlCondition.in(key, value));
    }

    public QueryCondition in(String key, Set<String> value) {
        return this.add(SqlCondition.in(key, value));
    }

    public QueryCondition inLong(String key, List<Long> value) {
        return this.add(SqlCondition.inLong(key, value));
    }

    public QueryCondition inLong(String key, Set<Long> value) {
        return this.add(SqlCondition.inLong(key, value));
    }

    public QueryCondition inInt(String key, List<Integer> value) {
        return this.add(SqlCondition.inInt(key, value));
    }

    public QueryCondition inInt(String key, Set<Integer> value) {
        return this.add(SqlCondition.inInt(key, value));
    }

    public QueryCondition descBy(String key) {
        this.order = SqlOrder.descOf(key);
        return this;
    }

    public QueryCondition ascBy(String key) {
        this.order = SqlOrder.ascOf(key);
        return this;
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
