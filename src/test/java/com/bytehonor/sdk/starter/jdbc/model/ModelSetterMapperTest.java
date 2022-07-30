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
            public ModelSetter<UserProfile> make(ResultSet rs) throws SQLException {
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

                return setters;
            }
        };

        setterMapper.toString();

    }

}
