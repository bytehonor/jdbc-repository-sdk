package com.bytehonor.sdk.jdbc.bytehonor.jdbc.delete;

import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.define.bytehonor.error.ServerBasicException;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.AbstractStatement;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.SqlConstants;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.SqlInjectUtils;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchColumn;
import com.bytehonor.sdk.lang.bytehonor.string.StringCreator;

public class DeletePreparedStatement implements AbstractStatement {

    private final String table;

    private final DeleteCondition condition;

    private DeletePreparedStatement() {
        this(null, DeleteCondition.create());
    }

    private DeletePreparedStatement(String table, DeleteCondition condition) {
        this.table = table;
        this.condition = condition;
    }

    public static DeletePreparedStatement create(String table) {
        return create(table, DeleteCondition.create());
    }

    public static DeletePreparedStatement create(String table, DeleteCondition condition) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(condition, "condition");
        return new DeletePreparedStatement(table, condition);
    }

    public DeletePreparedStatement match(MatchColumn column) {
        this.condition.and(column);
        return this;
    }

    public String getTable() {
        return table;
    }
    
    public void setLimit(Integer limit) {
        condition.setLimit(limit);
    }

    public String toDeleteRealSql() {
        StringCreator sc = StringCreator.create().append("DELETE FROM ").append(table).append(" WHERE 1=1")
                .append(condition.getMatchHolder().toAndSql());
        if (condition.getLimit() != null) {
            sc.append(" LIMIT ").append(condition.getLimit());
        }
        return sc.toString();
    }

    public String toDeleteLogicSql() {
        StringCreator sc = StringCreator.create().append(SqlConstants.UPDATE).append(table)
                .append(" SET deleted = 1 WHERE 1=1").append(condition.getMatchHolder().toAndSql());
        if (condition.getLimit() != null) {
            sc.append(" LIMIT ").append(condition.getLimit());
        }
        return sc.toString();
    }

    @Override
    public List<Object> listArgs() {
        if (condition.getMatchHolder().getArgs().isEmpty()) {
            throw new ServerBasicException(44, "delete but without any match condition");
        }
        return condition.getMatchHolder().getArgs();
    }

	@Override
	public List<Integer> listTypes() {
		return condition.getMatchHolder().getArgTypes();
	}

	@Override
	public Object[] args() {
		return listArgs().toArray();
	}

	@Override
	public int[] types() {
		return SqlInjectUtils.listArray(listTypes());
	}

}
