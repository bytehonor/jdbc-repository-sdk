package com.bytehonor.sdk.starter.jdbc.meta;

/**
 * @author lijianqiang
 *
 */
public class MetaTableField {

    /**
     * 如：updateAt
     */
    private String camel;

    /**
     * 如：update_at
     */
    private String underline;

    /**
     * java类型
     */
    private String type;

    public MetaTableField() {
        this("", "", "");
    }

    public MetaTableField(String camel, String underline, String type) {
        this.camel = camel;
        this.underline = underline;
        this.type = type;
    }

    public String getCamel() {
        return camel;
    }

    public void setCamel(String camel) {
        this.camel = camel;
    }

    public String getUnderline() {
        return underline;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
