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
import com.bytehonor.sdk.lang.spring.util.StringObject;
import com.bytehonor.sdk.starter.jdbc.constant.SqlConstants;
import com.bytehonor.sdk.starter.jdbc.exception.JdbcSdkException;
import com.bytehonor.sdk.starter.jdbc.meta.MetaTable;
import com.bytehonor.sdk.starter.jdbc.model.ModelKeyValue;
import com.google.common.collect.Sets;

public class SqlColumnUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlColumnUtils.class);

    private static final char UNDERLINE = '_';

    private static final Map<String, String> UNDERLINE_COLUMNS = new ConcurrentHashMap<String, String>();

    private static final Map<String, Boolean> TIME_AT_ENABLED = new ConcurrentHashMap<String, Boolean>();

    private static final Set<String> IGNORES = Sets.newHashSet(SqlConstants.UPDATE_AT_KEY,
            SqlConstants.UPDATE_AT_COLUMN, SqlConstants.CREATE_AT_KEY, SqlConstants.CREATE_AT_COLUMN);

    public static List<ModelKeyValue> prepareInsert(MetaTable metaTable, List<ModelKeyValue> items) {
        Objects.requireNonNull(metaTable, "metaTable");

        List<ModelKeyValue> result = new ArrayList<ModelKeyValue>();

        String primary = metaTable.getPrimaryKey();
        for (ModelKeyValue item : items) {
            if (isSaveIgnore(primary, item.getKey())) {
                LOG.debug("prepare ({}) pass", item.getKey());
                continue;
            }
            LOG.debug("key:{}, value:{}, javaType:{}", item.getKey(), item.getValue(), item.getJavaType());
            result.add(item);
        }

        if (CollectionUtils.isEmpty(result)) {
            throw new JdbcSdkException("prepareInsert ModelColumnValue empty");
        }

        // 自动补充更新时间和创建时间
        long now = System.currentTimeMillis();
        if (enabledUpdateAtIfCache(metaTable)) {
            result.add(ModelKeyValue.of(SqlConstants.UPDATE_AT_COLUMN, now));
        }
        if (enabledCreateAtIfCache(metaTable)) {
            result.add(ModelKeyValue.of(SqlConstants.CREATE_AT_COLUMN, now));
        }
        return result;
    }

    public static List<ModelKeyValue> prepareUpdate(MetaTable metaTable, List<ModelKeyValue> items,
            List<String> filterColumns) {
        Objects.requireNonNull(metaTable, "metaTable");

        List<ModelKeyValue> result = new ArrayList<ModelKeyValue>();

        String primary = metaTable.getPrimaryKey();

        Set<String> filters = new HashSet<String>();
        if (CollectionUtils.isEmpty(filterColumns) == false) {
            filters = new HashSet<String>(filterColumns);
            filters.remove(primary);

        }
        boolean filter = CollectionUtils.isEmpty(filters) == false;
        for (ModelKeyValue item : items) {
            if (isSaveIgnore(primary, item.getKey())) {
                LOG.debug("prepare ({}) pass", item.getKey());
                continue;
            }
            if (filter && filters.contains(item.getKey())) {
                LOG.debug("prepare ({}) filter", item.getKey());
                continue;
            }
            LOG.debug("key:{}, value:{}, javaType:{}", item.getKey(), item.getValue(), item.getJavaType());
            result.add(item);
        }

        // 自动补充更新时间和创建时间
        long now = System.currentTimeMillis();
        if (enabledUpdateAtIfCache(metaTable)) {
            result.add(ModelKeyValue.of(SqlConstants.UPDATE_AT_COLUMN, now));
        }
        return result;
    }

    public static boolean enabledUpdateAtIfCache(MetaTable metaTable) {
        String key = metaTable.getModelClazz() + ":update";
        Boolean enabled = TIME_AT_ENABLED.get(key);
        if (enabled != null) {
            return enabled;
        }
        enabled = enabledUpdateAt(metaTable);
        TIME_AT_ENABLED.put(key, enabled);
        return enabled;
    }

    public static boolean enabledUpdateAt(MetaTable metaTable) {
        if (metaTable.getKeySet().contains(SqlConstants.UPDATE_AT_KEY)) {
            return true;
        }
        if (metaTable.getColumnSet().contains(SqlConstants.UPDATE_AT_COLUMN)) {
            return true;
        }
        return false;
    }

    public static boolean enabledCreateAtIfCache(MetaTable metaTable) {
        String key = metaTable.getModelClazz() + ":create";
        Boolean enabled = TIME_AT_ENABLED.get(key);
        if (enabled != null) {
            return enabled;
        }
        enabled = enabledCreateAt(metaTable);
        TIME_AT_ENABLED.put(key, enabled);
        return enabled;
    }

    public static boolean enabledCreateAt(MetaTable metaTable) {
        if (metaTable.getKeySet().contains(SqlConstants.CREATE_AT_KEY)) {
            return true;
        }
        if (metaTable.getColumnSet().contains(SqlConstants.CREATE_AT_COLUMN)) {
            return true;
        }
        return false;
    }

    public static String camelToUnderline(String column) {
        Objects.requireNonNull(column, "column");

        String val = UNDERLINE_COLUMNS.get(column);
        if (StringObject.isEmpty(val) == false) {
            return val;
        }

        acceptChar(column);

        val = StringObject.camelToUnderline(column);
        UNDERLINE_COLUMNS.put(column, val);
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
        if (StringObject.equals(primary, column)) {
            return true;
        }
        return IGNORES.contains(column);
    }

}
