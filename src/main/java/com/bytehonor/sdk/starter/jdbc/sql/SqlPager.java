package com.bytehonor.sdk.starter.jdbc.sql;

import com.bytehonor.sdk.lang.spring.constant.HttpConstants;

public class SqlPager {

    public static int LIMIT_DEF = HttpConstants.LIMIT_DEF;

    public static int LIMIT_ALL = HttpConstants.LIMIT_NON;

    private int offset;

    private int limit;

    public SqlPager() {
        this.offset = 0;
        this.limit = LIMIT_DEF;
    }

    public static SqlPager create() {
        return new SqlPager();
    }

    public static SqlPager of(int offset, int limit) {
        SqlPager page = new SqlPager();
        page.setOffset(offset > LIMIT_ALL ? offset : HttpConstants.OFFSET_DEF);
        page.setLimit(limit);
        return page;
    }

    public String toSql() {
        if (unlimited()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" LIMIT ").append(offset).append(",").append(limit);
        return sb.toString();
    }

    @Override
    public String toString() {
        return toSql();
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean unlimited() {
        return this.limit == LIMIT_ALL;
    }
}
