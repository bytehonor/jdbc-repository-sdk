package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.lang.spring.query.QueryCondition;
import com.bytehonor.sdk.lang.spring.query.QueryFilter;
import com.bytehonor.sdk.lang.spring.query.QueryFilter.QueryFilterColumn;
import com.bytehonor.sdk.lang.spring.query.QueryOrder;
import com.bytehonor.sdk.lang.spring.query.QueryOrder.QueryOrderColumn;
import com.bytehonor.sdk.lang.spring.query.QueryPager;
import com.bytehonor.sdk.starter.jdbc.sql.SqlFilter.SqlFilterColumn;
import com.bytehonor.sdk.starter.jdbc.sql.SqlOrder.SqlOrderColumn;
import com.bytehonor.sdk.starter.jdbc.sql.rewrite.KeyRewriter;

public class SqlAdapter {

    public static SqlCondition convert(QueryCondition condition) {
        Objects.requireNonNull(condition, "condition");

        SqlCondition model = SqlCondition.create(condition.getLogic(), toPager(condition.getPager()));

        model.filters(toFilters(condition.getFilter()));
        model.orders(toOrders(condition.getOrder()));
        return model;
    }

    public static SqlCondition convert(QueryCondition condition, KeyRewriter rewriter) {
        Objects.requireNonNull(condition, "condition");

        SqlCondition model = SqlCondition.create(condition.getLogic(), toPager(condition.getPager()), rewriter);

        model.filters(toFilters(condition.getFilter()));
        model.orders(toOrders(condition.getOrder()));
        return model;
    }

    public static List<SqlOrderColumn> toOrders(QueryOrder order) {
        List<SqlOrderColumn> list = new ArrayList<SqlOrderColumn>();
        List<QueryOrderColumn> items = order.getColumns();
        if (CollectionUtils.isEmpty(items)) {
            return list;
        }
        for (QueryOrderColumn item : items) {
            list.add(SqlOrderColumn.of(item.getKey(), item.getSorter()));
        }
        return list;
    }

    public static SqlPager toPager(QueryPager pager) {
        return pager != null ? SqlPager.of(pager.getOffset(), pager.getLimit()) : SqlPager.create();
    }

    public static List<SqlFilterColumn> toFilters(QueryFilter filter) {
        List<SqlFilterColumn> list = new ArrayList<SqlFilterColumn>();
        List<QueryFilterColumn> items = filter.getColumns();
        if (CollectionUtils.isEmpty(items)) {
            return list;
        }

        for (QueryFilterColumn item : items) {
            list.add(SqlFilterColumn.create(item.getKey(), item.getValue(), item.getType(), item.getOperator()));
        }
        return list;
    }

//    public static SqlFilter toFilter(QueryFilter filter) {
//        Object value = filter.getValue();
//        Object copy = "";
//        String javaType = filter.getType();
//        if (SqlOperator.IN.equals(filter.getOperator())) {
//            copy = SqlFilter.appendIn(SqlAdaptUtils.joinCollection(javaType, value));
//        } else if (SqlOperator.LIKE.equals(filter.getOperator())) {
//            copy = SqlInjectUtils.like(value.toString(), true, true);
//        } else if (SqlOperator.LIKE_LEFT.equals(filter.getOperator())) {
//            copy = SqlInjectUtils.like(value.toString(), true, false);
//        } else if (SqlOperator.LIKE_RIGHT.equals(filter.getOperator())) {
//            copy = SqlInjectUtils.like(value.toString(), false, true);
//        } else {
//            copy = value;
//        }
//        return SqlFilter.create(filter.getKey(), copy, javaType, filter.getOperator());
//    }

}
