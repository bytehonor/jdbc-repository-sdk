package com.bytehonor.sdk.starter.jdbc.model;

import org.junit.Test;

import com.bytehonor.sdk.starter.jdbc.UserProfile;

public class ModelGetterMapperTest {

    @Test
    public void test() {
        ModelGetterMapper<UserProfile> getterMapper = new ModelGetterMapper<UserProfile>() {

            @Override
            public ModelGetter<UserProfile> create(UserProfile model) {
                ModelGetter<UserProfile> getters = new ModelGetter<UserProfile>(model);

                getters.add(UserProfile::getId);

                getters.add(UserProfile::getUuid);
                getters.add(UserProfile::getName);
                getters.add(UserProfile::getAge);
                getters.add(UserProfile::getGender);
                getters.add(UserProfile::getIncome);

                getters.add(UserProfile::getPhone);
                getters.add(UserProfile::getOccupation);

                getters.add(UserProfile::getUpdateAt);
                getters.add(UserProfile::getCreateAt);
                return getters;
            }

        };

        getterMapper.toString();
    }

}
