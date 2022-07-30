package com.bytehonor.sdk.starter.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;

public abstract class ModelSetterMapper<T> implements RowMapper<T> {

    @Override
    public final T mapRow(ResultSet rs, int rowNum) throws SQLException {
        ModelSetter<T> setters = make(rs);
        Objects.requireNonNull(setters, "setters");
        return setters.model();
    }

    public abstract ModelSetter<T> make(ResultSet rs) throws SQLException;
}
