package com.bytehonor.sdk.starter.jdbc.model;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;

public class ModelGetterTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelGetterTest.class);

    @Test
    public void test() {

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setUpdateAt(now);
        student.setCreateAt(now);

        ModelGetter<Student> getter = ModelGetter.create(student);
        getter.add(Student::getId);
        getter.add(Student::getAge);
        getter.add(Student::getNickname);
        getter.add(Student::getUpdateAt);
        getter.add(Student::getCreateAt);

        List<ModelKeyValue> items = getter.getKvs();
        for (ModelKeyValue item : items) {
            LOG.info("key:{}, value:{}, javaType:{}", item.getKey(), item.getValue(), item.getJavaType());
        }
    }

    @Test
    public void test2() {
        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        int times = 1000000;
        for (int i = 0; i < times; i++) {
            ModelGetter<Student> getter = ModelGetter.create(student);
            getter.add(Student::getId);
            getter.add(Student::getAge);
            getter.add(Student::getNickname);
            getter.add(Student::getUpdateAt);
            getter.add(Student::getCreateAt);

            getter.getKvs();// 几乎不花时间
        }

        long millis = (System.currentTimeMillis() - now);
        LOG.info("times:{} cost millis:{}", times, millis);
    }

}
