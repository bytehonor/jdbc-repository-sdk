package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.sql.rewrite.KeyRewriter;
import com.bytehonor.sdk.starter.jdbc.sql.rewrite.UnderlineRewriter;

public class SqlOrder implements SqlPart {

    private final List<SqlOrderColumn> columns;

    private final KeyRewriter rewriter;

    private SqlOrder() {
        this(new UnderlineRewriter());
    }

    private SqlOrder(KeyRewriter rewriter) {
        this.columns = new ArrayList<SqlOrderColumn>();
        this.rewriter = rewriter;
    }

    public SqlOrder desc(String key) {
        Objects.requireNonNull(key, "key");
        return with(SqlOrderColumn.desc(key));
    }

    public SqlOrder asc(String key) {
        Objects.requireNonNull(key, "key");
        return with(SqlOrderColumn.asc(key));
    }

    public static SqlOrder create() {
        return new SqlOrder();
    }

    public static SqlOrder create(KeyRewriter rewriter) {
        return new SqlOrder(rewriter);
    }

    public SqlOrder with(SqlOrderColumn column) {
        if (SpringString.isEmpty(column.getKey()) || SpringString.isEmpty(column.getSorter())) {
            return this;
        }
        this.columns.add(column);
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
            sb.append(format(column.getKey(), column.getSorter()));
            index++;
        }
        return sb.toString().trim();
    }

    private String format(String key, String sorter) {
        String column = rewriter.rewrite(key);
        StringBuilder sb = new StringBuilder();
        sb.append(column).append(SqlConstants.BLANK).append(sorter);
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

    public static final class SqlOrderColumn {

        /**
         * 忽略驼峰或下划线
         */
        private String key;

        private String sorter;

        public SqlOrderColumn() {
            this("", "");
        }

        public SqlOrderColumn(String key, String sorter) {
            this.key = key;
            this.sorter = sorter;
        }

        public static SqlOrderColumn desc(String key) {
            Objects.requireNonNull(key, "key");
            return of(key, SqlConstants.DESC);
        }

        public static SqlOrderColumn asc(String key) {
            Objects.requireNonNull(key, "key");
            return of(key, SqlConstants.ASC);
        }

        public static SqlOrderColumn of(String key, String sorter) {
            Objects.requireNonNull(key, "key");
            return new SqlOrderColumn(key, sorter);
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

        public String getSorter() {
            return sorter;
        }

        public void setSorter(String sorter) {
            this.sorter = sorter;
        }

    }

}
