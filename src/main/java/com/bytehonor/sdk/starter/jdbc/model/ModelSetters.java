package com.bytehonor.sdk.starter.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bytehonor.sdk.lang.spring.function.Setters;
import com.bytehonor.sdk.lang.spring.function.setter.SetBoolean;
import com.bytehonor.sdk.lang.spring.function.setter.SetDouble;
import com.bytehonor.sdk.lang.spring.function.setter.SetInteger;
import com.bytehonor.sdk.lang.spring.function.setter.SetLong;
import com.bytehonor.sdk.lang.spring.function.setter.SetString;
import com.bytehonor.sdk.starter.jdbc.result.Results;

public class ModelSetters {

    public static <T> void set(SetString<T> setter, T t, ResultSet rs) throws SQLException {
        String field = Setters.field(setter);
        String val = Results.string(rs, field);
        setter.accept(t, val);
    }

    public static <T> void set(SetLong<T> setter, T t, ResultSet rs) throws SQLException {
        String field = Setters.field(setter);
        Long val = Results.longer(rs, field);
        setter.accept(t, val);
    }

    public static <T> void set(SetInteger<T> setter, T t, ResultSet rs) throws SQLException {
        String field = Setters.field(setter);
        Integer val = Results.integer(rs, field);
        setter.accept(t, val);
    }

    public static <T> void set(SetBoolean<T> setter, T t, ResultSet rs) throws SQLException {
        String field = Setters.field(setter);
        Boolean val = Results.bool(rs, field);
        setter.accept(t, val);
    }

    public static <T> void set(SetDouble<T> setter, T t, ResultSet rs) {
    }
}
