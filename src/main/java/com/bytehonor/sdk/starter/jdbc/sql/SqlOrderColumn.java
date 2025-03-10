package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.Objects;

import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;

public class SqlOrderColumn {

    /**
     * 忽略驼峰或下划线
     */
    private String key;

    private String tag;

    public SqlOrderColumn() {
        this("", "");
    }

    public SqlOrderColumn(String key, String tag) {
        this.key = key;
        this.tag = tag;
    }

    public static SqlOrderColumn descOf(String key) {
        Objects.requireNonNull(key, "key");
        return new SqlOrderColumn(key, SqlConstants.DESC);
    }

    public static SqlOrderColumn ascOf(String key) {
        Objects.requireNonNull(key, "key");
        return new SqlOrderColumn(key, SqlConstants.ASC);
    }

    public static SqlOrderColumn of(String key, String tag) {
        Objects.requireNonNull(key, "key");
        return new SqlOrderColumn(key, tag);
    }

    public static SqlOrderColumn plain() {
        return new SqlOrderColumn();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

//    public String toSql() {
//        if (SpringString.isEmpty(key)) {
//            return "";
//        }
//        String column = rewriter.rewrite(key);
//        StringBuilder sb = new StringBuilder();
//        sb.append(column).append(SqlConstants.BLANK);
//        sb.append(desc ? SqlConstants.DESC : SqlConstants.ASC);
//        return sb.toString();
//    }
}
