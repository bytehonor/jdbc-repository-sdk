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

    private final QueryLogic logic;

    private final SqlOrder order;

    private final SqlPage page;

    private final List<SqlColumn> columns; // 不要了

    private final SqlArgHolder holder;

    private SqlArgCondition(QueryLogic logic, SqlOrder order, SqlPage page) {
        this.logic = logic;
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
            LOG.debug("put column ignore, key:{}, value:{}", column.getKey(), column.getValue());
            return this;
        }
        this.columns.add(column);
        this.holder.add(column);
        return this;
    }

    public static boolean isArgsEmpty(SqlArgCondition group) {
        Objects.requireNonNull(group, "group");

        return group.getHolder().isEmpty();
    }

    public String toSql() {
        if (CollectionUtils.isEmpty(columns)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ").append(holder.toSql());
        return sb.toString();
    }

    public List<Object> args() {
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

    public QueryLogic getLogic() {
        return logic;
    }

    public SqlOrder getOrder() {
        return order;
    }

    public SqlPage getPage() {
        return page;
    }

    public List<SqlColumn> getColumns() {
        return columns;
    }

    public SqlArgHolder getHolder() {
        return holder;
    }

}
