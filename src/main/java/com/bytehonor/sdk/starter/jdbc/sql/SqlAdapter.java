package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.lang.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.match.KeyMatcher;
import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.lang.spring.query.QueryOrder;
import com.bytehonor.sdk.lang.spring.query.QueryPager;
import com.bytehonor.sdk.lang.spring.query.SimpleQueryCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class SqlAdapter {

    public static SqlCondition convert(SimpleQueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        SqlCondition model = SqlCondition.create(condition.getLogic(), pager(condition.getPager()));

        List<KeyMatcher> matchers = condition.getMatchers();
        for (KeyMatcher matcher : matchers) {
            model.add(matcher(matcher));
        }

        model.order(order(condition.getOrder()));
        return model;
    }

    public static SqlCondition convert(QueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        SqlCondition model = SqlCondition.create(condition.getLogic(), pager(condition.getPager()));

        List<KeyMatcher> matchers = condition.getMatchers();
        for (KeyMatcher matcher : matchers) {
            model.add(matcher(matcher));
        }

        model.order(order(condition.getOrder()));
        return model;
    }

    public static SqlOrder order(QueryOrder order) {
        return order != null ? SqlOrder.of(order.getKey(), order.isDesc()) : SqlOrder.non();
    }

    public static SqlPager pager(QueryPager pager) {
        return pager != null ? SqlPager.of(pager.getOffset(), pager.getLimit()) : SqlPager.create();
    }

    public static SqlMatcher matcher(KeyMatcher matcher) {
        Object value = matcher.getValue();
        Object copy = "";
        String javaType = matcher.getType();
        if (SqlOperator.IN.equals(matcher.getOperator())) {
            copy = SqlMatcher.appendIn(SqlAdaptUtils.joinCollection(javaType, value));
        } else if (SqlOperator.LIKE.equals(matcher.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), true, true);
        } else if (SqlOperator.LIKE_LEFT.equals(matcher.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), true, false);
        } else if (SqlOperator.LIKE_RIGHT.equals(matcher.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), false, true);
        } else {
            copy = value;
        }
        return SqlMatcher.create(matcher.getKey(), copy, javaType, matcher.getOperator());
    }
}
