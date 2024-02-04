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
                ModelSetter<UserProfile> setter = ModelSetter.of(UserProfile::new, rs);
                setter.add(UserProfile::setId);

                setter.add(UserProfile::setUuid);
                setter.add(UserProfile::setNickname);
                setter.add(UserProfile::setAge);
                setter.add(UserProfile::setGender);
                setter.add(UserProfile::setIncome);

                setter.add(UserProfile::setPhone);
                setter.add(UserProfile::setOccupation);

                setter.add(UserProfile::setUpdateAt);
                setter.add(UserProfile::setCreateAt);

                return setter.model();
            }
        };

        try {
            rowMapper.mapRow(null, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
