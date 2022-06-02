package com.bytehonor.sdk.jdbc.bytehonor.meta;

import java.util.List;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;

public class MetaTable {

    private String modelClazz;

    private String tableName;

    private String primaryKey;

    private List<MetaTableColumn> columns;

    private String sqlColumns;

    public String formColumns() {
        if (StringObject.isEmpty(sqlColumns)) {
            StringBuilder sb = new StringBuilder();
            sb.append(primaryKey);
            for (MetaTableColumn column : columns) {
                sb.append(", ").append(column.getColumn());
            }
            sqlColumns = sb.toString();
        }
        return sqlColumns;
    }

    public String getModelClazz() {
        return modelClazz;
    }

    public void setModelClazz(String modelClazz) {
        this.modelClazz = modelClazz;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<MetaTableColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<MetaTableColumn> columns) {
        this.columns = columns;
    }

}
