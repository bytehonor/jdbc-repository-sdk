package com.bytehonor.sdk.repository.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupStats {

    private String value;

    private Integer size;

    public static final ModelSetterMapper<GroupStats> SETTERS = new ModelSetterMapper<GroupStats>() {

        @Override
        public ModelSetter<GroupStats> create(ResultSet rs) throws SQLException {
            ModelSetter<GroupStats> setter = ModelSetter.of(GroupStats::new, rs);

            setter.add(GroupStats::setValue);
            setter.add(GroupStats::setSize);

            return setter;
        }
    };

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

}
