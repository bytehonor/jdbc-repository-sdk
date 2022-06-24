package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.starter.jdbc.condition.SqlArgCondition;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.model.ModelColumnValue;
import com.bytehonor.sdk.starter.jdbc.model.ModelConvertMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterGroup;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlStringUtils;

public class UpdatePrepareStatement extends MysqlPrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePrepareStatement.class);

    private final List<String> saveColumns;
    private final List<Object> saveValues;
    private final List<Integer> saveTypes;

    public UpdatePrepareStatement(Class<?> clazz, SqlArgCondition condition) {
        super(clazz, condition);
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

        // confilc check
        List<String> filterColumns = condition.getHolder().getKeys();
        List<ModelColumnValue> items = group.spread(model);
        List<ModelColumnValue> result = SqlColumnUtils.prepareUpdate(getTable(), items, filterColumns);

        for (ModelColumnValue val : result) {
            saveColumns.add(val.getColumn());
            saveValues.add(val.getValue());
            saveTypes.add(SqlAdaptUtils.toSqlType(val.getType())); // 转换
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("prepare saveColumns:{}, saveValues:{}", saveColumns.size(), saveValues.size());
        }
        return result;
    }

    @Override
    public String sql() {
        if (CollectionUtils.isEmpty(saveColumns)) {
            throw new JdbcSdkException("update sql updateColumns empty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(table.getTableName()).append(" SET ");
        int idx = 0;
        for (String column : saveColumns) {
            if (idx > 0) {
                sql.append(SqlConstants.CON);
            }
            idx++;
            sql.append(column).append(" = ").append(SqlConstants.PARAM);
        }

        sql.append(SqlStringUtils.toWhereSql(condition));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (CollectionUtils.isEmpty(saveValues)) {
            throw new JdbcSdkException("update sql saveValues empty");
        }
        if (SqlArgCondition.isArgEmpty(condition)) {
            throw new JdbcSdkException("update sql condition args isEmpty");
        }

        List<Object> allArgs = new ArrayList<Object>(256);
        allArgs.addAll(saveValues);
        List<Object> args = condition.values();
        allArgs.addAll(args);

        return allArgs.toArray();
    }

    @Override
    public int[] types() {
        if (CollectionUtils.isEmpty(saveTypes)) {
            throw new JdbcSdkException("update sql saveTypes empty");
        }
        if (SqlArgCondition.isArgEmpty(condition)) {
            throw new JdbcSdkException("update sql condition args isEmpty");
        }

        List<Integer> allTypes = new ArrayList<Integer>(256);
        allTypes.addAll(saveTypes);
        List<Integer> types = condition.types();
        allTypes.addAll(types);

        return SqlInjectUtils.listArray(allTypes);
    }

}
