package com.bytehonor.sdk.starter.jdbc.statement;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTable;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlMetaUtils;

/**
 * @author lijianqiang
 *
 */
public abstract class AbstractJoinStatement implements PrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractJoinStatement.class);

    protected final Class<?> clazzm;

    protected final MetaTable tablem;

    protected final Class<?> clazzs;

    protected final MetaTable tables;

    protected final SqlCondition condition;

    public AbstractJoinStatement(Class<?> clazzm, Class<?> clazzs, SqlCondition condition) {
        this.clazzm = clazzm;
        this.tablem = SqlMetaUtils.parse(clazzm);
        this.clazzs = clazzs;
        this.tables = SqlMetaUtils.parse(clazzs);
        this.condition = condition;
    }

    @Override
    public <T> List<ModelKeyValue> prepare(T model, ModelGetterMapper<T> mapper) {
        return new ArrayList<ModelKeyValue>();
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

    public SqlCondition getCondition() {
        return condition;
    }

}
