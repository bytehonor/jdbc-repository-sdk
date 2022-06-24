package com.bytehonor.sdk.starter.jdbc.condition;

import java.util.Collection;

import com.bytehonor.sdk.define.spring.constant.SqlOperator;
import com.bytehonor.sdk.lang.spring.creator.StringCreator;
import com.bytehonor.sdk.lang.spring.util.JoinUtils;
import com.bytehonor.sdk.lang.spring.util.StringObject;
import com.bytehonor.sdk.starter.jdbc.constant.SqlValueTypes;
import com.bytehonor.sdk.starter.jdbc.util.SqlAdaptUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class SqlColumn {

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
     * @param column
     * @return
     */
    public static boolean accept(SqlColumn column) {
        if (column == null) {
            return false;
        }

        if (StringObject.isEmpty(column.getKey())) {
            return false;
        }

        if (column.getValue() == null) {
            // 仅非null判断
            return false;
        }

        if (SqlOperator.LIKE.equals(column.getOperator()) && StringObject.isEmpty(column.getValue().toString())) {
            return false;
        }

        SqlColumnUtils.acceptChar(column.getKey());

        return true;
    }

    public static SqlColumn eq(String key, String value) {
        return create(key, value, SqlValueTypes.STRING, SqlOperator.EQ);
    }

    public static SqlColumn eq(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.EQ);
    }

    public static SqlColumn eq(String key, Integer value) {

        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.EQ);
    }

    public static SqlColumn eq(String key, Boolean value) {
        return create(key, value, SqlValueTypes.BOOLEAN, SqlOperator.EQ);
    }

    public static SqlColumn neq(String key, String value) {
        return create(key, value, SqlValueTypes.STRING, SqlOperator.NEQ);
    }

    public static SqlColumn neq(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.NEQ);
    }

    public static SqlColumn neq(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.NEQ);
    }

    public static SqlColumn neq(String key, Boolean value) {
        return create(key, value, SqlValueTypes.BOOLEAN, SqlOperator.NEQ);
    }

    public static SqlColumn gt(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.GT);
    }

    public static SqlColumn gt(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.GT);
    }

    public static SqlColumn egt(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.EGT);
    }

    public static SqlColumn egt(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.EGT);
    }

    public static SqlColumn lt(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.LT);
    }

    public static SqlColumn lt(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.LT);
    }

    public static SqlColumn elt(String key, Long value) {
        return create(key, value, SqlValueTypes.LONG, SqlOperator.ELT);
    }

    public static SqlColumn elt(String key, Integer value) {
        return create(key, value, SqlValueTypes.INTEGER, SqlOperator.ELT);
    }

    public static SqlColumn like(String key, String value) {
        return create(key, SqlInjectUtils.like(value, true, true), SqlValueTypes.STRING, SqlOperator.LIKE);
    }

    public static SqlColumn likeLeft(String key, String value) {
        return create(key, SqlInjectUtils.like(value, true, false), SqlValueTypes.STRING, SqlOperator.LIKE_LEFT);
    }

    public static SqlColumn likeRight(String key, String value) {
        return create(key, SqlInjectUtils.like(value, false, true), SqlValueTypes.STRING, SqlOperator.LIKE_RIGHT);
    }

    public static SqlColumn strings(String key, Collection<String> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = appendIn(JoinUtils.stringsSafe(value));
        }
        return create(key, src, SqlValueTypes.STRING, SqlOperator.IN);
    }

    public static SqlColumn longs(String key, Collection<Long> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = appendIn(JoinUtils.longs(value));
        }
        return create(key, src, SqlValueTypes.LONG, SqlOperator.IN);
    }

    public static SqlColumn integers(String key, Collection<Integer> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = appendIn(JoinUtils.integers(value));
        }
        return create(key, src, SqlValueTypes.INTEGER, SqlOperator.IN);
    }

    public static String appendIn(String raw) {
        return StringCreator.create().append("(").append(raw).append(")").toString();
    }

    public static SqlColumn create(String key, Object value, int sqlType, SqlOperator operator) {
        return new SqlColumn(key, value, sqlType, SqlAdaptUtils.toJavaType(sqlType), operator);
    }

    public static SqlColumn create(String key, Object value, String javaType, SqlOperator operator) {
        return new SqlColumn(key, value, SqlAdaptUtils.toSqlType(javaType), javaType, operator);
    }

    private SqlColumn(String key, Object value, int sqlType, String javaType, SqlOperator operator) {
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
