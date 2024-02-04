package com.bytehonor.sdk.starter.jdbc.util;

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

import com.bytehonor.sdk.lang.spring.regex.PatternUtils;
import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTable;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;
import com.google.common.collect.Sets;

public class SqlColumnUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlColumnUtils.class);

    private static final char UNDERLINE = '_';

    private static final Map<String, String> UNDERLINE_CACHE = new ConcurrentHashMap<String, String>();

    private static final Map<String, Boolean> ENABLED_CACHE = new ConcurrentHashMap<String, Boolean>();

    private static final Set<String> IGNORES = Sets.newHashSet(SqlConstants.UPDATE_AT_KEY,
            SqlConstants.UPDATE_AT_COLUMN, SqlConstants.CREATE_AT_KEY, SqlConstants.CREATE_AT_COLUMN);

    public static List<ModelKeyValue> prepareInsert(MetaTable metaTable, List<ModelKeyValue> keyValues) {
        Objects.requireNonNull(metaTable, "metaTable");

        List<ModelKeyValue> result = new ArrayList<ModelKeyValue>();

        String primary = metaTable.getPrimary();
        for (ModelKeyValue item : keyValues) {
            if (isSaveIgnore(primary, item.getKey())) {
                continue;
            }
            LOG.debug("insert key:{}, value:{}, javaType:{}", item.getKey(), item.getValue(), item.getJavaType());
            result.add(item);
        }

        if (CollectionUtils.isEmpty(result)) {
            throw new JdbcSdkException("prepareInsert ModelColumnValue empty");
        }

        // 自动补充更新时间和创建时间
        long now = System.currentTimeMillis();
        if (autoUpdateAt(metaTable)) {
            result.add(ModelKeyValue.of(SqlConstants.UPDATE_AT_COLUMN, now));
        }
        if (autoCreateAt(metaTable)) {
            result.add(ModelKeyValue.of(SqlConstants.CREATE_AT_COLUMN, now));
        }
        return result;
    }

    public static List<ModelKeyValue> prepareUpdate(MetaTable metaTable, List<ModelKeyValue> keyValues,
            List<String> filterKeys) {
        Objects.requireNonNull(metaTable, "metaTable");

        List<ModelKeyValue> result = new ArrayList<ModelKeyValue>();

        String primary = metaTable.getPrimary();

        Set<String> filters = new HashSet<String>();
        if (CollectionUtils.isEmpty(filterKeys) == false) {
            filters = new HashSet<String>(filterKeys);
        }

        boolean filter = CollectionUtils.isEmpty(filters) == false;
        for (ModelKeyValue item : keyValues) {
            if (isSaveIgnore(primary, item.getKey())) {
                continue;
            }
            if (filter && filters.contains(item.getKey())) {
                LOG.debug("prepare ({}) filter", item.getKey());
                continue;
            }
            LOG.debug("update key:{}, value:{}, javaType:{}", item.getKey(), item.getValue(), item.getJavaType());
            result.add(item);
        }

        // 自动补充更新时间和创建时间
        long now = System.currentTimeMillis();
        if (autoUpdateAt(metaTable)) {
            result.add(ModelKeyValue.of(SqlConstants.UPDATE_AT_COLUMN, now));
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
        if (SpringString.isEmpty(val) == false) {
            return val;
        }

        acceptChar(column);

        val = SpringString.camelToUnderline(column);
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
            if (PatternUtils.isLetterChar(ch)) {
                continue;
            }
            if (PatternUtils.isNumberChar(ch)) {
                continue;
            }
            throw new JdbcSdkException("column only accept _ number letter, column:" + column);
        }
    }

    public static boolean isSaveIgnore(String primary, String column) {
        if (SpringString.equals(primary, column)) {
            return true;
        }
        return IGNORES.contains(column);
    }

}
