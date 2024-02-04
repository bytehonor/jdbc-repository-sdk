package com.bytehonor.sdk.starter.jdbc.statement;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTableLeftJoin;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTableLeftJoinParser;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;
import com.bytehonor.sdk.starter.jdbc.sql.SqlCondition;
import com.bytehonor.sdk.starter.jdbc.sql.SqlFormatter;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

/**
 * 
 * @author lijianqiang
 *
 */
public class LeftJoinPrepareStatement implements PrepareStatement {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPrepareStatement.class);

    protected final Class<?> clazz;

    protected final MetaTableLeftJoin table;

    protected final SqlCondition condition;

    public LeftJoinPrepareStatement(Class<?> clazz, SqlCondition condition) {
        this.clazz = clazz;
        this.table = MetaTableLeftJoinParser.parse(clazz);
        this.condition = condition;
    }

    @Override
    public String sql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(table.getFullColumns());
        sql.append(" FROM ").append(table.getMain().getName()).append(" as m");
        sql.append(" LEFT JOIN ").append(table.getSub().getName()).append(" as s");
        sql.append(" ON m.").append(table.getOn()).append(" = s.").append(table.getOn());
        sql.append(SqlFormatter.toWhereSql(condition.getWhere()));
        sql.append(SqlFormatter.toOrderSql(condition.getOrder()));
        sql.append(SqlFormatter.toLimitSql(condition.getPager()));
        return sql.toString();
    }

    @Override
    public Object[] args() {
        if (SqlCondition.isArgEmpty(condition)) {
            if (condition.getPager() == null) {
                // 禁全表无分页查询
                throw new JdbcSdkException("select sql condition args isEmpty");
            }
            return new Object[0];
        }
        return condition.values().toArray();
    }

    @Override
    public int[] types() {
        if (SqlCondition.isArgEmpty(condition)) {
            return new int[0];
        }
        return SqlInjectUtils.listArray(condition.types());
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
}
