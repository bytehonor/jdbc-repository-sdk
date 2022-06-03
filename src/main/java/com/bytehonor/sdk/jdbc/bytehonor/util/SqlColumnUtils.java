package com.bytehonor.sdk.jdbc.bytehonor.util;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;
import com.bytehonor.sdk.jdbc.bytehonor.constant.SqlConstants;
import com.bytehonor.sdk.lang.bytehonor.regex.PatternUtils;

public class SqlColumnUtils {

    private static final char UNDERLINE = '_';

    private static final Map<String, String> UNDERLINE_COLUMNS = new ConcurrentHashMap<String, String>();

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
