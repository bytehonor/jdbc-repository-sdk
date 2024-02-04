package com.bytehonor.sdk.starter.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.bytehonor.sdk.starter.jdbc.UserProfile;

public class ModelSetterMapperTest {

    @Test
    public void test() {
        ModelSetterMapper<UserProfile> setterMapper = new ModelSetterMapper<UserProfile>() {

            @Override
            public ModelSetter<UserProfile> create(ResultSet rs) throws SQLException {
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

                return setter;
            }
        };

        setterMapper.toString();

    }

}
