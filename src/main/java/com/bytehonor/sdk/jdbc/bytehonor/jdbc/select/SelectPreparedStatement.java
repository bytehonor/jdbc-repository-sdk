package com.bytehonor.sdk.jdbc.bytehonor.jdbc.select;

import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.jdbc.bytehonor.jdbc.AbstractStatement;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.SqlConstants;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.SqlInjectUtils;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;
import com.bytehonor.sdk.lang.bytehonor.string.StringCreator;

public class SelectPreparedStatement implements AbstractStatement {

    private final String table;

    private final String primary;

    private final String columns;

    private final QueryCondition condition;

    private SelectPreparedStatement(String table, String primary, String columns, QueryCondition condition) {
        this.table = table;
        this.primary = primary;
        this.columns = columns;
        this.condition = condition;
    }

    /**
     * @param table
     * @param primary
     * @param columns
     * @param condition
     * @return
     */
    public static SelectPreparedStatement create(String table, String primary, String columns,
            QueryCondition condition) {
        Objects.requireNonNull(table, "table");
        Objects.requireNonNull(primary, "primary");
        Objects.requireNonNull(columns, "columns");
        Objects.requireNonNull(condition, "condition");
        if (columns.startsWith(primary) == false) {
            columns = StringCreator.create().append(primary).append(",").append(columns).toString();
        }
        return new SelectPreparedStatement(table, primary, columns, condition);
    }

    public String getTable() {
        return table;
    }

    public QueryCondition getCondition() {
        return condition;
    }

    public String getPrimary() {
        return primary;
    }

    public String getColumns() {
        return columns;
    }

    public String toSelectSql() {
        StringCreator creator = StringCreator.create();
        creator.append("SELECT ").append(columns).append(" FROM ").append(table).append(SqlConstants.BLANK);
        creator.append("WHERE 1=1").append(condition.getMatchHolder().toAndSql());
        if (condition.getOrder() != null) {
            creator.append(condition.getOrder().toSql());
        }
        creator.append(condition.offsetLimitSql());
        return creator.toString();
    }

    public String toCountSql() {
        StringCreator creator = StringCreator.create();
        creator.append("SELECT count(").append(primary).append(") FROM ").append(table).append(SqlConstants.BLANK);
        creator.append("WHERE 1=1").append(condition.getMatchHolder().toAndSql());
        return creator.toString();
    }

    @Override
    public List<Object> listArgs() {
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
