package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.define.bytehonor.constant.HttpConstants;
import com.bytehonor.sdk.define.bytehonor.error.ServerBasicException;
import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.getter.RequestGetter;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.MatchColumnHolder;

/**
 * 
 * @author lijianqiang
 *
 */
public final class QueryCondition {

    private static final Logger LOG = LoggerFactory.getLogger(QueryCondition.class);

    private int offset;

    private int limit;

    private SqlOrder order;

    private final MatchColumnHolder columnHolder;

    private QueryCondition() {
        this(0, HttpConstants.LIMIT_MAX, null);
    }

    private QueryCondition(int offset, int limit, SqlOrder order) {
        this.offset = offset;
        this.limit = limit;
        this.order = order;
        this.columnHolder = MatchColumnHolder.create();
    }

    public static QueryCondition create() {
        return create(0, HttpConstants.LIMIT_MAX, null);
    }

    public static QueryCondition create(HttpServletRequest request) {
        Objects.requireNonNull(request, "request");
        int offset = RequestGetter.getOffset(request);
        int limit = RequestGetter.getLimit(request);
        return create(offset, limit);
    }

    public static QueryCondition create(int offset, int limit) {
        return create(offset, limit, null);
    }

    public static QueryCondition create(int offset, int limit, SqlOrder order) {
        QueryCondition codition = new QueryCondition();
        codition.setOffset(offset);
        codition.setLimit(limit);
        codition.setOrder(order);
        return codition;
    }

    private QueryCondition and(MatchColumn column) {
        Objects.requireNonNull(column, "column");
        if (StringObject.isEmpty(column.getKey())) {
            throw new ServerBasicException(44, "column key cann't be empty");
        }
        columnHolder.and(column);
        return this;
    }

    public QueryCondition eq(String key, String value) {
        return this.and(MatchColumn.eq(key, value));
    }

    public QueryCondition eq(String key, Long value) {
        return this.and(MatchColumn.eq(key, value));
    }

    public QueryCondition eq(String key, Integer value) {
        return this.and(MatchColumn.eq(key, value));
    }

    public QueryCondition eq(String key, Boolean value) {
        return this.and(MatchColumn.eq(key, value));
    }

    public QueryCondition neq(String key, String value) {
        return this.and(MatchColumn.eq(key, value));
    }

    public QueryCondition neq(String key, Long value) {
        return this.and(MatchColumn.neq(key, value));
    }

    public QueryCondition neq(String key, Integer value) {
        return this.and(MatchColumn.neq(key, value));
    }

    public QueryCondition neq(String key, Boolean value) {
        return this.and(MatchColumn.neq(key, value));
    }

    public QueryCondition gt(String key, Long value) {
        return this.and(MatchColumn.gt(key, value));
    }

    public QueryCondition gt(String key, Integer value) {
        return this.and(MatchColumn.gt(key, value));
    }

    public QueryCondition egt(String key, Long value) {
        return this.and(MatchColumn.egt(key, value));
    }

    public QueryCondition egt(String key, Integer value) {
        return this.and(MatchColumn.egt(key, value));
    }

    public QueryCondition lt(String key, String value) {
        return this.and(MatchColumn.lt(key, value));
    }
    
    public QueryCondition lt(String key, Long value) {
        return this.and(MatchColumn.lt(key, value));
    }

    public QueryCondition lt(String key, Integer value) {
        return this.and(MatchColumn.lt(key, value));
    }

    public QueryCondition elt(String key, Long value) {
        return this.and(MatchColumn.elt(key, value));
    }

    public QueryCondition elt(String key, Integer value) {
        return this.and(MatchColumn.elt(key, value));
    }

    public QueryCondition like(String key, String value) {
        return this.and(MatchColumn.like(key, value));
    }

    public QueryCondition in(String key, List<String> value) {
        return this.and(MatchColumn.in(key, value));
    }

    public QueryCondition in(String key, Set<String> value) {
        return this.and(MatchColumn.in(key, value));
    }

    public QueryCondition inLong(String key, List<Long> value) {
        return this.and(MatchColumn.inLong(key, value));
    }

    public QueryCondition inLong(String key, Set<Long> value) {
        return this.and(MatchColumn.inLong(key, value));
    }

    public QueryCondition inInt(String key, List<Integer> value) {
        return this.and(MatchColumn.inInt(key, value));
    }

    public QueryCondition inInt(String key, Set<Integer> value) {
        return this.and(MatchColumn.inInt(key, value));
    }

    public QueryCondition orderBy(SqlOrder order) {
        this.order = order;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public SqlOrder getOrder() {
        return order;
    }

    public void setOrder(SqlOrder order) {
        this.order = order;
    }

    public MatchColumnHolder getMatchHolder() {
        return columnHolder;
    }

    public String offsetLimitSql() {
        if (limit > HttpConstants.LIMIT_MAX_TOP) {
            LOG.warn("[WARN] limit:{} cann't exceed {}", limit, HttpConstants.LIMIT_MAX_TOP);
            limit = HttpConstants.LIMIT_MAX_TOP;
        }
        StringBuilder sb = new StringBuilder(" LIMIT ").append(offset).append(",").append(limit);
        return sb.toString();
    }

}
