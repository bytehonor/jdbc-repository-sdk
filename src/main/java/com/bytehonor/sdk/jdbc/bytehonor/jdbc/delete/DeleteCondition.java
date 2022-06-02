package com.bytehonor.sdk.jdbc.bytehonor.jdbc.delete;

import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.define.bytehonor.error.ServerBasicException;
import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.MatchColumnHolder;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchColumn;

/**
 * 
 * @author lijianqiang
 *
 */
public final class DeleteCondition {

    private Integer limit;

    private final MatchColumnHolder columnHolder;

    private DeleteCondition() {
        this(null);
    }

    private DeleteCondition(Integer limit) {
        this.limit = limit;
        this.columnHolder = MatchColumnHolder.create();
    }

    public static DeleteCondition create() {
        return create(null);
    }

    public static DeleteCondition create(Integer limit) {
        DeleteCondition codition = new DeleteCondition(limit);
        return codition;
    }

    public DeleteCondition and(MatchColumn column) {
        Objects.requireNonNull(column, "column");
        if (StringObject.isEmpty(column.getKey())) {
            throw new ServerBasicException(44, "column key cann't be empty");
        }
        columnHolder.and(column);
        return this;
    }
    
    public DeleteCondition eq(String key, String value) {
        columnHolder.and(MatchColumn.eq(key, value));
        return this;
    }

    public DeleteCondition eq(String key, Long value) {
        columnHolder.and(MatchColumn.eq(key, value));
        return this;
    }

    public DeleteCondition eq(String key, Integer value) {
        columnHolder.and(MatchColumn.eq(key, value));
        return this;
    }

    public DeleteCondition eq(String key, Boolean value) {
        columnHolder.and(MatchColumn.eq(key, value));
        return this;
    }

    public DeleteCondition neq(String key, String value) {
        columnHolder.and(MatchColumn.eq(key, value));
        return this;
    }

    public DeleteCondition neq(String key, Long value) {
        columnHolder.and(MatchColumn.neq(key, value));
        return this;
    }

    public DeleteCondition neq(String key, Integer value) {
        columnHolder.and(MatchColumn.neq(key, value));
        return this;
    }

    public DeleteCondition neq(String key, Boolean value) {
        columnHolder.and(MatchColumn.neq(key, value));
        return this;
    }

    public DeleteCondition gt(String key, Long value) {
        columnHolder.and(MatchColumn.gt(key, value));
        return this;
    }

    public DeleteCondition gt(String key, Integer value) {
        columnHolder.and(MatchColumn.gt(key, value));
        return this;
    }

    public DeleteCondition egt(String key, Long value) {
        columnHolder.and(MatchColumn.egt(key, value));
        return this;
    }

    public DeleteCondition egt(String key, Integer value) {
        columnHolder.and(MatchColumn.egt(key, value));
        return this;
    }

    public DeleteCondition lt(String key, Long value) {
        columnHolder.and(MatchColumn.lt(key, value));
        return this;
    }

    public DeleteCondition lt(String key, Integer value) {
        columnHolder.and(MatchColumn.lt(key, value));
        return this;
    }

    public DeleteCondition elt(String key, Long value) {
        columnHolder.and(MatchColumn.elt(key, value));
        return this;
    }

    public DeleteCondition elt(String key, Integer value) {
        columnHolder.and(MatchColumn.elt(key, value));
        return this;
    }

    public DeleteCondition like(String key, String value) {
        columnHolder.and(MatchColumn.like(key, value));
        return this;
    }

    public DeleteCondition in(String key, List<String> value) {
        columnHolder.and(MatchColumn.in(key, value));
        return this;
    }

    public DeleteCondition inLong(String key, List<Long> value) {
        columnHolder.and(MatchColumn.inLong(key, value));
        return this;
    }

    public DeleteCondition inInt(String key, List<Integer> value) {
        columnHolder.and(MatchColumn.inInt(key, value));
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public MatchColumnHolder getMatchHolder() {
        return columnHolder;
    }

}
