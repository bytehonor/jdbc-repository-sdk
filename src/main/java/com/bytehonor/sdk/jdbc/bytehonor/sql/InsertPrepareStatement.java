package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlConstants;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelGetterGroup;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelKeyValue;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelMapper;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlInjectUtils;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlStringUtils;

public class InsertPrepareStatement extends MysqlPrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePrepareStatement.class);

    private final List<String> insertColumns;
    private final List<Object> insertArgs;

    public InsertPrepareStatement(Class<?> clazz) {
        super(clazz, QueryCondition.and());
        this.insertColumns = new ArrayList<String>();
        this.insertArgs = new ArrayList<Object>();
    }

    @Override
    public <T> List<ModelKeyValue> prepare(T model, ModelMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        ModelGetterGroup<T> group = mapper.create();
        Objects.requireNonNull(group, "group");

        String primary = getTable().getPrimaryKey();
        List<ModelKeyValue> items = group.out(model);
        for (ModelKeyValue item : items) {
            if (primary.equals(item.getKey())) {
                LOG.debug("insert {} pass", item.getKey());
                continue;
            }
            if (SqlConstants.CREATE_AT_COLUMN.equals(item.getKey())) {
                LOG.debug("insert {} pass", item.getKey());
                continue;
            }
            if (SqlConstants.UPDATE_AT_COLUMN.equals(item.getKey())) {
                LOG.debug("insert {} pass", item.getKey());
                continue;
            }
            LOG.info("key:{}, value:{}, type:{}", item.getKey(), item.getValue(), item.getType());
            insertColumns.add(item.getKey());
            insertArgs.add(item.getValue());
        }

        long now = System.currentTimeMillis();
        if (enabledUpdateAt()) {
            insertColumns.add(SqlConstants.UPDATE_AT_COLUMN);
            insertArgs.add(now);
        }
        if (enabledCreateAt()) {
            insertColumns.add(SqlConstants.CREATE_AT_COLUMN);
            insertArgs.add(now);
        }
        return items;
    }

    private boolean enabledUpdateAt() {
        if (getTable().getKeySet().contains(SqlConstants.UPDATE_AT_KEY)) {
            return true;
        }
        if (getTable().getColumnSet().contains(SqlConstants.UPDATE_AT_COLUMN)) {
            return true;
        }
        return false;
    }

    private boolean enabledCreateAt() {
        if (getTable().getKeySet().contains(SqlConstants.CREATE_AT_KEY)) {
            return true;
        }
        if (getTable().getColumnSet().contains(SqlConstants.CREATE_AT_COLUMN)) {
            return true;
        }
        return false;
    }

    @Override
    public String sql() {
        if (CollectionUtils.isEmpty(insertColumns)) {
            throw new RuntimeException("insert sql insertColumns empty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table.getTableName());
        int idx = 0;
        StringBuilder columnHolder = new StringBuilder();
        StringBuilder paramHolder = new StringBuilder();
        for (String column : insertColumns) {
            if (idx > 0) {
                columnHolder.append(",");
                paramHolder.append(",");
            }
            idx++;
            columnHolder.append(column);
            paramHolder.append(SqlConstants.PARAM);
        }
        sql.append(" (").append(columnHolder.toString()).append(") ");
        sql.append(" VALUES (").append(paramHolder.toString()).append(") ");

        sql.append(SqlStringUtils.toWhereSql(condition.getGroup()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (CollectionUtils.isEmpty(insertArgs)) {
            throw new RuntimeException("insert sql insertArgs empty");
        }
        List<Object> args = new ArrayList<Object>();
        args.addAll(insertArgs);

        return args.toArray();
    }

    @Override
    public int[] types() {
        if (condition.getGroup() == null || CollectionUtils.isEmpty(condition.getGroup().types())) {
            return new int[0];
        }
        return SqlInjectUtils.listArray(condition.getGroup().types());
    }

}
