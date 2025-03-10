package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.sql.key.KeyRewriter;
import com.bytehonor.sdk.starter.jdbc.sql.key.UnderlineRewriter;

public class SqlOrder implements SqlPart {

    private final List<SqlOrderColumn> columns;

    private final KeyRewriter rewriter;

    public SqlOrder() {
        this(new UnderlineRewriter());
    }

    public SqlOrder(KeyRewriter rewriter) {
        this.columns = new ArrayList<SqlOrderColumn>();
        this.rewriter = rewriter;
    }

    public SqlOrder desc(String key) {
        Objects.requireNonNull(key, "key");
        return sort(key, SqlConstants.DESC);
    }

    public SqlOrder asc(String key) {
        Objects.requireNonNull(key, "key");
        return sort(key, SqlConstants.ASC);
    }

    public static SqlOrder plain() {
        return new SqlOrder();
    }

    public static SqlOrder plain(KeyRewriter rewriter) {
        return new SqlOrder(rewriter);
    }

    public SqlOrder sort(String key, String tag) {
        this.columns.add(SqlOrderColumn.of(key, tag));
        return this;
    }

    public SqlOrder sorts(List<SqlOrderColumn> list) {
        if (CollectionUtils.isEmpty(list)) {
            return this;
        }
        for (SqlOrderColumn item : list) {
            sort(item.getKey(), item.getTag());
        }
        return this;
    }

    @Override
    public String toSql() {
        if (canOrder() == false) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER BY ");
        int index = 0;
        for (SqlOrderColumn column : columns) {
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(formatSort(column.getKey(), column.getTag()));
            index++;
        }
        return sb.toString().trim();
    }

    private String formatSort(String key, String tag) {
        String column = rewriter.rewrite(key);
        StringBuilder sb = new StringBuilder();
        sb.append(column).append(SqlConstants.BLANK).append(tag);
        return sb.toString();
    }

    public boolean canOrder() {
        return CollectionUtils.isEmpty(columns) == false;
    }

    public List<SqlOrderColumn> getColumns() {
        return columns;
    }

    public KeyRewriter getRewriter() {
        return rewriter;
    }

    @Override
    public String toString() {
        return toSql();
    }

}
