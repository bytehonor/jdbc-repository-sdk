package com.bytehonor.sdk.jdbc.bytehonor.model;

import org.junit.Test;

public class ModelGetterGroupTest {

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

        group.print(student);
    }

}
