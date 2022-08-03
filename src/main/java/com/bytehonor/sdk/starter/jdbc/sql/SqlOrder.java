package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.Objects;

import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class SqlOrder {

    /**
     * 忽略驼峰或下划线
     */
    private String key;

    private boolean desc;

    public static SqlOrder descOf(String key) {
        Objects.requireNonNull(key, "key");
        return new SqlOrder(key, true);
    }

    public static SqlOrder ascOf(String key) {
        Objects.requireNonNull(key, "key");
        return new SqlOrder(key, false);
    }

    public static SqlOrder of(String key, boolean desc) {
        Objects.requireNonNull(key, "key");
        return new SqlOrder(key, desc);
    }

    public SqlOrder() {
        this("id", false);
    }

    public SqlOrder(String key, boolean desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public String toSql() {
        if (SpringString.isEmpty(key)) {
            return "";
        }
        String column = SqlColumnUtils.camelToUnderline(key);
        StringBuilder sb = new StringBuilder(" ORDER BY ").append(column).append(SqlConstants.BLANK);
        sb.append(desc ? SqlConstants.DESC : SqlConstants.ASC);
        return sb.toString();
    }

    @Override
    public String toString() {
        return toSql();
    }
}
