package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.bytehonor.sdk.lang.bytehonor.getter.BooleanGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.DoubleGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.IntegerGetter;
import com.bytehonor.sdk.lang.bytehonor.getter.LongGetter;

public class InsertPrepareStatement extends MysqlPrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePrepareStatement.class);

    private final List<String> insertColumns;
    private final List<Object> insertArgs;

    public InsertPrepareStatement(Class<?> clazz) {
        super(clazz, QueryCondition.and());
        this.insertColumns = new ArrayList<String>();
        this.insertArgs = new ArrayList<Object>();
    }

    public static PreparedStatement convert(PrepareStatement statement, List<ModelKeyValue> items,
            Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(statement.sql(), Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        for (ModelKeyValue item : items) {
            set(ps, idx, item);
            idx++;
        }
        return ps;
    }

    private static void set(PreparedStatement ps, int idx, ModelKeyValue item) throws SQLException {
        if (String.class.getName().equals(item.getType())) {
            ps.setString(idx, item.getValue().toString());
            return;
        }
        if (Long.class.getName().equals(item.getType())) {
            ps.setLong(idx, LongGetter.optional(item.getValue().toString(), 0L));
            return;
        }
        if (Integer.class.getName().equals(item.getType())) {
            ps.setInt(idx, IntegerGetter.optional(item.getValue().toString(), 0));
            return;
        }
        if (Boolean.class.getName().equals(item.getType())) {
            ps.setBoolean(idx, BooleanGetter.optional(item.getValue().toString(), false));
            return;
        }
        if (Double.class.getName().equals(item.getType())) {
            ps.setDouble(idx, DoubleGetter.optional(item.getValue().toString(), 0.0));
            return;
        }
        LOG.error("not support type, set key:{}, value:{}, type:{}", item.getKey(), item.getValue(), item.getType());
        throw new RuntimeException("not support type");
    }

    @Override
    public <T> List<ModelKeyValue> prepare(T model, ModelMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        ModelGetterGroup<T> group = mapper.create();
        Objects.requireNonNull(group, "group");

        List<ModelKeyValue> items = group.out(model);
        for (ModelKeyValue item : items) {
            LOG.info("key:{}, value:{}, type:{}", item.getKey(), item.getValue(), item.getType());
            insertColumns.add(item.getKey());
            insertArgs.add(item.getValue());
        }
        // TODO create_at update_at
        return items;
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
