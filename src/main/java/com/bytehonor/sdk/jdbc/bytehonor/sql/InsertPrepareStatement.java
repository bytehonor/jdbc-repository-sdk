package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.jdbc.bytehonor.model.ModelColumn;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelGetterGroup;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelMapper;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
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
    public <T> void prepare(T model, ModelMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        ModelGetterGroup<T> group = mapper.create();
        Objects.requireNonNull(group, "group");

        List<ModelColumn> items = group.out(model);
        for (ModelColumn item : items) {
            LOG.info("key:{}, value:{}, type:{}", item.getKey(), item.getValue(), item.getType());
            insertColumns.add(item.getKey());
            insertArgs.add(item.getValue());
        }
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
            paramHolder.append("?");
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

}
