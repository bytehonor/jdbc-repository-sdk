package com.bytehonor.sdk.starter.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.springframework.jdbc.core.RowMapper;

import com.bytehonor.sdk.starter.jdbc.UserProfile;

public class ModelSetterTest {

    @Test
    public void test() {
        RowMapper<UserProfile> rowMapper = new RowMapper<UserProfile>() {

            @Override
            public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
                ModelSetter<UserProfile> setters = ModelSetter.create(UserProfile::new, rs);
                setters.add(UserProfile::setId);

                setters.add(UserProfile::setUuid);
                setters.add(UserProfile::setName);
                setters.add(UserProfile::setAge);
                setters.add(UserProfile::setGender);
                setters.add(UserProfile::setIncome);

                setters.add(UserProfile::setPhone);
                setters.add(UserProfile::setOccupation);

                setters.add(UserProfile::setUpdateAt);
                setters.add(UserProfile::setCreateAt);

                return setters.model();
            }
        };

        try {
            rowMapper.mapRow(null, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
