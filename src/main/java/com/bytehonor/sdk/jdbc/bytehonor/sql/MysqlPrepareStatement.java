package com.bytehonor.sdk.jdbc.bytehonor.sql;

import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaParseUtils;
import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelMapper;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchCondition;

public abstract class MysqlPrepareStatement implements PrepareStatement {

    protected final Class<?> clazz;

    protected final MetaTable table;

    protected final MatchCondition condition;

    public MysqlPrepareStatement(Class<?> clazz, MatchCondition condition) {
        this.clazz = clazz;
        this.table = MetaParseUtils.parse(clazz);
        this.condition = condition;
    }

    @Override
    public <T> void prepare(T model, ModelMapper<T> mapper) {

    }

    public Class<?> getClazz() {
        return clazz;
    }

    public MetaTable getTable() {
        return table;
    }

    public MatchCondition getCondition() {
        return condition;
    }

}
