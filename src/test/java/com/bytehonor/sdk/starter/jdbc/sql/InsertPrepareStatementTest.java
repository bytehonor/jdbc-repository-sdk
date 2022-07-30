package com.bytehonor.sdk.starter.jdbc.sql;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.starter.jdbc.Student;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetterMapper;
import com.bytehonor.sdk.starter.jdbc.model.ModelGetter;

public class InsertPrepareStatementTest {

    private static final Logger LOG = LoggerFactory.getLogger(InsertPrepareStatementTest.class);

    private static final ModelGetterMapper<Student> MAPPER = new ModelGetterMapper<Student>() {

        @Override
        public ModelGetter<Student> create(Student model) {
            ModelGetter<Student> getters = new ModelGetter<Student>(model);

            getters.add(Student::getId);
            getters.add(Student::getAge);
            getters.add(Student::getNickname);
            getters.add(Student::getUpdateAt);
            getters.add(Student::getCreateAt);
            return getters;
        }

    };

    @Test
    public void test() {

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("boy");
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (age,nickname,update_at,create_at) VALUES (?,?,?,?)";
        assertTrue("test", target.equals(sql) && args.length == 4);
    }

    @Test
    public void testValueNull() {

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname(null); // null, 不入
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testValueNull sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (age,update_at,create_at) VALUES (?,?,?)";
        assertTrue("testValueNull", target.equals(sql) && args.length == 3);
    }

    @Test
    public void testValueEmpty() {

        long now = System.currentTimeMillis();
        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname(""); // 空, 入
        student.setCreateAt(now);
        student.setUpdateAt(now);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testValueEmpty sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (age,nickname,update_at,create_at) VALUES (?,?,?,?)";
        assertTrue("testValueEmpty", target.equals(sql) && args.length == 4);
    }

    @Test
    public void testTimeNull() {

        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("TimeNull");
        student.setCreateAt(null);
        student.setUpdateAt(null);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, MAPPER);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testTimeNull sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (age,nickname,update_at,create_at) VALUES (?,?,?,?)";
        assertTrue("testTimeNull", target.equals(sql) && args.length == 4);
    }

    @Test
    public void testGetterLess() {

        ModelGetterMapper<Student> less = new ModelGetterMapper<Student>() {

            @Override
            public ModelGetter<Student> create(Student model) {
                ModelGetter<Student> getters = new ModelGetter<Student>(model);

                getters.add(Student::getNickname);
                return getters;
            }

        };

        Student student = new Student();
        student.setId(123L);
        student.setAge(1);
        student.setNickname("TimeNull");
        student.setCreateAt(null);
        student.setUpdateAt(null);

        PrepareStatement statement = new InsertPrepareStatement(Student.class);
        statement.prepare(student, less);

        String sql = statement.sql();
        Object[] args = statement.args();

        LOG.info("testGetterLess sql:{}", sql);
        statement.check();

        String target = "INSERT INTO tbl_student (nickname,update_at,create_at) VALUES (?,?,?)";
        assertTrue("testGetterLess", target.equals(sql) && args.length == 3);
    }
}
