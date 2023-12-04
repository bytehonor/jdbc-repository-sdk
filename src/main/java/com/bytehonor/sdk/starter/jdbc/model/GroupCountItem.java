package com.bytehonor.sdk.starter.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupCountItem {

    private String key;

    private Integer value;

    public static final ModelSetterMapper<GroupCountItem> SETTERS = new ModelSetterMapper<GroupCountItem>() {

        @Override
        public ModelSetter<GroupCountItem> create(ResultSet rs) throws SQLException {
            ModelSetter<GroupCountItem> setter = ModelSetter.of(GroupCountItem::new, rs);

            setter.add(GroupCountItem::setKey);
            setter.add(GroupCountItem::setValue);

            return setter;
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

//    public static final ModelGetterMapper<ColumnSizeItem> GETTERS = new ModelGetterMapper<ColumnSizeItem>() {
//
//        @Override
//        public ModelGetter<ColumnSizeItem> create(ColumnSizeItem model) {
//
//            ModelGetter<ColumnSizeItem> getter = ModelGetter.of(model);
//
//            getter.add(ColumnSizeItem::getColumn);
//            getter.add(ColumnSizeItem::getSize);
//
//            return getter;
//        }
//
//    };

}
