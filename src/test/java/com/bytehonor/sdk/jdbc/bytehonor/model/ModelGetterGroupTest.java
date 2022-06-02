package com.bytehonor.sdk.jdbc.bytehonor.model;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.Student;

public class ModelGetterGroupTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelGetterGroupTest.class);

    @Test
    public void test() {
        ModelGetterGroup<Student> group = ModelGetterGroup.create(Student.class);
        group.add("id", Student::getId);
        group.add("age", Student::getAge);
        group.add("nickname", Student::getNickname);
        group.add("updateAt", Student::getNickname);
        group.add("createAt", Student::getNickname);

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        List<ModelColumnValue> items = group.out(student);
        for (ModelColumnValue item : items) {
            LOG.info("key:{}, value:{}, type:{}", item.getColumn(), item.getValue(), item.getType());
        }
    }

}
