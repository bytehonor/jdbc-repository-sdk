package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.lang.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.lang.spring.query.QueryFilter;
import com.bytehonor.sdk.lang.spring.query.QueryOrder;
import com.bytehonor.sdk.lang.spring.query.QueryPager;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.sql.key.KeyRewriter;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class SqlAdapter {

    public static SqlCondition convert(QueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        SqlCondition model = SqlCondition.create(condition.getLogic(), pager(condition.getPager()));

        List<QueryFilter> filters = condition.listFilters();
        for (QueryFilter filter : filters) {
            model.add(filter(filter));
        }

        model.order(order(condition.getOrder()));
        return model;
    }

    public static SqlCondition convert(QueryCondition condition, KeyRewriter rewriter) {
        Objects.requireNonNull(condition, "condition");

        SqlCondition model = SqlCondition.create(condition.getLogic(), pager(condition.getPager()), rewriter);

        List<QueryFilter> filters = condition.listFilters();
        for (QueryFilter filter : filters) {
            model.add(filter(filter));
        }

        model.order(order(condition.getOrder()));
        return model;
    }

    public static List<SqlOrderColumn> order(QueryOrder order) {
        List<SqlOrderColumn> columns = new ArrayList<SqlOrderColumn>();
        if (order != null) {
            columns.add(SqlOrderColumn.of(order.getKey(), order.isDesc() ? SqlConstants.DESC : SqlConstants.ASC));
        }
        return columns;
    }

    public static SqlPager pager(QueryPager pager) {
        return pager != null ? SqlPager.of(pager.getOffset(), pager.getLimit()) : SqlPager.create();
    }

    public static SqlFilter filter(QueryFilter filter) {
        Object value = filter.getValue();
        Object copy = "";
        String javaType = filter.getType();
        if (SqlOperator.IN.equals(filter.getOperator())) {
            copy = SqlFilter.appendIn(SqlAdaptUtils.joinCollection(javaType, value));
        } else if (SqlOperator.LIKE.equals(filter.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), true, true);
        } else if (SqlOperator.LIKE_LEFT.equals(filter.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), true, false);
        } else if (SqlOperator.LIKE_RIGHT.equals(filter.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), false, true);
        } else {
            copy = value;
        }
        return SqlFilter.create(filter.getKey(), copy, javaType, filter.getOperator());
    }
}
