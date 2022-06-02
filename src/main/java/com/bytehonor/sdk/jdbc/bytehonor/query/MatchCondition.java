package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.define.bytehonor.constant.HttpConstants;
import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.constant.QueryLogicEnum;
import com.bytehonor.sdk.jdbc.bytehonor.getter.RequestGetter;

/**
 * 
 * @author lijianqiang
 *
 */
public final class MatchCondition {

    private static final Logger LOG = LoggerFactory.getLogger(MatchCondition.class);

    private static int LIMIT_DEF = HttpConstants.LIMIT_DEF;

    private SqlPage page;

    private SqlOrder order;

    private final SqlConditionGroup group;

    private MatchCondition(QueryLogicEnum logic) {
        this.page = SqlPage.create();
        this.order = null;
        this.group = SqlConditionGroup.create(logic);
    }

    public static MatchCondition id(Long id) {
        return and(0, 1).eq("id", id);
    }

    public static MatchCondition create() {
        return and();
    }

    public static MatchCondition and() {
        return and(0, LIMIT_DEF);
    }

    public static MatchCondition and(HttpServletRequest request) {
        Objects.requireNonNull(request, "request");
        int offset = RequestGetter.getOffset(request);
        int limit = RequestGetter.getLimit(request);
        return and(offset, limit);
    }

    public static MatchCondition and(int offset, int limit) {
        return create(QueryLogicEnum.AND, SqlPage.of(offset, limit));
    }

    public static MatchCondition or() {
        return or(0, LIMIT_DEF);
    }

    public static MatchCondition or(HttpServletRequest request) {
        Objects.requireNonNull(request, "request");
        int offset = RequestGetter.getOffset(request);
        int limit = RequestGetter.getLimit(request);
        return or(offset, limit);
    }

    public static MatchCondition or(int offset, int limit) {
        return create(QueryLogicEnum.OR, SqlPage.of(offset, limit));
    }

    public static MatchCondition create(QueryLogicEnum logic, SqlPage page) {
        Objects.requireNonNull(logic, "logic");
        Objects.requireNonNull(page, "page");

        MatchCondition codition = new MatchCondition(logic);
        codition.setPage(page);
        return codition;
    }

    private MatchCondition add(SqlCondition condition) {
        if (StringObject.isEmpty(condition.getKey()) || condition.getValue() == null) {
            LOG.warn("put error condition, key:{}, value:{}", condition.getKey(), condition.getValue());
            return this;
        }
        group.and(condition);
        return this;
    }

    public MatchCondition eq(String key, String value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public MatchCondition eq(String key, Long value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public MatchCondition eq(String key, Integer value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public MatchCondition eq(String key, Boolean value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public MatchCondition neq(String key, String value) {
        return this.add(SqlCondition.eq(key, value));
    }

    public MatchCondition neq(String key, Long value) {
        return this.add(SqlCondition.neq(key, value));
    }

    public MatchCondition neq(String key, Integer value) {
        return this.add(SqlCondition.neq(key, value));
    }

    public MatchCondition neq(String key, Boolean value) {
        return this.add(SqlCondition.neq(key, value));
    }

    public MatchCondition gt(String key, Long value) {
        return this.add(SqlCondition.gt(key, value));
    }

    public MatchCondition gt(String key, Integer value) {
        return this.add(SqlCondition.gt(key, value));
    }

    public MatchCondition egt(String key, Long value) {
        return this.add(SqlCondition.egt(key, value));
    }

    public MatchCondition egt(String key, Integer value) {
        return this.add(SqlCondition.egt(key, value));
    }

    public MatchCondition lt(String key, String value) {
        return this.add(SqlCondition.lt(key, value));
    }

    public MatchCondition lt(String key, Long value) {
        return this.add(SqlCondition.lt(key, value));
    }

    public MatchCondition lt(String key, Integer value) {
        return this.add(SqlCondition.lt(key, value));
    }

    public MatchCondition elt(String key, Long value) {
        return this.add(SqlCondition.elt(key, value));
    }

    public MatchCondition elt(String key, Integer value) {
        return this.add(SqlCondition.elt(key, value));
    }

    public MatchCondition like(String key, String value) {
        return this.add(SqlCondition.like(key, value));
    }

    public MatchCondition in(String key, List<String> value) {
        return this.add(SqlCondition.in(key, value));
    }

    public MatchCondition in(String key, Set<String> value) {
        return this.add(SqlCondition.in(key, value));
    }

    public MatchCondition inLong(String key, List<Long> value) {
        return this.add(SqlCondition.inLong(key, value));
    }

    public MatchCondition inLong(String key, Set<Long> value) {
        return this.add(SqlCondition.inLong(key, value));
    }

    public MatchCondition inInt(String key, List<Integer> value) {
        return this.add(SqlCondition.inInt(key, value));
    }

    public MatchCondition inInt(String key, Set<Integer> value) {
        return this.add(SqlCondition.inInt(key, value));
    }

    public MatchCondition descBy(String key) {
        this.order = SqlOrder.descOf(key);
        return this;
    }

    public MatchCondition ascBy(String key) {
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
