package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.meta.MetaTable;
import com.bytehonor.sdk.starter.jdbc.model.ModelColumnValue;
import com.bytehonor.sdk.starter.jdbc.model.ModelConvertMapper;
import com.bytehonor.sdk.starter.jdbc.query.QueryCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlMetaUtils;

public abstract class MysqlPrepareStatement implements PrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(MysqlPrepareStatement.class);

    protected final Class<?> clazz;

    protected final MetaTable table;

    protected final QueryCondition condition;

    public MysqlPrepareStatement(Class<?> clazz, QueryCondition condition) {
        this.clazz = clazz;
        this.table = SqlMetaUtils.parse(clazz);
        this.condition = condition;
    }

    @Override
    public <T> List<ModelColumnValue> prepare(T model, ModelConvertMapper<T> mapper) {
        return null;
    }

    @Override
    public void check() {
        Object[] args = args();
        int[] types = types();
        int argLength = args.length;
        int typeLength = types.length;
        LOG.debug("args:{}, types:{}", argLength, typeLength);

        if (argLength != typeLength) {
            throw new RuntimeException("args not equals types");
        }

        if (LOG.isDebugEnabled() == false) {
            return;
        }

        for (int i = 0; i < argLength; i++) {
            LOG.debug("arg:{}, type:{}, {}", args[i], types[i], SqlAdaptUtils.toJavaType(types[i]));
        }
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public MetaTable getTable() {
        return table;
    }

    public QueryCondition getCondition() {
        return condition;
    }

}
