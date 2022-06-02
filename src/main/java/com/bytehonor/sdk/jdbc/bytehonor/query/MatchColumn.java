package com.bytehonor.sdk.jdbc.bytehonor.query;

import java.sql.Types;
import java.util.List;
import java.util.Set;

import com.bytehonor.sdk.jdbc.bytehonor.jdbc.SqlOperator;
import com.bytehonor.sdk.lang.bytehonor.string.StringCreator;
import com.bytehonor.sdk.lang.bytehonor.util.ListJoinUtils;
import com.bytehonor.sdk.lang.bytehonor.util.SetJoinUtils;

public class MatchColumn {

    private String key;

    private Object value;

    private int type;

    private SqlOperator operator;

    public static MatchColumn eq(String key, String value) {
        return new MatchColumn(key, value, Types.VARCHAR, SqlOperator.EQ);
    }

    public static MatchColumn eq(String key, Long value) {
        return new MatchColumn(key, value, Types.BIGINT, SqlOperator.EQ);
    }

    public static MatchColumn eq(String key, Integer value) {
        return new MatchColumn(key, value, Types.INTEGER, SqlOperator.EQ);
    }

    public static MatchColumn eq(String key, Boolean value) {
        return new MatchColumn(key, value, Types.BOOLEAN, SqlOperator.EQ);
    }

    public static MatchColumn neq(String key, String value) {
        return new MatchColumn(key, value, Types.VARCHAR, SqlOperator.NEQ);
    }

    public static MatchColumn neq(String key, Long value) {
        return new MatchColumn(key, value, Types.BIGINT, SqlOperator.NEQ);
    }

    public static MatchColumn neq(String key, Integer value) {
        return new MatchColumn(key, value, Types.INTEGER, SqlOperator.NEQ);
    }

    public static MatchColumn neq(String key, Boolean value) {
        return new MatchColumn(key, value, Types.BOOLEAN, SqlOperator.NEQ);
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
    public static MatchColumn gt(String key, Long value) {
        return new MatchColumn(key, value, Types.BIGINT, SqlOperator.GT);
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
    public static MatchColumn gt(String key, Integer value) {
        return new MatchColumn(key, value, Types.INTEGER, SqlOperator.GT);
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
    public static MatchColumn egt(String key, Long value) {
        return new MatchColumn(key, value, Types.BIGINT, SqlOperator.EGT);
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
    public static MatchColumn egt(String key, Integer value) {
        return new MatchColumn(key, value, Types.INTEGER, SqlOperator.EGT);
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
    public static MatchColumn lt(String key, String value) {
        return new MatchColumn(key, value, Types.VARCHAR, SqlOperator.LT);
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
    public static MatchColumn lt(String key, Long value) {
        return new MatchColumn(key, value, Types.BIGINT, SqlOperator.LT);
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
    public static MatchColumn lt(String key, Integer value) {
        return new MatchColumn(key, value, Types.INTEGER, SqlOperator.LT);
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
    public static MatchColumn elt(String key, Long value) {
        return new MatchColumn(key, value, Types.BIGINT, SqlOperator.ELT);
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
    public static MatchColumn elt(String key, Integer value) {
        return new MatchColumn(key, value, Types.INTEGER, SqlOperator.ELT);
    }

    public static MatchColumn like(String key, String value) {
        return new MatchColumn(key, value, Types.VARCHAR, SqlOperator.LIKE);
    }

    public static MatchColumn in(String key, List<String> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(ListJoinUtils.joinStringSafe(value)).append(")").toString();
        }
        return new MatchColumn(key, src, Types.VARCHAR, SqlOperator.IN);
    }

    public static MatchColumn in(String key, Set<String> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(SetJoinUtils.joinStringSafe(value)).append(")").toString();
        }
        return new MatchColumn(key, src, Types.VARCHAR, SqlOperator.IN);
    }

    public static MatchColumn inLong(String key, List<Long> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(ListJoinUtils.joinLong(value)).append(")").toString();
        }
        return new MatchColumn(key, src, Types.BIGINT, SqlOperator.IN);
    }

    public static MatchColumn inLong(String key, Set<Long> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(SetJoinUtils.joinLong(value)).append(")").toString();
        }
        return new MatchColumn(key, src, Types.BIGINT, SqlOperator.IN);
    }

    public static MatchColumn inInt(String key, List<Integer> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(ListJoinUtils.joinInteger(value)).append(")").toString();
        }
        return new MatchColumn(key, src, Types.INTEGER, SqlOperator.IN);
    }

    public static MatchColumn inInt(String key, Set<Integer> value) {
        String src = null;
        if (value != null && value.isEmpty() == false) {
            src = StringCreator.create().append("(").append(SetJoinUtils.joinInteger(value)).append(")").toString();
        }
        return new MatchColumn(key, src, Types.INTEGER, SqlOperator.IN);
    }

    private MatchColumn(String key, Object value, int type, SqlOperator operator) {
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
