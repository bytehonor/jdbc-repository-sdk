package com.bytehonor.sdk.starter.jdbc.meta;

/**
 * @author lijianqiang
 *
 */
public class MetaTableField {

    /**
     * 如：updateAt
     */
    private String key;

    /**
     * 如：update_at
     */
    private String column;

    /**
     * java类型
     */
    private String type;

    public MetaTableField() {
        this("", "", "");
    }

    public MetaTableField(String key, String column, String type) {
        this.key = key;
        this.column = column;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
