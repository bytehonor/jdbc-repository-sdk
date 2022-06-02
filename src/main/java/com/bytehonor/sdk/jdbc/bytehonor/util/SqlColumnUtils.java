package com.bytehonor.sdk.jdbc.bytehonor.util;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.bytehonor.sdk.define.bytehonor.util.StringObject;

public class SqlColumnUtils {

    private static final Map<String, String> UNDERLINE_COLUMNS = new ConcurrentHashMap<String, String>();

    public static String camelToUnderline(String src) {
        Objects.requireNonNull(src, "src");

        String val = UNDERLINE_COLUMNS.get(src);
        if (StringObject.isEmpty(val) == false) {
            return val;
        }
        val = StringObject.camelToUnderline(src);
        UNDERLINE_COLUMNS.put(src, val);
        return val;
    }
}
