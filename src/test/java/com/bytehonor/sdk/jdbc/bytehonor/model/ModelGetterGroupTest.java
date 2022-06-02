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

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setAge(1);
        student.setNickname("boy");
        student.setId(123L);
        student.setCreateAt(now);
        student.setUpdateAt(now);

        List<ModelColumn> items = group.out(student);
        for (ModelColumn item : items) {
            LOG.info("key:{}, value:{}, type:{}", item.getKey(), item.getValue(), item.getType());
        }
    }

}
