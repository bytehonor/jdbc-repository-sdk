package com.bytehonor.sdk.jdbc.bytehonor.jdbc.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.ColumnValue;
import com.bytehonor.sdk.jdbc.bytehonor.jdbc.SqlConstants;

public class UpdateColumnHolder {

    private StringBuilder sb;

    private List<Object> args;

    private UpdateColumnHolder() {
        sb = new StringBuilder();
        args = new ArrayList<Object>();
    }

    public static UpdateColumnHolder create() {
        return new UpdateColumnHolder();
    }

    public UpdateColumnHolder set(String key, Object value) {
        this.set(new ColumnValue(key, value));
        return this;
    }

    /**
     * uncheck inject
     * 
     * @param column
     * @return
     */
    public UpdateColumnHolder set(ColumnValue column) {
        Objects.requireNonNull(column, "column");
        if (StringObject.isEmpty(column.getKey()) || column.getValue() == null) {
            return this;
        }
        // TODO value inject filter
        if (this.args.isEmpty()) {
            this.sb.append(SqlConstants.SET);
        } else {
            this.sb.append(SqlConstants.CON);
        }
        this.sb.append(column.getKey()).append(SqlConstants.PARAM_HOLD);
        this.args.add(column.getValue());
        return this;
    }

    @Override
    public String toString() {
        return toSql();
    }

    public String toSql() {
        return sb.toString();
    }

    public List<Object> getArgs() {
        return args;
    }
}
