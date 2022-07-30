package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.condition.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTable;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlMetaUtils;

/**
 * @author lijianqiang
 *
 */
public abstract class MysqlPrepareStatement implements PrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(MysqlPrepareStatement.class);

    protected final Class<?> clazz;

    protected final MetaTable table;

    protected final SqlCondition condition;

    public MysqlPrepareStatement(Class<?> clazz, SqlCondition condition) {
        this.clazz = clazz;
        this.table = SqlMetaUtils.parse(clazz);
        this.condition = condition;
    }

    @Override
    public <T> List<ModelKeyValue> prepare(T model, ModelGetterMapper<T> mapper) {
        return null;
    }

    @Override
    public void check() {
        Object[] args = args();
        int[] types = types();
        int argLength = args.length;
        int typeLength = types.length;
        LOG.debug("argSize:{}, typeSize:{}", argLength, typeLength);

        if (argLength != typeLength) {
            throw new JdbcSdkException("args not equals types");
        }

        if (LOG.isDebugEnabled()) {
            for (int i = 0; i < argLength; i++) {
                LOG.debug("arg:({}), type:{}, {}", args[i], types[i], SqlAdaptUtils.toJavaType(types[i]));
            }
        }

    }

    public Class<?> getClazz() {
        return clazz;
    }

    public MetaTable getTable() {
        return table;
    }

    public SqlCondition getCondition() {
        return condition;
    }

}
