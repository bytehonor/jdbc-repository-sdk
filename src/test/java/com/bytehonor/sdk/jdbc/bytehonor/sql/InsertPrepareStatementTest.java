package com.bytehonor.sdk.jdbc.bytehonor.sql;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.jdbc.bytehonor.model.ModelGetterGroup;
import com.bytehonor.sdk.jdbc.bytehonor.model.ModelMapper;
import com.bytehonor.sdk.jdbc.bytehonor.model.Student;
import com.bytehonor.sdk.jdbc.bytehonor.query.QueryCondition;

public class InsertPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(InsertPrepareStatementTest.class);

    @Test
    public void test() {
        ModelMapper<Student> mapper = new ModelMapper<Student>() {

            @Override
            public ModelGetterGroup<Student> create() {
                ModelGetterGroup<Student> group = ModelGetterGroup.create(Student.class);
                group.add("age", Student::getAge);
                group.add("nickname", Student::getNickname);
                group.add("create_at", Student::getCreateAt);
                return group;
            }

        };

        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        QueryCondition condition = QueryCondition.create();
        condition.inInt("age", set);
        condition.gt("create_at", System.currentTimeMillis());
        condition.like("nickname", "boy");
        condition.descBy("age");

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setAge(1);
        student.setNickname("boy");
        student.setId(123L);
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, mapper);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        for (Object arg : args) {
            LOG.info("arg:{}", arg);
        }

        assertTrue("test", args.length > 2);
    }

}
