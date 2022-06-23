package com.bytehonor.sdk.starter.jdbc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import com.bytehonor.sdk.define.spring.util.StringObject;
import com.bytehonor.sdk.starter.jdbc.annotation.SqlColumn;
import com.bytehonor.sdk.starter.jdbc.annotation.SqlTable;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTable;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTableColumn;

public class SqlMetaUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlMetaUtils.class);

    private static final Map<String, MetaTable> TABLES = new ConcurrentHashMap<String, MetaTable>();

    public static MetaTable parse(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        String clazzName = clazz.getName();
        if (clazz.isAnnotationPresent(SqlTable.class) == false) {
            throw new RuntimeException("No SqlTable Annotation, clazz:" + clazzName);
        }

        MetaTable metaTable = TABLES.get(clazzName);
        if (metaTable != null) {
            return metaTable;
        }

        metaTable = new MetaTable();
        metaTable.setModelClazz(clazz.getName());

        SqlTable sqlTable = AnnotationUtils.getAnnotation(clazz, SqlTable.class);
        String primary = sqlTable.primary();
        if (StringObject.isEmpty(primary)) {
            throw new RuntimeException("No SqlTable primary, clazz:" + clazzName);
        }

        metaTable.setTableName(sqlTable.name());
        metaTable.setPrimaryKey(primary);
        LOG.debug("table name:{}, primary:{}", metaTable.getTableName(), primary);

        List<MetaTableColumn> columns = new ArrayList<MetaTableColumn>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            if (primary.equals(key)) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            MetaTableColumn column = new MetaTableColumn();
            column.setKey(key);

            String columnName = "";
            if (field.isAnnotationPresent(SqlColumn.class)) {
                SqlColumn sqlColumn = AnnotationUtils.getAnnotation(field, SqlColumn.class);
                LOG.debug("key:{}, column name:{}, ignore:{}", key, sqlColumn.name(), sqlColumn.ignore());
                if (sqlColumn.ignore()) {
                    continue;
                }
                columnName = sqlColumn.name();
            }
            if (StringObject.isEmpty(columnName)) {
                LOG.debug("key:{}, use camelToUnderline", key);
                columnName = SqlColumnUtils.camelToUnderline(key);
            }
            column.setColumn(columnName);
            columns.add(column);
        }
        metaTable.setColumns(columns);

        metaTable.finish();
        TABLES.put(clazzName, metaTable);
        return metaTable;
    }
}
