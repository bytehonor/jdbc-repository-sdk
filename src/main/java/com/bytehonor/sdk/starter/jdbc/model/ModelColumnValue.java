package com.bytehonor.sdk.starter.jdbc.model;

public class ModelColumnValue {

    /**
     * 表字段名
     */
    private String column;

    /**
     * 表字段值
     */
    private Object value;

    /**
     * 值类型, String Long 等
     */
    private String type;

    public static ModelColumnValue of(String column, Object value, String type) {
        ModelColumnValue model = new ModelColumnValue();
        model.setColumn(column);
        model.setValue(value);
        model.setType(type);
        return model;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
