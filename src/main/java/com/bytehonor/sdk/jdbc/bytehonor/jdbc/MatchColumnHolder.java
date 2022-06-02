package com.bytehonor.sdk.jdbc.bytehonor.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.query.MatchColumn;

public class MatchColumnHolder {

    private StringBuilder sb;

    private List<Object> args;
    
    private List<Integer> argTypes;

    private MatchColumnHolder() {
        sb = new StringBuilder();
        args = new ArrayList<Object>();
        argTypes = new ArrayList<Integer>();
    }

    public static MatchColumnHolder create() {
        return new MatchColumnHolder();
    }

    /**
     * 
     * @param column
     * @return
     */
    public MatchColumnHolder and(MatchColumn column) {
        Objects.requireNonNull(column, "column");
        Objects.requireNonNull(column.getOperator(), "operator");
        if (StringObject.isEmpty(column.getKey()) || column.getValue() == null) {
            return this;
        }
        if (SqlOperator.IN.getKey().equals(column.getOperator().getKey())) {
            this.sb.append(SqlConstants.AND).append(column.getKey()).append(SqlConstants.BLANK)
                    .append(column.getOperator().getOpt()).append(SqlConstants.BLANK).append(column.getValue());
            return this;
        }
        this.sb.append(SqlConstants.AND).append(column.getKey()).append(SqlConstants.BLANK)
                .append(column.getOperator().getOpt()).append(SqlConstants.BLANK).append(SqlConstants.PARAM);
        this.args.add(column.getValue());
        this.argTypes.add(column.getType());
        return this;
    }

    @Override
    public String toString() {
        return toAndSql();
    }

    public String toAndSql() {
        return sb.toString();
    }

    public List<Object> getArgs() {
        return args;
    }
    
    public List<Integer> getArgTypes() {
    	return argTypes;
    }
}
