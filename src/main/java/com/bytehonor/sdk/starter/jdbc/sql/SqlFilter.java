package com.bytehonor.sdk.starter.jdbc.sql;

import java.util.Collection;

import com.bytehonor.sdk.lang.spring.constant.JavaValueTypes;
import com.bytehonor.sdk.lang.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.string.SpringString;
import com.bytehonor.sdk.lang.spring.string.StringCreator;
import com.bytehonor.sdk.lang.spring.util.JoinUtils;
import com.bytehonor.sdk.starter.jdbc.constant.SqlValueTypes;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class SqlFilter {

    /**
     * 忽略驼峰或下划线
     */
    private final String key;

    private final Object value;

    private final int sqlType;

    private final String javaType;

    private final SqlOperator operator;

    /**
     * 允许空字符串做value
     * 
     * @param filter
     * @return
     */
    public static boolean accept(SqlFilter filter) {
        if (filter == null) {
            return false;
        }

        if (SpringString.isEmpty(filter.getKey())) {
            return false;
        }

        if (filter.getValue() == null) {
            // 仅非null判断
            return false;
        }

        if (SqlOperator.LIKE.equals(filter.getOperator()) && SpringString.isEmpty(filter.getValue().toString())) {
            return false;
        }

        SqlColumnUtils.acceptChar(filter.getKey());

        return true;
    }

    public static SqlFilter eq(String key, String value) {
        return create(key, value, SqlValueTypes.STRING, SqlOperator.EQ);
    }

    public static SqlFilter eq(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.EQ);
    }

    public static SqlFilter eq(String key, Integer value) {

        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.EQ);
    }

    public static SqlFilter eq(String key, Boolean value) {
        return create(key, value, SqlValueTypes.BOOLEAN, SqlOperator.EQ);
    }

    public static SqlFilter neq(String key, String value) {
        return create(key, value, SqlValueTypes.STRING, SqlOperator.NEQ);
    }

    public static SqlFilter neq(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.NEQ);
    }

    public static SqlFilter neq(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.NEQ);
    }

    public static SqlFilter neq(String key, Boolean value) {
        return create(key, value, SqlValueTypes.BOOLEAN, SqlOperator.NEQ);
    }

    public static SqlFilter gt(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.GT);
    }

    public static SqlFilter gt(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.GT);
    }

    public static SqlFilter egt(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.EGT);
    }

    public static SqlFilter egt(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.EGT);
    }

    public static SqlFilter lt(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.LT);
    }

    public static SqlFilter lt(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.LT);
    }

    public static SqlFilter elt(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.ELT);
    }

    public static SqlFilter elt(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.ELT);
    }

    public static SqlFilter like(String key, String value) {
        return create(key, SqlInjectUtils.like(value, true, true), SqlValueTypes.STRING, SqlOperator.LIKE);
    }

    public static SqlFilter likeLeft(String key, String value) {
        return create(key, SqlInjectUtils.like(value, true, false), SqlValueTypes.STRING, SqlOperator.LIKE_LEFT);
    }

    public static SqlFilter likeRight(String key, String value) {
        return create(key, SqlInjectUtils.like(value, false, true), SqlValueTypes.STRING, SqlOperator.LIKE_RIGHT);
    }

    public static <T> SqlFilter in(String key, Collection<T> value, Class<T> type) {
        return in(key, value, type.getName());
    }

    public static <T> SqlFilter in(String key, Collection<T> value, String type) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            if (JavaValueTypes.STRING.equals(type)) {
                src = appendIn(JoinUtils.joinSafe(value));
            } else {
                src = appendIn(JoinUtils.join(value));
            }
        }
        return create(key, src, SqlValueTypes.STRING, SqlOperator.IN);
    }

//    public static SqlMatcher ins(String key, Collection<String> value) {
//        String src = null;
//        if (value != null && value.isEmpty() == false) {
//            src = appendIn(JoinUtils.stringsSafe(value));
//        }
//        return create(key, src, SqlValueTypes.STRING, SqlOperator.IN);
//    }
//
//    public static SqlMatcher inl(String key, Collection<Long> value) {
//        String src = null;
//        if (value != null && value.isEmpty() == false) {
//            src = appendIn(JoinUtils.longs(value));
//        }
//        return create(key, src, SqlValueTypes.LONG, SqlOperator.IN);
//    }
//
//    public static SqlMatcher ini(String key, Collection<Integer> value) {
//        String src = null;
//        if (value != null && value.isEmpty() == false) {
//            src = appendIn(JoinUtils.integers(value));
//        }
//        return create(key, src, SqlValueTypes.INTEGER, SqlOperator.IN);
//    }

    public static String appendIn(String raw) {
        return StringCreator.create().append("(").append(raw).append(")").toString();
    }

    public static SqlFilter create(String key, Object value, int sqlType, SqlOperator operator) {
        return new SqlFilter(key, value, sqlType, SqlAdaptUtils.toJavaType(sqlType), operator);
    }

    public static SqlFilter create(String key, Object value, String javaType, SqlOperator operator) {
        return new SqlFilter(key, value, SqlAdaptUtils.toSqlType(javaType), javaType, operator);
    }

    private SqlFilter(String key, Object value, int sqlType, String javaType, SqlOperator operator) {
        this.key = key;
        this.value = value;
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.operator = operator;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public int getSqlType() {
        return sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public SqlOperator getOperator() {
        return operator;
    }

}
