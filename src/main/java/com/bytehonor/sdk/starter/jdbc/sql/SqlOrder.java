package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.Objects;

import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.sql.key.KeyRewriter;
import com.bytehonor.sdk.starter.jdbc.sql.key.UnderlineRewriter;

public class SqlOrder {

    /**
     * 忽略驼峰或下划线
     */
    private String key;

    private boolean desc;

    private final KeyRewriter rewriter;

    public SqlOrder() {
        this("", false, new UnderlineRewriter());
    }

    public SqlOrder(String key, boolean desc, KeyRewriter rewriter) {
        this.key = key;
        this.desc = desc;
        this.rewriter = rewriter;
    }

    public static SqlOrder descOf(String key) {
        Objects.requireNonNull(key, "key");
        return new SqlOrder(key, true, new UnderlineRewriter());
    }

    public static SqlOrder ascOf(String key) {
        Objects.requireNonNull(key, "key");
        return new SqlOrder(key, false, new UnderlineRewriter());
    }

    public static SqlOrder of(String key, boolean desc) {
        Objects.requireNonNull(key, "key");
        return new SqlOrder(key, desc, new UnderlineRewriter());
    }

    public static SqlOrder plain() {
        return new SqlOrder();
    }

    public static SqlOrder plain(KeyRewriter rewriter) {
        return new SqlOrder("", false, rewriter);
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

    public SqlOrder desc(String key) {
        this.key = key;
        this.desc = true;
        return this;
    }

    public SqlOrder asc(String key) {
        this.key = key;
        this.desc = false;
        return this;
    }

    public String toSql() {
        if (SpringString.isEmpty(key)) {
            return "";
        }
        String column = rewriter.rewrite(key);
        StringBuilder sb = new StringBuilder();
        sb.append(" ORDER BY ").append(column).append(SqlConstants.BLANK);
        sb.append(desc ? SqlConstants.DESC : SqlConstants.ASC);
        return sb.toString();
    }

    @Override
    public String toString() {
        return toSql();
    }
}
