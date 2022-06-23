package com.bytehonor.sdk.starter.jdbc.query;

import java.util.Collection;

import com.bytehonor.sdk.lang.spring.creator.StringCreator;
import com.bytehonor.sdk.lang.spring.util.JoinUtils;
import com.bytehonor.sdk.lang.spring.util.StringObject;
import com.bytehonor.sdk.starter.jdbc.constant.SqlOperator;
import com.bytehonor.sdk.starter.jdbc.constant.SqlValueTypes;
import com.bytehonor.sdk.starter.jdbc.util.SqlColumnUtils;
import com.bytehonor.sdk.starter.jdbc.util.SqlInjectUtils;

public class SqlCondition {

    private String key;

    private Object value;

    /**
     * sql type
     */
    private int type;

    private SqlOperator operator;

    /**
     * 允许空字符串值做参数
     * 
     * @param condition
     * @return
     */
    public static boolean accept(SqlCondition condition) {
        if (StringObject.isEmpty(condition.getKey())) {
            return false;
        }
        if (condition.getValue() == null) {
            // 仅非null判断
            return false;
        }

        if (SqlOperator.LIKE.equals(condition.getOperator()) && StringObject.isEmpty(condition.getValue().toString())) {
            return false;
        }
        SqlColumnUtils.acceptChar(condition.getKey());

        condition.format();
        return true;
    }

    public void format() {
        this.key = SqlColumnUtils.camelToUnderline(key);
    }

    public static SqlCondition eq(String key, String value) {
        return new SqlCondition(key, value, SqlValueTypes.STRING, SqlOperator.EQ);
    }

    public static SqlCondition eq(String key, Long value) {
        return new SqlCondition(key, value, SqlValueTypes.LONG, SqlOperator.EQ);
    }

    public static SqlCondition eq(String key, Integer value) {

        return new SqlCondition(key, value, SqlValueTypes.INTEGER, SqlOperator.EQ);
    }

    public static SqlCondition eq(String key, Boolean value) {
        return new SqlCondition(key, value, SqlValueTypes.BOOLEAN, SqlOperator.EQ);
    }

    public static SqlCondition neq(String key, String value) {
        return new SqlCondition(key, value, SqlValueTypes.STRING, SqlOperator.NEQ);
    }

    public static SqlCondition neq(String key, Long value) {
        return new SqlCondition(key, value, SqlValueTypes.LONG, SqlOperator.NEQ);
    }

    public static SqlCondition neq(String key, Integer value) {
        return new SqlCondition(key, value, SqlValueTypes.INTEGER, SqlOperator.NEQ);
    }

    public static SqlCondition neq(String key, Boolean value) {
        return new SqlCondition(key, value, SqlValueTypes.BOOLEAN, SqlOperator.NEQ);
    }

    /**
     * <pre>
     * >
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public static SqlCondition gt(String key, Long value) {
        return new SqlCondition(key, value, SqlValueTypes.LONG, SqlOperator.GT);
    }

    /**
     * <pre>
     * >
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public static SqlCondition gt(String key, Integer value) {
        return new SqlCondition(key, value, SqlValueTypes.INTEGER, SqlOperator.GT);
    }

    /**
     * <pre>
     * >=
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public static SqlCondition egt(String key, Long value) {
        return new SqlCondition(key, value, SqlValueTypes.LONG, SqlOperator.EGT);
    }

    /**
     * <pre>
     * >=
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public static SqlCondition egt(String key, Integer value) {
        return new SqlCondition(key, value, SqlValueTypes.INTEGER, SqlOperator.EGT);
    }

    /**
     * <pre>
     * <
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public static SqlCondition lt(String key, Long value) {
        return new SqlCondition(key, value, SqlValueTypes.LONG, SqlOperator.LT);
    }

    /**
     * <pre>
     * <
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public static SqlCondition lt(String key, Integer value) {
        return new SqlCondition(key, value, SqlValueTypes.INTEGER, SqlOperator.LT);
    }

    /**
     * <pre>
     * <=
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public static SqlCondition elt(String key, Long value) {
        return new SqlCondition(key, value, SqlValueTypes.LONG, SqlOperator.ELT);
    }

    /**
     * <pre>
     * <=
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public static SqlCondition elt(String key, Integer value) {
        return new SqlCondition(key, value, SqlValueTypes.INTEGER, SqlOperator.ELT);
    }

    public static SqlCondition like(String key, String value) {
        return new SqlCondition(key, SqlInjectUtils.like(value, true, true), SqlValueTypes.STRING, SqlOperator.LIKE);
    }

    public static SqlCondition likeLeft(String key, String value) {
        return new SqlCondition(key, SqlInjectUtils.like(value, true, false), SqlValueTypes.STRING, SqlOperator.LIKE);
    }

    public static SqlCondition likeRight(String key, String value) {
        return new SqlCondition(key, SqlInjectUtils.like(value, false, true), SqlValueTypes.STRING, SqlOperator.LIKE);
    }

    public static SqlCondition strings(String key, Collection<String> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(JoinUtils.stringsSafe(value)).append(")").toString();
        }
        return new SqlCondition(key, src, SqlValueTypes.STRING, SqlOperator.IN);
    }

    public static SqlCondition longs(String key, Collection<Long> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(JoinUtils.longs(value)).append(")").toString();
        }
        return new SqlCondition(key, src, SqlValueTypes.LONG, SqlOperator.IN);
    }

    public static SqlCondition integers(String key, Collection<Integer> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(JoinUtils.integers(value)).append(")").toString();
        }
        return new SqlCondition(key, src, SqlValueTypes.INTEGER, SqlOperator.IN);
    }

    private SqlCondition(String key, Object value, int type, SqlOperator operator) {
        this.key = key;
        this.value = value;
        this.type = type;
        this.operator = operator;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SqlOperator getOperator() {
        return operator;
    }

    public void setOperator(SqlOperator operator) {
        this.operator = operator;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
