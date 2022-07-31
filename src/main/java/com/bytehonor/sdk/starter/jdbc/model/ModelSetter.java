package com.bytehonor.sdk.starter.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Supplier;

import com.bytehonor.sdk.lang.spring.function.Setters;
import com.bytehonor.sdk.lang.spring.function.setter.SetBoolean;
import com.bytehonor.sdk.lang.spring.function.setter.SetDouble;
import com.bytehonor.sdk.lang.spring.function.setter.SetInteger;
import com.bytehonor.sdk.lang.spring.function.setter.SetLong;
import com.bytehonor.sdk.lang.spring.function.setter.SetString;
import com.bytehonor.sdk.starter.jdbc.result.Results;

public class ModelSetter<T> {

    private final T model;

    private final ResultSet rs;

    public ModelSetter(T model, ResultSet rs) {
        Objects.requireNonNull(model, "model");

        this.model = model;
        this.rs = rs;
    }

    public static <T> ModelSetter<T> of(Supplier<T> sp, ResultSet rs) {
        return new ModelSetter<T>(sp.get(), rs);
    }

    public ModelSetter<T> add(SetString<T> setter) throws SQLException {
        String field = Setters.field(setter);
        String val = Results.string(rs, field);
        setter.accept(model, val);

        return this;
    }

    public ModelSetter<T> add(SetLong<T> setter) throws SQLException {
        String field = Setters.field(setter);
        Long val = Results.longer(rs, field);
        setter.accept(model, val);

        return this;
    }

    public ModelSetter<T> add(SetInteger<T> setter) throws SQLException {
        String field = Setters.field(setter);
        Integer val = Results.integer(rs, field);
        setter.accept(model, val);

        return this;
    }

    public ModelSetter<T> add(SetBoolean<T> setter) throws SQLException {
        String field = Setters.field(setter);
        Boolean val = Results.bool(rs, field);
        setter.accept(model, val);

        return this;
    }

    public ModelSetter<T> add(SetDouble<T> setter) throws SQLException {
        String field = Setters.field(setter);
        Double val = Results.doubler(rs, field);
        setter.accept(model, val);

        return this;
    }

    public T model() {
        return model;
    }
}
