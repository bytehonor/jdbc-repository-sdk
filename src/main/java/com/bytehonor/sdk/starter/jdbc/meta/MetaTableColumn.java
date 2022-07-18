package com.bytehonor.sdk.starter.jdbc.meta;

/**
 * @author lijianqiang
 *
 */
public class MetaTableColumn {

    private String key;

    private String column;

    public MetaTableColumn() {
        this("", "");
    }

    public MetaTableColumn(String key, String column) {
        this.key = key;
        this.column = column;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

}
