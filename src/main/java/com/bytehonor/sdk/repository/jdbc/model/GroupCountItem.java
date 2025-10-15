package com.bytehonor.sdk.repository.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupCountItem {

    private String value;

    private Integer size;

    public static final ModelSetterMapper<GroupCountItem> SETTERS = new ModelSetterMapper<GroupCountItem>() {

        @Override
        public ModelSetter<GroupCountItem> create(ResultSet rs) throws SQLException {
            ModelSetter<GroupCountItem> setter = ModelSetter.of(GroupCountItem::new, rs);

            setter.add(GroupCountItem::setValue);
            setter.add(GroupCountItem::setSize);

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
