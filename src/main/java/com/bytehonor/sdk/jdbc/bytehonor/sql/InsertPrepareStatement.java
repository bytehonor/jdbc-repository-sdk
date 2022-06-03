package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlConstants;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelColumnValue;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelConvertMapper;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelGetterGroup;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelSavePrepareResult;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlColumnUtils;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlInjectUtils;

public class InsertPrepareStatement extends MysqlPrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePrepareStatement.class);

    private final List<String> saveColumns;
    private final List<Object> saveValues;

    public InsertPrepareStatement(Class<?> clazz) {
        super(clazz, QueryCondition.and());
        this.saveColumns = new ArrayList<String>();
        this.saveValues = new ArrayList<Object>();
    }

    @Override
    public <T> List<ModelColumnValue> prepare(T model, ModelConvertMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        ModelGetterGroup<T> group = mapper.create();
        Objects.requireNonNull(group, "group");

        List<ModelColumnValue> items = group.spread(model);
        ModelSavePrepareResult result = SqlColumnUtils.prepare(getTable(), items, null);

        saveColumns.addAll(result.getColumns());
        saveValues.addAll(result.getValues());

        // 检查参数数目
        int keySize = getTable().getKeySet().size();
        int valueSize = saveValues.size();
        if (keySize != valueSize) {
            LOG.warn("miss key! {}, keySize:{} != valueSize:{}", getTable().getModelClazz(), keySize, valueSize);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("prepare saveColumns:{}, saveValues:{}", saveColumns.size(), valueSize);
        }
        return items;
    }

    @Override
    public String sql() {
        if (CollectionUtils.isEmpty(saveColumns)) {
            throw new RuntimeException("insert sql insertColumns empty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table.getTableName());
        int idx = 0;
        StringBuilder columnHolder = new StringBuilder();
        StringBuilder paramHolder = new StringBuilder();
        for (String column : saveColumns) {
            if (idx > 0) {
                columnHolder.append(SqlConstants.CON);
                paramHolder.append(SqlConstants.CON);
            }
            idx++;
            columnHolder.append(column);
            paramHolder.append(SqlConstants.PARAM);
        }

        sql.append(" (").append(columnHolder.toString()).append(") VALUES (").append(paramHolder.toString())
                .append(")");
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (CollectionUtils.isEmpty(saveValues)) {
            throw new RuntimeException("insert sql insertArgs empty");
        }
        List<Object> args = new ArrayList<Object>();
        args.addAll(saveValues);
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
