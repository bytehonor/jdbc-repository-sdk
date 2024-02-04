package com.bytehonor.sdk.starter.jdbc.model;

import org.junit.Test;

import com.bytehonor.sdk.starter.jdbc.UserProfile;

public class ModelGetterMapperTest {

    @Test
    public void test() {
        ModelGetterMapper<UserProfile> getterMapper = new ModelGetterMapper<UserProfile>() {

            @Override
            public ModelGetter<UserProfile> create(UserProfile model) {
                ModelGetter<UserProfile> getter = new ModelGetter<UserProfile>(model);

                getter.add(UserProfile::getId);

                getter.add(UserProfile::getUuid);
                getter.add(UserProfile::getNickname);
                getter.add(UserProfile::getAge);
                getter.add(UserProfile::getGender);
                getter.add(UserProfile::getIncome);

                getter.add(UserProfile::getPhone);
                getter.add(UserProfile::getOccupation);

                getter.add(UserProfile::getUpdateAt);
                getter.add(UserProfile::getCreateAt);
                return getter;
            }

        };

        getterMapper.toString();
    }

}
