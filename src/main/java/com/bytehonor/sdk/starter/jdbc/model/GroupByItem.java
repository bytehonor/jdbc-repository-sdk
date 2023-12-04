package com.bytehonor.sdk.starter.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupByItem {

    private String key;

    private String value;

    public static final ModelSetterMapper<GroupByItem> SETTERS = new ModelSetterMapper<GroupByItem>() {

        @Override
        public ModelSetter<GroupByItem> create(ResultSet rs) throws SQLException {
            ModelSetter<GroupByItem> setter = ModelSetter.of(GroupByItem::new, rs);

            setter.add(GroupByItem::setKey);
            setter.add(GroupByItem::setValue);

            return setter;
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
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
