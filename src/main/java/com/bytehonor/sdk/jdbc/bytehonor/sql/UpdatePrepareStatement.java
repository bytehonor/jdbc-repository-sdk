package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.jdbc.bytehonor.model.ModelKeyValue;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelGetterGroup;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelMapper;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
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

        List<ModelKeyValue> items = group.out(model);
        for (ModelKeyValue item : items) {
            LOG.info("key:{}, value:{}, type:{}", item.getKey(), item.getValue(), item.getType());
            updateColumns.add(item.getKey());
            updateArgs.add(item.getValue());
        }
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
            sql.append(column).append(" = ?");
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

}
