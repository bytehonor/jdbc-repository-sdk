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

public class UpdatePrepareStatement extends MysqlPrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePrepareStatement.class);

    private final List<String> updateColumns;
    private final List<Object> updateArgs;

    public UpdatePrepareStatement(Class<?> clazz, QueryCondition condition) {
        super(clazz, condition);
        this.updateColumns = new ArrayList<String>();
        this.updateArgs = new ArrayList<Object>();
    }

    @Override
    public <T> void prepare(T model, ModelMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        ModelGetterGroup<T> group = mapper.create();
        Objects.requireNonNull(group, "group");

        String primary = getTable().getPrimaryKey();
        List<ModelKeyValue> items = group.out(model);
        for (ModelKeyValue item : items) {
            if (primary.equals(item.getKey())) {
                LOG.debug("update {} pass", item.getKey());
                continue;
            }
            if (SqlConstants.CREATE_AT_COLUMN.equals(item.getKey())) {
                LOG.debug("update {} pass", item.getKey());
                continue;
            }
            if (SqlConstants.UPDATE_AT_COLUMN.equals(item.getKey())) {
                LOG.debug("update {} pass", item.getKey());
                continue;
            }
            LOG.debug("update key:{}, value:{}, type:{}", item.getKey(), item.getValue(), item.getType());
            updateColumns.add(item.getKey());
            updateArgs.add(item.getValue());
        }

        if (enabledUpdateAt()) {
            updateColumns.add(SqlConstants.UPDATE_AT_COLUMN);
            updateArgs.add(System.currentTimeMillis());
        }
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

    @Override
    public String sql() {
        if (CollectionUtils.isEmpty(updateColumns)) {
            throw new RuntimeException("update sql updateColumns empty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(table.getTableName()).append(" SET ");
        int idx = 0;
        for (String column : updateColumns) {
            if (idx > 0) {
                sql.append(" , ");
            }
            idx++;
            sql.append(column).append(" = ").append(SqlConstants.PARAM);
        }

        sql.append(SqlStringUtils.toWhereSql(condition.getGroup()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (condition.getGroup() == null) {
            throw new RuntimeException("update sql condition group null");
        }
        if (condition.getGroup().getHolder().isEmpty()) {
            throw new RuntimeException("update sql condition group holder isEmpty");
        }
        List<Object> args = new ArrayList<Object>();
        args.addAll(updateArgs);
        args.addAll(condition.getGroup().args());

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
