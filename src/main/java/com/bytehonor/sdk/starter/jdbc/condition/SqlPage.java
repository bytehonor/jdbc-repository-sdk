package com.bytehonor.sdk.starter.jdbc.condition;

import com.bytehonor.sdk.define.spring.constant.HttpConstants;

public class SqlPage {

    private static int LIMIT_DEF = HttpConstants.LIMIT_DEF;

    private int offset;

    private int limit;

    public SqlPage() {
        this.offset = 0;
        this.limit = LIMIT_DEF;
    }

    public static SqlPage create() {
        return new SqlPage();
    }

    public static SqlPage of(int offset, int limit) {
        SqlPage page = new SqlPage();
        page.setOffset(offset > -1 ? offset : HttpConstants.OFFSET_DEF);
        page.setLimit(limit);
        return page;
    }

    public String toSql() {
        if (limit < -1) {
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

}
