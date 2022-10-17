package com.bytehonor.sdk.starter.jdbc.meta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bytehonor.sdk.lang.spring.string.SpringString;

/**
 * @author lijianqiang
 *
 */
public class MetaTable {

    private String modelClazz;

    private String tableName;

    private String primaryKey;

    private List<MetaTableField> fields;

    private Set<String> keySet;

    private Set<String> columnSet;

    private String fullColumns;

    public MetaTable() {
        fields = new ArrayList<MetaTableField>();
        keySet = new HashSet<String>();
        columnSet = new HashSet<String>();
    }

    public void finish() {
        if (SpringString.isEmpty(fullColumns) == false) {
            return;
        }
        keySet = new HashSet<String>();
        columnSet = new HashSet<String>();
        StringBuilder sb = new StringBuilder();
        sb.append(primaryKey);
        for (MetaTableField field : fields) {
            keySet.add(field.getKey());
            columnSet.add(field.getColumn());
            sb.append(", ").append(field.getColumn());
        }
        fullColumns = sb.toString();
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

    public List<MetaTableField> getFields() {
        return fields;
    }

    public void setFields(List<MetaTableField> fields) {
        this.fields = fields;
    }

    public Set<String> getKeySet() {
        return keySet;
    }

    public void setKeySet(Set<String> keySet) {
        this.keySet = keySet;
    }

    public Set<String> getColumnSet() {
        return columnSet;
    }

    public void setColumnSet(Set<String> columnSet) {
        this.columnSet = columnSet;
    }

    public String getFullColumns() {
        return fullColumns;
    }

    public void setFullColumns(String fullColumns) {
        this.fullColumns = fullColumns;
    }

}
