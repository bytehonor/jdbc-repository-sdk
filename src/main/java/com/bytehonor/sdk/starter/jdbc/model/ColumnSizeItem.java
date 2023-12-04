package com.bytehonor.sdk.starter.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnSizeItem {

    private String column;

    private String size;

    public static final ModelSetterMapper<ColumnSizeItem> SETTERS = new ModelSetterMapper<ColumnSizeItem>() {

        @Override
        public ModelSetter<ColumnSizeItem> create(ResultSet rs) throws SQLException {
            ModelSetter<ColumnSizeItem> setter = ModelSetter.of(ColumnSizeItem::new, rs);

            setter.add(ColumnSizeItem::setColumn);
            setter.add(ColumnSizeItem::setSize);

            return setter;
        }
    };

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

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
