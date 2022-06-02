package com.bytehonor.sdk.jdbc.bytehonor.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.annotation.SqlColumn;
import com.bytehonor.sdk.jdbc.bytehonor.annotation.SqlTable;
import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTableColumn;

public class MetaParseUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MetaParseUtils.class);

    public static MetaTable parse(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        if (clazz.isAnnotationPresent(SqlTable.class) == false) {
            throw new RuntimeException("No SqlTable Annotation");
        }
        MetaTable table = new MetaTable();
        table.setModelClazz(clazz.getName());

        SqlTable sqlTable = AnnotationUtils.getAnnotation(clazz, SqlTable.class);
        String primary = sqlTable.primary();
        if (StringObject.isEmpty(primary)) {
            throw new RuntimeException("No SqlTable primary");
        }

        table.setTableName(sqlTable.name());
        table.setPrimaryKey(primary);
        LOG.debug("table name:{}, primary:{}", table.getTableName(), primary);

        List<MetaTableColumn> columns = new ArrayList<MetaTableColumn>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (primary.equals(fieldName)) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            MetaTableColumn column = new MetaTableColumn();
            column.setField(fieldName);

            String columnName = "";
            if (field.isAnnotationPresent(SqlColumn.class)) {
                SqlColumn sqlColumn = AnnotationUtils.getAnnotation(field, SqlColumn.class);
                LOG.debug("fieldName:{}, column name:{}, ignore:{}", fieldName, sqlColumn.name(), sqlColumn.ignore());
                if (sqlColumn.ignore()) {
                    continue;
                }
                columnName = sqlColumn.name();
            }
            if (StringObject.isEmpty(columnName)) {
                LOG.debug("fieldName:{}, use camelToUnderline", fieldName);
                columnName = StringObject.camelToUnderline(fieldName);
            }
            column.setColumn(columnName);
            columns.add(column);
        }
        table.setColumns(columns);
        return table;
    }
}
