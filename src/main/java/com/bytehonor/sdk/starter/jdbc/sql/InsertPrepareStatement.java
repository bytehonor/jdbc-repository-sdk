package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.model.ModelColumnValue;
import com.bytehonor.sdk.starter.jdbc.model.ModelConvertMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterGroup;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class InsertPrepareStatement extends MysqlPrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(InsertPrepareStatement.class);

    private final List<String> saveColumns;
    private final List<Object> saveValues;
    private final List<Integer> saveTypes;

    public InsertPrepareStatement(Class<?> clazz) {
        super(clazz, null);
        this.saveColumns = new ArrayList<String>();
        this.saveValues = new ArrayList<Object>();
        this.saveTypes = new ArrayList<Integer>();
    }

    @Override
    public <T> List<ModelColumnValue> prepare(T model, ModelConvertMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        ModelGetterGroup<T> group = mapper.create();
        Objects.requireNonNull(group, "group");

        int keySize = getTable().getKeySet().size();
        if (keySize - group.size() > 2) {
            LOG.warn("WARN miss getter! {}, keys:{}, groups:{}", getTable().getModelClazz(), keySize, group.size());
        }

        List<ModelColumnValue> items = group.spread(model);
        List<ModelColumnValue> result = SqlColumnUtils.prepareInsert(getTable(), items);

        for (ModelColumnValue val : result) {
            saveColumns.add(val.getColumn());
            saveValues.add(val.getValue());
            saveTypes.add(SqlAdaptUtils.toSqlType(val.getType())); // 转换
        }

        // 检查参数数目
        int valueSize = saveValues.size();
        if (keySize != valueSize) {
            LOG.debug("NOTICE miss value! {}, keys:{} != values:{}", getTable().getModelClazz(), keySize, valueSize);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("prepare saveColumns:{}, saveValues:{}", saveColumns.size(), valueSize);
        }
        return result;
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
            throw new RuntimeException("insert sql saveValues empty");
        }
        List<Object> args = new ArrayList<Object>(256);
        args.addAll(saveValues);
        return args.toArray();
    }

    @Override
    public int[] types() {
        if (CollectionUtils.isEmpty(saveTypes)) {
            throw new RuntimeException("insert sql saveTypes empty");
        }
        List<Integer> types = new ArrayList<Integer>(256);
        types.addAll(saveTypes);
        return SqlInjectUtils.listArray(types);
    }

}
