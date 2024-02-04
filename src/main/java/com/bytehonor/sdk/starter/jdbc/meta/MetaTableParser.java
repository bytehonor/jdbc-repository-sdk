package com.bytehonor.sdk.starter.jdbc.meta;

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

import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.annotation.SqlColumn;
import com.bytehonor.sdk.starter.jdbc.annotation.SqlTable;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;

public class MetaTableParser {

    private static final Logger LOG = LoggerFactory.getLogger(MetaTableParser.class);

    private static final Map<String, MetaTable> TABLES = new ConcurrentHashMap<String, MetaTable>();

    public static MetaTable parse(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        String clazzName = clazz.getName();
        if (clazz.isAnnotationPresent(SqlTable.class) == false) {
            throw new JdbcSdkException("No SqlTable Annotation, clazz:" + clazzName);
        }

        MetaTable metaTable = TABLES.get(clazzName);
        if (metaTable != null) {
            return metaTable;
        }

        metaTable = new MetaTable();
        metaTable.setClazz(clazz.getName());

        SqlTable sqlTable = AnnotationUtils.getAnnotation(clazz, SqlTable.class);
        String primary = sqlTable.primary();
        if (SpringString.isEmpty(primary)) {
            throw new JdbcSdkException("No SqlTable primary, clazz:" + clazzName);
        }

        metaTable.setName(sqlTable.name());
        metaTable.setPrimary(primary);
        LOG.debug("table name:{}, primary:{}", metaTable.getName(), primary);

        List<MetaTableField> metaTableFields = new ArrayList<MetaTableField>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String camel = field.getName();
            if (primary.equals(camel)) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            MetaTableField tableField = new MetaTableField();
            tableField.setCamel(camel);
            tableField.setType(field.getType().getName());

            String underline = "";
            if (field.isAnnotationPresent(SqlColumn.class)) {
                SqlColumn sqlColumn = AnnotationUtils.getAnnotation(field, SqlColumn.class);
                LOG.debug("camel:{}, column name:{}, ignore:{}", camel, sqlColumn.name(), sqlColumn.ignore());
                if (sqlColumn.ignore()) {
                    continue;
                }
                underline = sqlColumn.name();
            }
            if (SpringString.isEmpty(underline)) {
                LOG.debug("camel:{}, use camelToUnderline", camel);
                underline = SqlColumnUtils.camelToUnderline(camel);
            }
            tableField.setUnderline(underline);
            metaTableFields.add(tableField);
        }
        metaTable.setFields(metaTableFields);

        metaTable.finish();
        TABLES.put(clazzName, metaTable);
        return metaTable;
    }
}
