package com.bytehonor.sdk.repository.jdbc.meta;

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
import com.bytehonor.sdk.repository.jdbc.annotation.SqlColumn;
import com.bytehonor.sdk.repository.jdbc.annotation.SqlTable;
import com.bytehonor.sdk.repository.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;

public class MetaTableParser {

    private static final Logger LOG = LoggerFactory.getLogger(MetaTableParser.class);

    private static final Map<String, MetaTable> CACHE = new ConcurrentHashMap<String, MetaTable>(1024);

    public static MetaTable parse(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        String clazzName = clazz.getName();
        if (clazz.isAnnotationPresent(SqlTable.class) == false) {
            throw new JdbcSdkException("No SqlTable Annotation, clazz:" + clazzName);
        }

        MetaTable mata = CACHE.get(clazzName);
        if (mata != null) {
            return mata;
        }

        mata = new MetaTable();
        mata.setClazz(clazzName);

        SqlTable annotation = AnnotationUtils.getAnnotation(clazz, SqlTable.class);
        String primary = annotation.primary();
        if (SpringString.isEmpty(primary)) {
            throw new JdbcSdkException("No SqlTable primary, clazz:" + clazzName);
        }

        mata.setName(annotation.name());
        mata.setPrimary(primary);
        LOG.debug("table name:{}, primary:{}", mata.getName(), primary);

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
        mata.setFields(metaTableFields);

        mata.finish();
        CACHE.put(clazzName, mata);
        return mata;
    }
}
