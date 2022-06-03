package com.bytehonor.sdk.jdbc.bytehonor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlConstants;
import com.bytehonor.sdk.jdbc.bytehonor.meta.MetaTable;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelColumnValue;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelSavePrepareResult;
import com.bytehonor.sdk.lang.bytehonor.regex.PatternUtils;

public class SqlColumnUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SqlColumnUtils.class);

    private static final char UNDERLINE = '_';

    private static final Map<String, String> UNDERLINE_COLUMNS = new ConcurrentHashMap<String, String>();

    public static ModelSavePrepareResult prepare(MetaTable metaTable, List<ModelColumnValue> items, boolean insert) {
        Objects.requireNonNull(metaTable, "metaTable");

        ModelSavePrepareResult result = new ModelSavePrepareResult();

        List<String> columns = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();

        String primary = metaTable.getPrimaryKey();
        for (ModelColumnValue item : items) {
            if (SqlColumnUtils.isSaveIgnore(primary, item.getColumn())) {
                LOG.debug("prepare ({}) pass", item.getColumn());
                continue;
            }
            LOG.info("column:{}, value:{}, type:{}", item.getColumn(), item.getValue(), item.getType());
            columns.add(item.getColumn());
            values.add(item.getValue());
        }

        long now = System.currentTimeMillis();
        if (enabledUpdateAt(metaTable)) {
            columns.add(SqlConstants.UPDATE_AT_COLUMN);
            values.add(now);
        }
        if (insert && enabledCreateAt(metaTable)) {
            columns.add(SqlConstants.CREATE_AT_COLUMN);
            values.add(now);
        }

        result.setColumns(columns);
        result.setValues(values);
        return result;
    }

    private static boolean enabledUpdateAt(MetaTable metaTable) {
        if (metaTable.getKeySet().contains(SqlConstants.UPDATE_AT_KEY)) {
            return true;
        }
        if (metaTable.getColumnSet().contains(SqlConstants.UPDATE_AT_COLUMN)) {
            return true;
        }
        return false;
    }

    private static boolean enabledCreateAt(MetaTable metaTable) {
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

        accept(column);

        val = StringObject.camelToUnderline(column);
        UNDERLINE_COLUMNS.put(column, val);
        return val;
    }

    public static void accept(String column) {
        Objects.requireNonNull(column, "column");

        int len = column.length();
        if (len < 1) {
            throw new RuntimeException("column cannt be empty, column:" + column);
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
            throw new RuntimeException("column only accept _ number letter, column:" + column);
        }
    }

    public static boolean isSaveIgnore(String primary, String column) {
        if (StringObject.equals(primary, column)) {
            return true;
        }
        if (SqlConstants.CREATE_AT_COLUMN.equals(column)) {
            return true;
        }
        if (SqlConstants.UPDATE_AT_COLUMN.equals(column)) {
            return true;
        }
        return false;
    }
}
