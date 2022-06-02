package com.bytehonor.sdk.jdbc.bytehonor.sql;

import java.util.List;

import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelKeyValue;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelMapper;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlMetaUtils;

public abstract class MysqlPrepareStatement implements PrepareStatement {

    protected final Class<?> clazz;

    protected final MetaTable table;

    protected final QueryCondition condition;

    public MysqlPrepareStatement(Class<?> clazz, QueryCondition condition) {
        this.clazz = clazz;
        this.table = SqlMetaUtils.parse(clazz);
        this.condition = condition;
    }

    @Override
    public <T> List<ModelKeyValue> prepare(T model, ModelMapper<T> mapper) {
        return null;
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
