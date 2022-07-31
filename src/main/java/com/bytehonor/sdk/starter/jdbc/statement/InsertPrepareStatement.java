package com.bytehonor.sdk.starter.jdbc.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetter;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

/**
 * INSERT INTO TableName (columns) VALUE (values)
 * 
 * @author lijianqiang
 *
 */
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
    public <T> List<ModelKeyValue> prepare(T model, ModelGetterMapper<T> mapper) {
        Objects.requireNonNull(model, "model");
        Objects.requireNonNull(mapper, "mapper");

        ModelGetter<T> getter = mapper.create(model);
        Objects.requireNonNull(getter, "getter");

        int keySize = getTable().getKeySet().size();
        List<ModelKeyValue> keyValues = getter.getKeyValues();
        if (keySize - keyValues.size() > 2) {
            LOG.info("miss values! {}, keys:{}, keyValues:{}", getTable().getModelClazz(), keySize, keyValues.size());
        }

        List<ModelKeyValue> result = SqlColumnUtils.prepareInsert(getTable(), keyValues);

        for (ModelKeyValue mkv : result) {
            saveColumns.add(mkv.getKey());
            saveValues.add(mkv.getValue());
            saveTypes.add(SqlAdaptUtils.toSqlType(mkv.getJavaType())); // 转换
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
            throw new JdbcSdkException("insert sql insertColumns empty");
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
            throw new JdbcSdkException("insert sql saveValues empty");
        }
        List<Object> args = new ArrayList<Object>(256);
        args.addAll(saveValues);
        return args.toArray();
    }

    @Override
    public int[] types() {
        if (CollectionUtils.isEmpty(saveTypes)) {
            throw new JdbcSdkException("insert sql saveTypes empty");
        }
        List<Integer> types = new ArrayList<Integer>(256);
        types.addAll(saveTypes);
        return SqlInjectUtils.listArray(types);
    }

}
