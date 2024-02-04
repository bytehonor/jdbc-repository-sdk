package com.bytehonor.sdk.starter.jdbc.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetter;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
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
public class InsertPrepareStatement extends AbstractPrepareStatement {

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

        List<ModelKeyValue> keyValues = getter.getKeyValues();
        List<ModelKeyValue> result = SqlColumnUtils.prepareInsert(getTable(), keyValues);

        for (ModelKeyValue mkv : result) {
            saveColumns.add(mkv.getKey());
            saveValues.add(mkv.getValue());
            saveTypes.add(SqlAdaptUtils.toSqlType(mkv.getJavaType())); // 转换
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("prepare saveColumns:{}, saveValues:{}", saveColumns.size(), saveValues.size());
        }
        return result;
    }

    @Override
    public String sql() {
        if (CollectionUtils.isEmpty(saveColumns)) {
            throw new JdbcSdkException("insert sql insertColumns empty");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table.getName());
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
        return saveValues.toArray();
    }

    @Override
    public int[] types() {
        if (CollectionUtils.isEmpty(saveTypes)) {
            throw new JdbcSdkException("insert sql saveTypes empty");
        }
        return SqlInjectUtils.listArray(saveTypes);
    }

}
