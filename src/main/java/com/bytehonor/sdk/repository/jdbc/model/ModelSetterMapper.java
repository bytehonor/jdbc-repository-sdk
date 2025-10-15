package com.bytehonor.sdk.repository.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;

public abstract class ModelSetterMapper<T> implements RowMapper<T> {

    @Override
    public final T mapRow(ResultSet rs, int rowNum) throws SQLException {
        ModelSetter<T> setters = create(rs);
        Objects.requireNonNull(setters, "setters");
        return setters.model();
    }

    public abstract ModelSetter<T> create(ResultSet rs) throws SQLException;
}
