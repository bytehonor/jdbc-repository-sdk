package com.bytehonor.sdk.repository.jdbc.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bytehonor.sdk.framework.lang.string.SpringString;

public class MetaTableLeftJoin implements Serializable {

    private static final long serialVersionUID = -3957039146909053296L;

    /**
     * Model Clazz
     */
    private String clazz;

    private String on;

    private List<MetaTableField> fields;

    private Set<String> camels;

    private Set<String> underlines;

    private String fullColumns;

    private MetaTable main;

    private MetaTable sub;

    public MetaTableLeftJoin() {
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

        sb.append("m.").append(main.getPrimary());
        for (MetaTableField field : fields) {
            if (field.getUnderline().equals(main.getPrimary())) {
                continue;
            }
            if (field.getUnderline().equals(sub.getPrimary())) {
                continue;
            }
            camels.add(field.getCamel());
            underlines.add(field.getUnderline());
            sb.append(", ");
            if (main.getUnderlines().contains(field.getUnderline())) {
                sb.append("m.");
            } else {
                sb.append("s.");
            }
            sb.append(field.getUnderline());
        }
        fullColumns = sb.toString();
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
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

    public MetaTable getMain() {
        return main;
    }

    public void setMain(MetaTable main) {
        this.main = main;
    }

    public MetaTable getSub() {
        return sub;
    }

    public void setSub(MetaTable sub) {
        this.sub = sub;
    }

    public String getFullColumns() {
        return fullColumns;
    }

    public void setFullColumns(String fullColumns) {
        this.fullColumns = fullColumns;
    }

}
