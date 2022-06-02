package com.bytehonor.sdk.jdbc.bytehonor.meta;

public class MetaTableColumn {

    private String field;

    private String column;

    public MetaTableColumn() {
        this("", "");
    }

    public MetaTableColumn(String field, String column) {
        this.field = field;
        this.column = column;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

}
