package com.bytehonor.sdk.starter.jdbc.condition;

import java.util.List;

import com.bytehonor.sdk.define.spring.constant.SqlOperator;
import com.bytehonor.sdk.define.spring.query.MatchColumn;
import com.bytehonor.sdk.define.spring.query.QueryCondition;
import com.bytehonor.sdk.define.spring.query.QueryOrder;
import com.bytehonor.sdk.define.spring.query.QueryPage;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class SqlAdapter {

    public static SqlCondition convert(QueryCondition condition) {
        SqlCondition model = SqlCondition.create(condition.getLogic());

        List<MatchColumn> columns = condition.getGroup().getColumns();
        for (MatchColumn column : columns) {
            model.safeAdd(from(column));
        }
        QueryOrder order = condition.getOrder();
        if (order != null) {
            model.setOrder(SqlOrder.of(order.getKey(), order.isDesc()));
        }
        QueryPage page = condition.getPage();
        if (page != null) {
            model.setPage(SqlPage.of(page.getOffset(), page.getLimit()));
        }
        return model;
    }

    public static SqlMatcher from(MatchColumn column) {
        Object value = column.getValue();
        Object copy = "";
        String javaType = column.getType();
        if (SqlOperator.IN.equals(column.getOperator())) {
            copy = SqlMatcher.appendIn(SqlAdaptUtils.joinCollection(javaType, value));
        } else if (SqlOperator.LIKE.equals(column.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), true, true);
        } else if (SqlOperator.LIKE_LEFT.equals(column.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), true, false);
        } else if (SqlOperator.LIKE_RIGHT.equals(column.getOperator())) {
            copy = SqlInjectUtils.like(value.toString(), false, true);
        } else {
            copy = value;
        }
        return SqlMatcher.create(column.getKey(), copy, javaType, column.getOperator());
    }
}
