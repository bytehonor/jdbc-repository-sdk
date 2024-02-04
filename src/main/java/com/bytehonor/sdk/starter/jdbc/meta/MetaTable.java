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

    /**
     * Model Clazz
     */
    private String clazz;

    /**
     * Table Name
     */
    private String name;

    /**
     * Table Primary Key
     */
    private String primary;

    private List<MetaTableField> fields;

    private Set<String> camels;

    private Set<String> underlines;

    private String fullColumns;

    public MetaTable() {
        fields = new ArrayList<MetaTableField>();
        camels = new HashSet<String>();
        underlines = new HashSet<String>();
    }

    public void finish() {
        if (SpringString.isEmpty(fullColumns) == false) {
            return;
        }
        camels = new HashSet<String>();
        underlines = new HashSet<String>();
        StringBuilder sb = new StringBuilder();
        sb.append(primary);
        for (MetaTableField field : fields) {
            camels.add(field.getCamel());
            underlines.add(field.getUnderline());
            sb.append(", ").append(field.getUnderline());
        }
        fullColumns = sb.toString();
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public List<MetaTableField> getFields() {
        return fields;
    }

    public void setFields(List<MetaTableField> fields) {
        this.fields = fields;
    }

    public Set<String> getCamels() {
        return camels;
    }

    public void setCamels(Set<String> camels) {
        this.camels = camels;
    }

    public Set<String> getUnderlines() {
        return underlines;
    }

    public void setUnderlines(Set<String> underlines) {
        this.underlines = underlines;
    }

    public String getFullColumns() {
        return fullColumns;
    }

    public void setFullColumns(String fullColumns) {
        this.fullColumns = fullColumns;
    }

}
