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

import com.bytehonor.sdk.framework.lang.string.SpringString;
import com.bytehonor.sdk.repository.jdbc.annotation.SqlColumn;
import com.bytehonor.sdk.repository.jdbc.annotation.SqlTableLeftJoin;
import com.bytehonor.sdk.repository.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.repository.jdbc.util.SqlColumnUtils;

public class MetaTableLeftJoinParser {

    private static final Logger LOG = LoggerFactory.getLogger(MetaTableParser.class);

    private static final Map<String, MetaTableLeftJoin> CACHE = new ConcurrentHashMap<String, MetaTableLeftJoin>();

    public static MetaTableLeftJoin parse(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz");

        String clazzName = clazz.getName();
        if (clazz.isAnnotationPresent(SqlTableLeftJoin.class) == false) {
            throw new JdbcSdkException("No SqlTableLeftJoin Annotation, clazz:" + clazzName);
        }

        MetaTableLeftJoin meta = CACHE.get(clazzName);
        if (meta != null) {
            return meta;
        }

        meta = new MetaTableLeftJoin();
        meta.setClazz(clazzName);

        SqlTableLeftJoin annotation = AnnotationUtils.getAnnotation(clazz, SqlTableLeftJoin.class);
        String on = annotation.on();
        if (SpringString.isEmpty(on)) {
            throw new JdbcSdkException("No SqlTableLeftJoin on, clazz:" + clazzName);
        }

        meta.setOn(SqlColumnUtils.camelToUnderline(on));

        meta.setMain(MetaTableParser.parse(annotation.main()));
        meta.setSub(MetaTableParser.parse(annotation.sub()));

        List<MetaTableField> metaTableFields = new ArrayList<MetaTableField>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String camel = field.getName();
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
        meta.setFields(metaTableFields);

        meta.finish();
        CACHE.put(clazzName, meta);
        return meta;
    }
}
