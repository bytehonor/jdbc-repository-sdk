package com.bytehonor.sdk.repository.jdbc.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.bytehonor.sdk.framework.lang.regex.PatternKit;
import com.bytehonor.sdk.framework.lang.string.StringKit;
import com.bytehonor.sdk.repository.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.repository.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.repository.jdbc.meta.MetaTable;
import com.bytehonor.sdk.repository.jdbc.model.ModelField;
import com.google.common.collect.Sets;

public class SqlColumnUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlColumnUtils.class);

    private static final char UNDERLINE = '_';

    private static final Map<String, String> UNDERLINE_CACHE = new ConcurrentHashMap<String, String>();

    private static final Map<String, Boolean> ENABLED_CACHE = new ConcurrentHashMap<String, Boolean>();

    private static final Set<String> IGNORES = Sets.newHashSet(SqlConstants.UPDATE_AT_KEY,
            SqlConstants.UPDATE_AT_COLUMN, SqlConstants.CREATE_AT_KEY, SqlConstants.CREATE_AT_COLUMN);

    public static List<ModelField> prepareInsert(MetaTable table, List<ModelField> fields) {
        Objects.requireNonNull(table, "table");

        List<ModelField> result = new ArrayList<ModelField>();

        String primary = table.getPrimary();
        for (ModelField field : fields) {
            if (isSaveIgnore(primary, field.getKey())) {
                continue;
            }
            LOG.debug("insert key:{}, value:{}, javaType:{}", field.getKey(), field.getValue(), field.getJavaType());
            result.add(field);
        }

        if (CollectionUtils.isEmpty(result)) {
            throw new JdbcSdkException("prepareInsert ModelField empty");
        }

        // 自动补充更新时间和创建时间
        long now = System.currentTimeMillis();
        if (autoUpdateAt(table)) {
            result.add(ModelField.of(SqlConstants.UPDATE_AT_COLUMN, now));
        }
        if (autoCreateAt(table)) {
            result.add(ModelField.of(SqlConstants.CREATE_AT_COLUMN, now));
        }
        return result;
    }

    public static List<ModelField> prepareUpdate(MetaTable table, List<ModelField> fields, List<String> filterKeys) {
        Objects.requireNonNull(table, "table");

        List<ModelField> result = new ArrayList<ModelField>();

        String primary = table.getPrimary();

        Set<String> ignores = new HashSet<String>();
        if (CollectionUtils.isEmpty(filterKeys) == false) {
            ignores = new HashSet<String>(filterKeys);
        }

        boolean canFilter = CollectionUtils.isEmpty(ignores) == false;
        for (ModelField field : fields) {
            if (isSaveIgnore(primary, field.getKey())) {
                continue;
            }
            if (canFilter && ignores.contains(field.getKey())) {
                LOG.debug("prepare ({}) filter", field.getKey());
                continue;
            }
            LOG.debug("update key:{}, value:{}, javaType:{}", field.getKey(), field.getValue(), field.getJavaType());
            result.add(field);
        }

        // 自动补充更新时间和创建时间
        long now = System.currentTimeMillis();
        if (autoUpdateAt(table)) {
            result.add(ModelField.of(SqlConstants.UPDATE_AT_COLUMN, now));
        }
        return result;
    }

    public static boolean autoUpdateAt(MetaTable metaTable) {
        String key = metaTable.getClazz() + ":update";
        Boolean enabled = ENABLED_CACHE.get(key);
        if (enabled != null) {
            return enabled;
        }
        enabled = hasUpdateAt(metaTable);
        ENABLED_CACHE.put(key, enabled);
        return enabled;
    }

    public static boolean hasUpdateAt(MetaTable metaTable) {
        if (metaTable.getCamels().contains(SqlConstants.UPDATE_AT_KEY)) {
            return true;
        }
        if (metaTable.getUnderlines().contains(SqlConstants.UPDATE_AT_COLUMN)) {
            return true;
        }
        return false;
    }

    public static boolean autoCreateAt(MetaTable metaTable) {
        String key = metaTable.getClazz() + ":create";
        Boolean enabled = ENABLED_CACHE.get(key);
        if (enabled != null) {
            return enabled;
        }
        enabled = hasCreateAt(metaTable);
        ENABLED_CACHE.put(key, enabled);
        return enabled;
    }

    public static boolean hasCreateAt(MetaTable metaTable) {
        if (metaTable.getCamels().contains(SqlConstants.CREATE_AT_KEY)) {
            return true;
        }
        if (metaTable.getUnderlines().contains(SqlConstants.CREATE_AT_COLUMN)) {
            return true;
        }
        return false;
    }

    public static String camelToUnderline(String column) {
        Objects.requireNonNull(column, "column");

        String val = UNDERLINE_CACHE.get(column);
        if (StringKit.isEmpty(val) == false) {
            return val;
        }

        acceptChar(column);

        val = StringKit.camelToUnderline(column);
        UNDERLINE_CACHE.put(column, val);
        return val;
    }

    public static void acceptChar(String column) {
        Objects.requireNonNull(column, "column");

        int len = column.length();
        if (len < 1) {
            throw new JdbcSdkException("column cannt be empty, column:" + column);
        }
        char ch = column.charAt(0);
        for (int i = 0; i < len; i++) {
            ch = column.charAt(i);
            if (UNDERLINE == ch) {
                continue;
            }
            if (PatternKit.isLetterChar(ch)) {
                continue;
            }
            if (PatternKit.isNumberChar(ch)) {
                continue;
            }
            throw new JdbcSdkException("column only accept _ number letter, column:" + column);
        }
    }

    public static boolean isSaveIgnore(String primary, String column) {
        if (StringKit.equals(primary, column)) {
            return true;
        }
        return IGNORES.contains(column);
    }

    public static String joins(Set<String> columns, String prefix) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String column : columns) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(prefix).append(column);
        }
        return sb.toString();
    }
}
