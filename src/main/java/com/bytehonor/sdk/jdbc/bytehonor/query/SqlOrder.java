package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.util.Objects;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlConstants;
import com.bytehonor.sdk.jdbc.bytehonor.util.SqlInjectUtils;

public class SqlOrder {

    private String column;

    private boolean desc;

    public static SqlOrder descOf(String column) {
        Objects.requireNonNull(column, "column");
        return new SqlOrder(column, true);
    }

    public static SqlOrder ascOf(String column) {
        Objects.requireNonNull(column, "column");
        return new SqlOrder(column, false);
    }

    public static SqlOrder of(String column, boolean desc) {
        Objects.requireNonNull(column, "column");
        return new SqlOrder(column, desc);
    }

    public SqlOrder() {
        this("id", false);
    }

    public SqlOrder(String column, boolean desc) {
        this.column = column;
        this.desc = desc;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public String toSql() {
        if (StringObject.isEmpty(column)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" ORDER BY ").append(SqlInjectUtils.column(column))
                .append(SqlConstants.BLANK);
        if (desc) {
            sb.append(SqlConstants.DESC);
        } else {
            sb.append(SqlConstants.ASC);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toSql();
    }
}
