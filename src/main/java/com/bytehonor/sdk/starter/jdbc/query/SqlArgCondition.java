package com.bytehonor.sdk.starter.jdbc.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.define.spring.constant.QueryLogic;

public class SqlArgCondition {

    private static final Logger LOG = LoggerFactory.getLogger(SqlArgCondition.class);

    private final SqlOrder order;

    private final SqlPage page;

    private final List<SqlColumn> columns; // 不要了

    private final SqlArgHolder holder;

    private SqlArgCondition(QueryLogic logic, SqlOrder order, SqlPage page) {
        this.order = order;
        this.page = page;
        this.columns = new ArrayList<SqlColumn>();
        this.holder = SqlArgHolder.create(logic);
    }

    // convert
    public static SqlArgCondition create(QueryLogic logic, SqlOrder order, SqlPage page) {
        Objects.requireNonNull(logic, "logic");

        return new SqlArgCondition(logic, order, page);
    }

    public SqlArgCondition add(SqlColumn column) {
        Objects.requireNonNull(column, "column");
        Objects.requireNonNull(column.getOperator(), "operator");

        if (SqlColumn.accept(column) == false) {
            LOG.debug("put SqlColumn ignore, key:{}, value:{}", column.getKey(), column.getValue());
            return this;
        }
        this.columns.add(column);
        this.holder.add(column);
        return this;
    }

    public static boolean isArgEmpty(SqlArgCondition condition) {
        Objects.requireNonNull(condition, "condition");

        return condition.getHolder().isEmpty();
    }

    public String toSql() {
        if (CollectionUtils.isEmpty(columns)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ").append(holder.toSql());
        return sb.toString();
    }

    public List<Object> values() {
        if (holder == null) {
            return new ArrayList<Object>();
        }
        return holder.getValues();
    }

    public List<Integer> types() {
        if (holder == null) {
            return new ArrayList<Integer>();
        }
        return holder.getSqlTypes();
    }

    @Override
    public String toString() {
        return toSql();
    }

    public SqlOrder getOrder() {
        return order;
    }

    public SqlPage getPage() {
        return page;
    }

    public List<SqlColumn> getArgs() {
        return columns;
    }

    public SqlArgHolder getHolder() {
        return holder;
    }

    public static SqlArgCondition id(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
